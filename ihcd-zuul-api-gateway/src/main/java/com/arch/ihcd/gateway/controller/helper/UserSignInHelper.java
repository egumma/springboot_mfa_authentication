package com.arch.ihcd.gateway.controller.helper;

import com.arch.ihcd.gateway.bean.*;
import com.arch.ihcd.gateway.config.*;
import com.arch.ihcd.gateway.entity.EmailOutboxTrans;
import com.arch.ihcd.gateway.entity.SmsOutboxTrans;
import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.exception.GenericException;
import com.arch.ihcd.gateway.exception.OAuthClientGetAccessTokenException;
import com.arch.ihcd.gateway.exception.UserNotFoundException;
import com.arch.ihcd.gateway.exception.UserValidationException;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.arch.ihcd.gateway.request.*;
import com.arch.ihcd.gateway.response.*;
import com.arch.ihcd.gateway.service.barcode.BarcodeService;
import com.arch.ihcd.gateway.service.email.EmailService;
import com.arch.ihcd.gateway.service.mobile.MobileService;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.DateUtil;
import com.arch.ihcd.gateway.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserSignInHelper {

    @Value("${ihcd.frontend.home}")
    private String IHCD_HOST_URL;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BarcodeService barcodeService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserConfig userConfig;

    public User registerUser(UserCreationRequest request, String role){    	
        User user = userRepository.findByUsername(request.getUsername());
        if(user != null) {
            throw new GenericException(AppConstants.ERROR_USER_ALREADY_EXIST+" - "+user.getUsername());
        }
        Role USER = new Role();
        USER.setRole(role);
        Privilege USER_ACCESS1 = new Privilege();
        USER_ACCESS1.setPrivilege("USER_ACCESS1");//ToDo: replace dummy
        Privilege USER_ACCESS2 = new Privilege();
        USER_ACCESS2.setPrivilege("USER_ACCESS2");//ToDo: replace dummy

        //ToDo: read dynamic values
        boolean isQRAuthEnabled = AppConstants.IS_QRCODE_DEFAULT_ENABLE;
        boolean isEmailVerifyEnabled = AppConstants.IS_EMAIL_VERIFY_DEFAULT_ENABLE;
        boolean isMobileVerifyEnabled = AppConstants.IS_MOBILE_VERIFY_DEFAULT_ENABLE;

        User userToSave = new User();
        userToSave.setQRAuthEnabled(isQRAuthEnabled);
        if(isQRAuthEnabled){
            userToSave.setQrSecretcode(barcodeService.generate2faQRCodeSecretKey());
        }
        //Mobile verification
        userToSave.setMobileverifyenabled(isMobileVerifyEnabled);
        //Email Verification
        userToSave.setEmailverifyenabled(isEmailVerifyEnabled);
        if(isEmailVerifyEnabled){
            TwoFACode email2FACode = emailService.getEmail2FACode();
            userToSave.setEmailotpvalidtime(email2FACode.getValidTime());
            userToSave.setEmailotp(email2FACode.getCode());
            EmailOutboxTrans email = new EmailOutboxTrans();
            email.setTo(request.getEmail());//Email or username
            email.setMessageType("Email verification");
            email.setSubject("Email verification");
            //ToDo: Define templates for message body
            StringBuilder body = new StringBuilder("Dear "+request.getFirstname()+", <br/>");
            String passEmail = org.springframework.util.StringUtils.isEmpty(request.getPassword()) ? "ihcd$123" : request.getPassword();
            body.append("Your default password is "+passEmail+"  <br/>");
            body.append("Please click the below link to activate your iHCD account.  <br/>");
            //body.append(IHCD_HOST_URL).append("/auth/activate/"+request.getEmail()+"/"+email2FACode.getCode());
            String link = IHCD_HOST_URL+"/auth/activate/"+request.getEmail()+"/"+email2FACode.getCode();
            String hyperLink = "<a href=\""+link+"\">Activate account</a>";
            body.append(hyperLink);
            body.append("<br/><br/>Thank you, <br/>");
            body.append("Team iHCD.");
            email.setMessage(body.toString());
            email.setCreatedby(userToSave.getUsername());
            email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
            email.setModifiedby(userToSave.getUsername());
            email.setModifieddtm(DateUtil.getTimeInMilliSeconds());
            emailService.saveEmailDetails(email);
        }

        userToSave.setSigninToken("");
        userToSave.setUsername(request.getUsername());
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        userToSave.setEmail(request.getEmail());
        userToSave.setMobile(request.getMobile());
        if(request.getFirstname() != null){
        userToSave.setFirstname(org.springframework.util.StringUtils.capitalize(request.getFirstname()));
        }
        if(request.getLastname() != null){
            userToSave.setLastname(org.springframework.util.StringUtils.capitalize(request.getLastname()));
        }

        userToSave.setAddresses(request.getAddresses());
        userToSave.setRoles(request.getRoles());
        userToSave.setPrivileges(new HashSet<>(Arrays.asList(USER_ACCESS1, USER_ACCESS2)));
        userToSave.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        userToSave.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        //String userId = userConfig.getLoginUser(); No user will be available
        userToSave.setCreatedby(userToSave.getUsername());
        userToSave.setModifiedby(userToSave.getUsername());
        return userRepository.save(userToSave);

    }

    public User getUserDetails(String username){
        System.out.println("------>"+username);
        User user = userRepository.findByUsername(username);
        if(user==null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+user.getUsername());
        }
        return user;
    }

    public String saveUserExperience(String userId, ExperienceInfo expInfo){
        System.out.println("------>saveUserExperience: "+userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+userId);
        }
        User user = userOptional.get();
        user.setExpInfo(expInfo);
        userRepository.save(user);
        return "Success";
    }

    public ExperienceInfo getUserExperience(String userId){
        System.out.println("------>getUserExperience: "+userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if(!userOptional.isPresent()) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+userId);
        }
        return userOptional.get().getExpInfo();
    }

    public UserPreSignInResponse userPreSignInCheck(LoginViewModel credentials){
        User user = userRepository.findByUsername(credentials.getUsername());
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+credentials.getUsername());
        }
        String token = barcodeService.generateToken(credentials);
        String brCodeURL = user.isQRAuthEnabled() && !user.isQRSetupdone() ? barcodeService.getGoogleAuthenticatorBarCode(user.getQrSecretcode(), user.getUsername(), AppConstants.QR_CODE_ISSUER): "";

        //Mobile verification code sent
        if(user.isMobileverifyenabled() & !user.isMobileverified()){
            TwoFACode mobileCode = mobileService.getMobile2FACode();
            user.setMobileotp(mobileCode.getCode());
            user.setMobileotpvalidtime(mobileCode.getValidTime());
            //Sms details
            SmsOutboxTrans sms = new SmsOutboxTrans();
            sms.setTo(user.getMobile());

            //ToDo: Define templates for message body
            sms.setMessage(AppConstants.SMS_USER_AC_ACTIVATION.concat(mobileCode.getCode()));

            sms.setMessageType("Mobile Verification");
            sms.setCreatedby(user.getId());
            sms.setCreateddtm(DateUtil.getTimeInMilliSeconds());
            sms.setModifiedby(user.getId());
            sms.setModifieddtm(DateUtil.getTimeInMilliSeconds());
            mobileService.saveSMSDetails(sms);
        }
        user.setSigninToken(token);//So that this token can be validated for other requests
        userRepository.save(user);
        UserPreSignInResponse response = new UserPreSignInResponse(user.getId(),
                user.getUsername(),
                this.hideEmail(user.getEmail()),//ToDo: send email partial details like xxx@gmail.com
                this.hideMobile(user.getMobile()!=null? user.getMobile() : ""),//ToDo: send mobile partial details
                token,
                user.isMobileverifyenabled(),
                user.isMobileverified(),
                user.getMobileotpvalidtime(),
                user.isEmailverifyenabled(),
                user.isEmailverified(),
                user.getEmailotpvalidtime(),
                user.isQRAuthEnabled(),
                user.isQRSetupdone(),
                false,
                user.isQRAuthEnabled() && !user.isQRSetupdone()? brCodeURL : "",
                user.getFirstname()+" "+(org.springframework.util.StringUtils.isEmpty(user.getLastname())? "" : user.getLastname()));
        System.out.println("------->Presignin complete: "+brCodeURL);
        return response;
    }

    public TFAResponse verifyTFAUserMobile(LoginViewModel credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        long currentTime = DateUtil.getTimeInMilliSeconds();
        long otpValidTime = user.getMobileotpvalidtime();
        System.out.println("------>Received mobile OTP: "+credentials.getMobileotp());
        if(credentials.getMobileotp() == null){
            throw new UserValidationException("Provide Mobile OTP Code");
        } else if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+credentials.getUsername());
        } else if(user.getMobileotp() == null || !user.getMobileotp().equals(credentials.getMobileotp())){
            throw new UserValidationException("Invalid Mobile OTP Code");
        } else if(currentTime > otpValidTime){
            throw new UserValidationException("Mobile OTP Code is expired");
        }
        user.setMobileverified(true);
        user.setMobileverifiedtime(DateUtil.getTimeInMilliSeconds());

        userRepository.save(user);
        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_MOBILE);
        response.setMessage("Mobile OTP Verified successfully");
        return response;
    }

    public TFAResponse verifyTFAUserEmail(String username, String otp){
        User user = userRepository.findByUsername(username);
        long currentTime = DateUtil.getTimeInMilliSeconds();
        long otpValidTime = user.getEmailotpvalidtime();
        if(otp == null){
            throw new UserValidationException("Provide Email OTP Code");
        } else if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(user.getEmailotp()== null || !user.getEmailotp().equals(otp)){
            throw new UserValidationException("Invalid Email OTP Code");
        } else if(currentTime > otpValidTime){
            throw new UserValidationException("Email OTP Code is expired, Please contact helpdesk");
        }
        user.setEmailverified(true);
        user.setEmailverifiedtime(DateUtil.getTimeInMilliSeconds());
        userRepository.save(user);
        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_EMAIL);
        response.setMessage("Email OTP Verified successfully");
        return response;
    }

    public UserSignInResponse signInUser(LoginViewModel credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+credentials.getUsername());
        }
        //String token = barcodeService.generateToken(credentials);
        if(user.isEmailverifyenabled() && !user.isEmailverified()){
            throw new UserValidationException("User mail verification pending");
        } if(user.isMobileverifyenabled() && !user.isMobileverified()){
            throw new UserValidationException("User mobile verfication pending");
        } else if(user.isQRAuthEnabled()) {
            String systemQRCode = barcodeService.getTOTPCode(user.getQrSecretcode());
            System.out.println("------->QR code input : "+credentials.getQrauthcode());
            System.out.println("------->QR code System: "+systemQRCode);
            if(credentials.getQrauthcode()!=null
                    && credentials.getQrauthcode().equals(systemQRCode)){
                user.setQRSetupdone(true);
                user.setQRCodeverified(true);
                System.out.println("------->QR code success");
            } else {
                System.out.println("------->QR code failure");
                throw new OAuthClientGetAccessTokenException("Auhentication code may expired/invalid.", "");
            }
        }

        //Reset profile edit
        user.setEditprofileon(false);
        user.setEditprofileaverified(false);
        user.setEditprofilecode("");

        //user.setSigninToken(token);
        //ToDo: Create seperate tx table to track user signin & signout timings
        user.setSigninTime(DateUtil.getTimeInMilliSeconds());
        /*user.setModifieddtm(DateUtil.getTimeInMilliSeconds());*/
        //ToDo: modifiedby
        userRepository.save(user);

        UserSignInResponse response = new UserSignInResponse(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getSigninToken(),
                user.getRoles(),
                user.getPrivileges(),
                /*user.getResourceServerTokens()*/ new HashSet<>(0),
                user.getFirstname()+" "+(org.springframework.util.StringUtils.isEmpty(user.getLastname())? "" : user.getLastname()));
        return response;
    }

    public TFAResponse changeUserMobile(String username, String mobile){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }
        String loginUserId = userConfig.getLoginUser();
        TwoFACode code = mobileService.getMobile2FACode();
        user.setMobileverified(false);
        user.setMobileotpvalidtime(code.getValidTime());
        user.setMobileotp(code.getCode());
        user.setMobile(mobile);//Mobile update
        userRepository.save(user);

        //Sms OTP
        SmsOutboxTrans sms = new SmsOutboxTrans();
        sms.setTo(user.getMobile());
        //ToDo: Define templates for message body
        sms.setMessage(AppConstants.SMS_USER_AC_ACTIVATION.concat(code.getCode()));
        sms.setMessageType("Change Mobile Verification");
        sms.setCreatedby(loginUserId);
        sms.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        sms.setModifiedby(loginUserId);
        sms.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        mobileService.saveSMSDetails(sms);

        //Response
        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_MOBILE);
        response.setMessage("Successfully sent the OTP to your Mobile Number :"+mobile);
        return response;
    }

    public TFAResponse resetTFAUserMobile(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }
        TwoFACode code = mobileService.getMobile2FACode();
        user.setMobileverified(false);
        user.setMobileotpvalidtime(code.getValidTime());
        user.setMobileotp(code.getCode());
        userRepository.save(user);

        //Sms OTP
        String loginUserId = userConfig.getLoginUser();
        SmsOutboxTrans sms = new SmsOutboxTrans();
        sms.setTo(user.getMobile());
        //ToDo: Define templates for message body
        sms.setMessage(AppConstants.SMS_USER_AC_ACTIVATION.concat(code.getCode()));
        sms.setMessageType("Resend OTP Verification");
        sms.setCreatedby(loginUserId);
        sms.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        sms.setModifiedby(loginUserId);
        sms.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        mobileService.saveSMSDetails(sms);

        //Response
        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_MOBILE);
        response.setMessage("Mobile OTP reset successfully");
        return response;
    }

    public TFAResponse resetTFAUserEmail(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }
        TwoFACode code = emailService.getEmail2FACode();
        user.setEmailverified(false);
        user.setEmailotpvalidtime(code.getValidTime());
        user.setEmailotp(code.getCode());
        userRepository.save(user);

        String loginUserId = userConfig.getLoginUser();
        EmailOutboxTrans email = new EmailOutboxTrans();
        email.setTo(user.getEmail());//Email or username
        email.setMessageType("Reset Email OTP");
        email.setSubject("Email verification");
        //ToDo: Define templates for message body
        StringBuilder body = new StringBuilder("Dear "+user.getFirstname()+", <br/>");
        body.append("Please click the below link to verify your email with iHCD account.  <br/> <br/>");
        //body.append(IHCD_HOST_URL).append("/api/users/"+user.getUsername()+"/verifyemail/"+code.getCode());
        String link = IHCD_HOST_URL+"/api/users/"+user.getUsername()+"/verifyemail/"+code.getCode();
        String hyperLink = "<br/> <a href=\""+link+"\">Verify email</a>";
        body.append(hyperLink);
        body.append("<br/><br/>Thank you, <br/>");
        body.append("Team iHCD.");
        email.setMessage(body.toString());
        email.setCreatedby(loginUserId);
        email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        email.setModifiedby(loginUserId);
        email.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        emailService.saveEmailDetails(email);

        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_EMAIL);
        response.setMessage("Email OTP reset successfully");
        return response;
    }

    public LogoutResponse signoutUser(LoginViewModel credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+credentials.getUsername());
        } else if(user.isQRAuthEnabled()) {
            user.setQRCodeverified(false);
        }
        user.setSigninToken("");
        //ToDo: Create seperate tx table to track user signin & signout timings
        user.setSignoutTime(DateUtil.getTimeInMilliSeconds());
        userRepository.save(user);
        System.out.println("------->Logout success "+user.getUsername());
        LogoutResponse response = new LogoutResponse("User("+user.getUsername()+") signout successfully", "Ok");
        System.out.println("------->Logout received "+credentials.getUsername());

        return response;
    }

    public GenericResponse resetVerification(String username, ResetVerification request) {

        //NOTE: Beaware while using this call, all non requested fileds will be disabled

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }

        user.setSigninToken("");
        if(request.isMobile()){
            user.setMobileverifyenabled(true);
            user.setMobileverified(false);
            user.setMobileotp("");//ToDo: set & send sms
        } else {
            user.setMobileverifyenabled(false);
        }
        if(request.isEmail()){
            user.setEmailverifyenabled(true);
            user.setEmailverified(false);
            user.setEmailotp("");//ToDo: set & send mail
        } else {
            user.setEmailverifyenabled(false);
        }
        if(request.isQrcode()){
            user.setQRAuthEnabled(true);
            user.setQRCodeverified(false);
            user.setQRSetupdone(false);//ToDo: set & generate QR secret
        } else {
            user.setQRAuthEnabled(false);
        }
        String loginUserId = userConfig.getLoginUser();
        user.setModifiedby(loginUserId);
        user.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        userRepository.save(user);
        GenericResponse response = new GenericResponse("Resetverification setup done successfully!");
        return response;
    }

    public GenericResponse requestEditProfile(String username, ReqEditProfileRequest request) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(request == null || request.getRequestTitle() == null){
            //ToDo: Related validations to be added
            throw new UserValidationException("Please provide valid reason for edit profile");
        }

        TwoFACode code = mobileService.getMobile2FACode();

        user.setEditprofileaverified(false);
        user.setEditprofileon(true);
        user.setEditprofilecode(code.getCode());

        String loginUserId = userConfig.getLoginUser();
        //SMS OTP
        SmsOutboxTrans sms = new SmsOutboxTrans();
        sms.setTo(user.getMobile());
        sms.setMessageType(request.getRequestTitle()+" OTP: "+code.getCode());
        sms.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        sms.setCreatedby(loginUserId);

        //Email OTP
        EmailOutboxTrans email = new EmailOutboxTrans();
        email.setTo(user.getEmail());//Email or username
        email.setMessageType(request.getRequestTitle());
        email.setSubject(request.getRequestTitle());
        //ToDo: Define templates for message body
        StringBuilder body = new StringBuilder("Dear "+user.getFirstname()+", <br/>");
        body.append("<br/>your otp code: "+code.getCode());
        body.append("<br/><br/>Thank you, <br/>");
        body.append("Team iHCD.");
        email.setMessage(body.toString());
        email.setCreatedby(loginUserId);
        email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        emailService.saveEmailDetails(email);
        userRepository.save(user);
        System.out.println(request.getRequestTitle()+" request placed successfully");
        return new GenericResponse(request.getRequestTitle()+" request placed successfully");
    }

    public TFAResponse verifyEditUserProfile(String username, String otp){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(user.getEditprofilecode()== null || !user.getEditprofilecode().equals(otp)){
            throw new UserValidationException("Invalid OTP");
        }
        user.setEditprofileaverified(true);
        user.setEditprofileverifiedtime(DateUtil.getTimeInMilliSeconds());
        userRepository.save(user);
        //Response
        TFAResponse response = new TFAResponse();
        response.setVerifyType(AppConstants.TWOFATYPE_PROFILE);
        response.setMessage("OTP Verified successfully");
        return response;
    }

    public GenericResponse editPassword(String username, EditPasswordRequest request){
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(!user.isEditprofileon()){
            throw new UserValidationException("Change pasword not allowed");
        } else if(user.getEditprofilecode()== null || !user.getEditprofilecode().equals(request.getOtp())){
            throw new UserValidationException("Invalid OTP for change password");
        } if(request.getNewPassword() == null) {
            throw new UserValidationException("Provide new password");
        }
        //This validates user existing credentials
        LoginViewModel credentials = new LoginViewModel();
        credentials.setUsername(username);
        credentials.setPassword(request.getPassword());
        String token = barcodeService.generateToken(credentials);
        if(token == null){
            throw new UserValidationException("Invalid Credentials");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        //Reset editprofile requeston
        user.setEditprofileon(false);
        user.setEditprofileaverified(false);
        user.setEditprofilecode("");
        userRepository.save(user);
        //After password reset logout user
        credentials.setPassword(request.getNewPassword());
        this.signoutUser(credentials);
        return new GenericResponse("Your Password updated successfully");
    }

    public GenericResponse forgotPassword(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }
        TwoFACode code = emailService.getEmail2FACode();
        user.setEditprofileon(true);
        user.setEditprofileaverified(false);
        user.setEditprofilecode(code.getCode());

        String loginUserId = userConfig.getLoginUser();
        EmailOutboxTrans email = new EmailOutboxTrans();
        email.setTo(user.getEmail());//Email or username
        email.setMessageType("Forgot password -Reset password");
        email.setSubject("Forgot password -Reset password");
        //ToDo: Define templates for message body
        StringBuilder body = new StringBuilder("Dear "+user.getFirstname()+", <br/>");
        body.append(StringUtils.SPACE+ "Please click the below link to reset your iHCD account password. <br/><br/>");
        //body.append(IHCD_HOST_URL).append(AppConstants.IHCD_FORGOTPASSWORD+"/"+username+"/"+code.getCode());//GUI URL
        String link = IHCD_HOST_URL+AppConstants.IHCD_FORGOTPASSWORD+"/"+username+"/"+code.getCode();
        String hyperLink = "<br/> <a href=\""+link+"\">Reset password</a>";
        body.append(hyperLink);
        body.append("<br/><br/>Thank you, <br/>");
        body.append("Team iHCD.");
        email.setMessage(body.toString());
        email.setCreatedby(loginUserId);
        email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
        email.setModifiedby(loginUserId);
        email.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        emailService.saveEmailDetails(email);

        userRepository.save(user);
        return new GenericResponse("A password reset request has been sent to the email address you entered.");
    }

    public GenericResponse resetForgotPassword(String username, ForgotpasswordRequest request){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(!user.isEditprofileon()){
            throw new UserValidationException("Forgot password not allowed");
        } else if(!request.getForgototp().equals(user.getEditprofilecode())){
            throw new UserValidationException("Forgot password reset request expired");
        } else if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new UserValidationException("Password and Confirm password not same");
        }
        //Reset profile edit
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEditprofileon(false);
        user.setEditprofileaverified(false);
        user.setEditprofilecode("");
        user.setEditprofileverifiedtime(DateUtil.getTimeInMilliSeconds());

        userRepository.save(user);
        return new GenericResponse("Password reset successful.");
    }

    public GenericResponse editPersonalDetails(String username, EditPersonalDetails request){
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        } else if(!user.isEditprofileon()){
            throw new UserValidationException("Edit personal details not allowed");
        } else if(user.getEditprofilecode()== null || !user.getEditprofilecode().equals(request.getOtp())){
            throw new UserValidationException("Invalid OTP for edit personal details");
        }

        String loginUserId = userConfig.getLoginUser();
        //Update email
        user.setEmail(request.getUsername());
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setCompany(request.getCompany());
        boolean isEmailVerifyEnabled = AppConstants.IS_EMAIL_VERIFY_DEFAULT_ENABLE;
        //Email OTP
        if(isEmailVerifyEnabled){
            TwoFACode email2FACode = emailService.getEmail2FACode();
            user.setEmailotpvalidtime(email2FACode.getValidTime());
            user.setEmailotp(email2FACode.getCode());
            user.setEmailverified(false);
            EmailOutboxTrans email = new EmailOutboxTrans();
            System.out.println("---Change email sending to: "+user.getEmail());
            email.setTo(user.getEmail());//Email or username
            email.setMessageType("Update profile details verification");
            email.setSubject("Update profile details verification");
            email.setEmailsent(false);
            //ToDo: Define templates for message body
            StringBuilder body = new StringBuilder("Dear "+user.getFirstname()+", <br/>");
            body.append(StringUtils.SPACE+ "Please click the below link to apply your profile details with iHCD account. <br/>");
            //body.append(IHCD_HOST_URL).append("/activate/"+user.getEmail()+"/"+email2FACode.getCode());
            String link = IHCD_HOST_URL+"/auth/activate/"+user.getEmail()+"/"+email2FACode.getCode();
            String hyperLink = "<br/> <a href=\""+link+"\">Profile update email verification</a>";
            body.append(hyperLink);
            body.append("<br/><br/>Thank you, <br/>");
            body.append("Team iHCD.");
            email.setMessage(body.toString());
            email.setCreatedby(loginUserId);
            email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
            email.setModifiedby(loginUserId);
            email.setModifieddtm(DateUtil.getTimeInMilliSeconds());
            emailService.saveEmailDetails(email);
        }

        user.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        //Reset editprofile requeston
        user.setEditprofileon(false);
        user.setEditprofileaverified(false);
        user.setEditprofilecode("");
        user.setEditprofileverifiedtime(DateUtil.getTimeInMilliSeconds());
        user.setModifieddtm(DateUtil.getTimeInMilliSeconds());
        user.setModifiedby(loginUserId);
        userRepository.save(user);
        //After password reset logout user
        LoginViewModel credentials = new LoginViewModel();
        credentials.setUsername(user.getUsername());
        this.signoutUser(credentials);
        return new GenericResponse("Email updated successfully, please complete email verification");
    }

    private String hideMobile(String mobile) {
      String hiddenMobile = "";
      //ToDO: hide middle digits with xxx
        hiddenMobile = mobile;
      return hiddenMobile;
    }

    private String hideEmail(String email) {
        String hiddenEmail = "";
        //ToDO: hide middle chars in email with xxx
        hiddenEmail = email;
        return hiddenEmail;
    }
    
    public void activationMail(User user){
    	 TwoFACode email2FACode = emailService.getEmail2FACode();
    	 user.setEmailotpvalidtime(email2FACode.getValidTime());
    	 user.setEmailotp(email2FACode.getCode());
         EmailOutboxTrans email = new EmailOutboxTrans();
         email.setTo(user.getEmail());//Email or username
         email.setMessageType("Email verification");
         email.setSubject("Email verification");
         //ToDo: Define templates for message body
         StringBuilder body = new StringBuilder("Dear "+user.getFirstname()+", <br/>");
         body.append("Please click the below link to activate your iHCD account.  <br/>");
         //body.append(IHCD_HOST_URL).append("/auth/activate/"+request.getEmail()+"/"+email2FACode.getCode());
         String link = IHCD_HOST_URL+"/auth/activate/"+user.getEmail()+"/"+email2FACode.getCode();
         String hyperLink = "<a href=\""+link+"\">Activate account</a>";
         body.append(hyperLink);
         body.append("<br/><br/>Thank you, <br/>");
         body.append("Team iHCD.");
         email.setMessage(body.toString());
         email.setCreatedby(user.getUsername());
         email.setCreateddtm(DateUtil.getTimeInMilliSeconds());
         email.setModifiedby(user.getUsername());
         email.setModifieddtm(DateUtil.getTimeInMilliSeconds());
         emailService.saveEmailDetails(email);
    }

}
