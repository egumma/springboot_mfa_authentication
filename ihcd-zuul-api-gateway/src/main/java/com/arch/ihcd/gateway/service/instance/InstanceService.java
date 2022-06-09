package com.arch.ihcd.gateway.service.instance;

import com.arch.ihcd.gateway.config.UserConfig;
import com.arch.ihcd.gateway.controller.helper.UserSignInHelper;
import com.arch.ihcd.gateway.entity.ConsentForm;
import com.arch.ihcd.gateway.entity.InstanceUserRolesXRef;
import com.arch.ihcd.gateway.entity.InstancesRef;
import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.exception.GenericException;
import com.arch.ihcd.gateway.repository.ConsentFormRepository;
import com.arch.ihcd.gateway.repository.InstanceRepository;
import com.arch.ihcd.gateway.repository.InstanceUserRoleXRefRepository;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.arch.ihcd.gateway.request.InstanceCreationRequest;
import com.arch.ihcd.gateway.request.InstanceUserCreationRequest;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.RoleConstants;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class InstanceService implements IInstanceService {

    @Value("${ihcd.frontend.home}")
    private String IHCD_HOST_URL;

    @Autowired
    private InstanceRepository instanceRepository;
    @Autowired
    private InstanceUserRoleXRefRepository instanceUserRoleXRefRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSignInHelper userSignInHelper;

    @Autowired
    private ConsentFormRepository consentFormRepository;

    @Autowired
    private UserConfig userConfig;

    @Override
    public String registerInstance(InstanceCreationRequest request){

        //Verify user existence
        User user = userRepository.findByUsername(request.getEmail());
        if(user != null) {
            throw new GenericException(AppConstants.ERROR_USER_ALREADY_EXIST+" - "+user.getUsername());
        }
        String loginId = userConfig.getLoginUser();//Systems always here

        //Register instance
        InstancesRef instance = createInstance(request.getEmail(), loginId);
        log.info("\n---> Instance created: ", instance.getInstName());

        //Register user as admin for the above created instance
        User adminUser = userSignInHelper.registerUser(request, RoleConstants.ROLE_IHCD_USER);
        log.info("---> User created: ", adminUser.getUsername());

        //Assign User as admin to above created instance
        InstanceUserRolesXRef xref = assignUserToInstanceRole(instance.getInstId(), adminUser.getId(), RoleConstants.ROLE_INST_ADMIN, loginId);
        log.info("---> User("+adminUser.getUsername()+") assigned as "+xref.getRoleId()+" to the instance("+instance.getInstName()+")");

        //Create default consent form for Instance
        ConsentForm cform = new ConsentForm();
        cform.setConsentId("1");
        cform.setInstId(instance.getInstId());
        cform.setCfName("Default Consent");
        //Default consent body
        StringBuilder cformbody = new StringBuilder("<br/>Thank you for your willingness to use iHCD to gather requirements for this project. <br/>");
        cformbody.append("When we write reports or presentations on what we learn from the interviews, " +
                "we sometimes use specific quotes from study participants. " +
                "Quotes bring to life what we learn and are an important part of sharing your experience with others. " +
                "If you give us permission to use your quotes, " +
                "we will not include your name or a photograph of your face next to the quote. " +
                "This protects your identity and makes the quote anonymous. " +
                "If you approve of your quotes being used in future publications or presentations of our work, " +
                "please include your name and signature in the section below.");

        cform.setCfDesc(cformbody.toString());
        cform.setImageLocation("/media/consent/default-consent.png");
        cform.setCreatedby(adminUser.getId());
        cform.setCreateddtm(System.currentTimeMillis());
        cform.setModifiedby(adminUser.getId());
        cform.setModifieddtm(System.currentTimeMillis());
        consentFormRepository.save(cform);
        log.info("---> Instance("+instance.getInstId()+") default consent form created");

        return "User & instance registered successfully";
    }

    @Override
    public String registerInstanceUser(InstanceUserCreationRequest request){
    	 Integer active =  1;
         //Verify is user already exist
         User user = userRepository.findByUsername(request.getEmail());
//         if(user != null && active.equals(user.getActive())) {
//             throw new GenericException(AppConstants.ERROR_USER_ALREADY_EXIST+" - "+user.getUsername());
//         }
         InstancesRef exist = instanceRepository.findByInstId(request.getInstId());
         if(exist == null) {
             throw new GenericException("Instance id not found - "+request.getInstId());
         }
         String loginId = userConfig.getLoginUser();


         if(user != null) { // Make user active
             user.setActive(active);
             user.setModifiedby(loginId);
             user.setModifieddtm(System.currentTimeMillis());
             userRepository.save(user);
             log.info("---> User now active: ", user.getUsername());
             userSignInHelper.activationMail(user);
         } else {//Register new user
             if(StringUtils.isEmpty(request.getUsername())){
                 request.setUsername(request.getEmail());
             }
             if(StringUtils.isEmpty(request.getFirstname())){
                 request.setFirstname(request.getEmail());
             }
             if(StringUtils.isEmpty(request.getPassword())){
                 request.setPassword("ihcd$123");//ToDo: generate somepassword
             }
             user = userSignInHelper.registerUser(request, RoleConstants.ROLE_IHCD_USER);
             log.info("---> User created: ", user.getUsername());
         }

         //Assign User with given role to the given instanace
         InstanceUserRolesXRef xref = assignUserToInstanceRole(request.getInstId(), user.getId(), request.getInstRole(), loginId);
         log.info("---> User("+user.getUsername()+") assigned as "+xref.getRoleId()+" to the instance("+request.getInstId()+")");
         return "Success";
    }

    private InstancesRef createInstance(String email, String loginId){
        String instId = ""+System.currentTimeMillis();//ToDo: find a way to get unique id
        InstancesRef exist = instanceRepository.findByInstId(instId);
        if(exist != null) {
            throw new GenericException("Instance already exist - "+exist.getInstId());
        }
        InstancesRef insRequest = new InstancesRef();
        insRequest.setInstId(instId);
        insRequest.setInstName(email);
        insRequest.setInstDesc(email);
        insRequest.setCreatedby(loginId);
        insRequest.setCreateddtm(System.currentTimeMillis());
        insRequest.setModifiedby(loginId);
        insRequest.setModifieddtm(System.currentTimeMillis());
        return instanceRepository.save(insRequest);
    }

    private InstanceUserRolesXRef assignUserToInstanceRole(String instId, String userId, String role, String loginId){
        InstanceUserRolesXRef xref = instanceUserRoleXRefRepository.findByInstIdAndUserId(instId, userId);
        if(xref == null ){
            xref = new InstanceUserRolesXRef();
            xref.setInstId(instId);
            xref.setUserId(userId);
            xref.setCreatedby(loginId);
            xref.setCreateddtm(System.currentTimeMillis());
        }
        xref.setStatus(true);
        xref.setRoleId(role);
        xref.setModifiedby(loginId);
        xref.setModifieddtm(System.currentTimeMillis());
        return instanceUserRoleXRefRepository.save(xref);
    }
}
