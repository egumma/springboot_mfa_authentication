package com.arch.ihcd.gateway.controller;

import com.arch.ihcd.gateway.bean.ExperienceInfo;
import com.arch.ihcd.gateway.bean.ResourceServerAccessData;
import com.arch.ihcd.gateway.config.*;
import com.arch.ihcd.gateway.controller.helper.UserSignInHelper;
import com.arch.ihcd.gateway.request.*;
import com.arch.ihcd.gateway.exception.GenericException;
import com.arch.ihcd.gateway.exception.OAuthClientGetAccessTokenException;
import com.arch.ihcd.gateway.exception.UserNotFoundException;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.response.*;
//import com.arch.ihcd.gateway.service.oauth.*;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.DateUtil;
import com.arch.ihcd.gateway.util.RoleConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@Slf4j
@Api(tags = {"API Login Controller"})
@SwaggerDefinition(tags = {
        @Tag(name = "Login Controller", description = "User details")
})
@CrossOrigin
@RestController
@RequestMapping("api/users")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    /*@Autowired
    private ZeplinOauthClientAccessTokenServiceImpl zeplinOauthClientAccessTokenService;
    @Autowired
    private SMonkeyOauthClientAccessTokenServiceImpl sMonkeyOauthClientAccessTokenService;
    @Autowired
    private MiroOAuthAccessTokenService miroOAuthAccessTokenService;
    @Autowired
    private JiraOauthClientAccessTokenServiceImpl jiraOauthClientAccessTokenService;
    @Autowired
    private ConfluenceOauthClientAccessTokenServiceImpl confluenceOauthClientAccessTokenService;
    @Autowired
    private ZoomOauthClientAccessTokenServiceImpl zoomOauthClientAccessTokenService;*/
    @Autowired
    private UserSignInHelper userSignInHelper;
//    @Autowired
//    private GoogleOAuthAccessTokenService googleOAuthAccessTokenService;

    public LoginController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Available to all authenticated users
    @GetMapping("test")
    public String test1(){
        return "API Test";
    }
    // Available to all authenticated users
    @GetMapping("status")
    public String status(){
        return "It Works";
    }

    // Available to managers
    @GetMapping("management/reports")
    public String reports(){
        return "Some report data";
    }

    // Available to ROLE_ADMIN
    @GetMapping("admin/users")
    public List<User> users(){
        return this.userRepository.findAll();
    }

    @ApiOperation(value = "New User Registration", response = ResponseEntity.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody UserCreationRequest request) {

        User savedUser  = userSignInHelper.registerUser(request, RoleConstants.ROLE_IHCD_USER);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(savedUser.getUsername())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Save User Experience details", response = ResponseEntity.class)
    @PostMapping("/{userId}/experience")
    public ResponseEntity<Object> saveUserExperience(@PathVariable String userId, @Valid @RequestBody ExperienceInfo request) {
        return new ResponseEntity<>(new GenericResponse(userSignInHelper.saveUserExperience(userId, request)), HttpStatus.OK);
    }

    @ApiOperation(value = "View User Experience details", response = ResponseEntity.class)
    @GetMapping("/{userId}/experience")
    public ResponseEntity<ExperienceInfo> getUserExperience(@PathVariable String userId) {
        return new ResponseEntity<ExperienceInfo>(userSignInHelper.getUserExperience(userId), HttpStatus.OK);
    }

    @PostMapping("/presignin")
    public ResponseEntity<UserPreSignInResponse> userPreSignin(@RequestBody LoginViewModel credentials) {
        System.out.println("------->Presignin received");
        return new ResponseEntity<UserPreSignInResponse>(userSignInHelper.userPreSignInCheck(credentials), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserSignInResponse> signInUser(@RequestBody LoginViewModel credentials) {
        return new ResponseEntity<UserSignInResponse>(userSignInHelper.signInUser(credentials), HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<LogoutResponse> signoutUser(@RequestBody LoginViewModel credentials) {
        return new ResponseEntity<LogoutResponse>(userSignInHelper.signoutUser(credentials), HttpStatus.OK);
    }

    @GetMapping("/{username}/verifyemail/{otp}")
    public ResponseEntity<TFAResponse> verifyUserEmail(@PathVariable String username, @PathVariable String otp) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.verifyTFAUserEmail(username, otp), HttpStatus.OK);
    }

    @PutMapping("/{username}/resetemailotp")
    public ResponseEntity<TFAResponse> resetEmail2FACode(@PathVariable String username) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.resetTFAUserEmail(username), HttpStatus.OK);
    }

    @PutMapping("/{username}/mobile/{mobilenum}")
    public ResponseEntity<TFAResponse> changeMobile(@PathVariable String username, @PathVariable String mobilenum) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.changeUserMobile(username, mobilenum), HttpStatus.OK);
    }

    @PutMapping("/verifymobile")
    public ResponseEntity<TFAResponse> verifyUserMobile(@RequestBody LoginViewModel credentials) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.verifyTFAUserMobile(credentials), HttpStatus.OK);
    }

    @PutMapping("/{username}/resetmobileotp")
    public ResponseEntity<TFAResponse> resetMobile2FACode(@PathVariable String username) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.resetTFAUserMobile(username), HttpStatus.OK);
    }

    @PutMapping("/{username}/resetverification")
    public ResponseEntity<GenericResponse> resetVerification(@PathVariable String username, @RequestBody ResetVerification request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.resetVerification(username, request), HttpStatus.OK);
    }

    @GetMapping("{username}/info")
    public ResponseEntity<User> getUserDetails(@PathVariable String username) {
        return new ResponseEntity<User>(userSignInHelper.getUserDetails(username), HttpStatus.OK);
    }

    /**
     * This action for edit profile/changemobile/changeemail/changepassword
     * @param username
     * @return
     */
    @PutMapping("/{username}/reqeditprofile")
    public ResponseEntity<GenericResponse> requestEditProfile(@PathVariable String username, @RequestBody ReqEditProfileRequest request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.requestEditProfile(username, request), HttpStatus.OK);
    }

    @PutMapping("/{username}/verifyeditprofile/{otp}")
    public ResponseEntity<TFAResponse> verifyEditProfile(@PathVariable String username, @PathVariable String otp) {
        return new ResponseEntity<TFAResponse>(userSignInHelper.verifyEditUserProfile(username, otp), HttpStatus.OK);
    }

    @PutMapping("/{username}/editpassword")
    public ResponseEntity<GenericResponse> editPassword(@PathVariable String username, @RequestBody EditPasswordRequest request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.editPassword(username, request), HttpStatus.OK);
    }

    @PutMapping("/{username}/editpersonaldetails")
    public ResponseEntity<GenericResponse> editPersonalDetails(@PathVariable String username, @RequestBody EditPersonalDetails request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.editPersonalDetails(username, request), HttpStatus.OK);
    }

    @PutMapping("/{username}/forgotpassword")
    public ResponseEntity<GenericResponse> forgotPassword(@PathVariable String username, @RequestBody LoginViewModel request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.forgotPassword(username), HttpStatus.OK);
    }

    @PutMapping("/{username}/reseforgotpassword")
    public ResponseEntity<GenericResponse> resetForgotPassword(@PathVariable String username, @RequestBody ForgotpasswordRequest request) {
        return new ResponseEntity<GenericResponse>(userSignInHelper.resetForgotPassword(username, request), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUserDetails() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @GetMapping("/specific")
    public ResponseEntity<User> getDetails() {
        User user = userRepository.findById("5f2c05be69cc217946023513").get();
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/{userid}/tokens")
    public ResponseEntity<OAuthClientAccessTokenResponse> createOAuthAccessToken(@PathVariable String userid, @RequestBody OAuthClientAccessTokenRequest requestToken) {
        OAuthClientAccessTokenResponse response = null;
        return new ResponseEntity<OAuthClientAccessTokenResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/rserver/callback")
    public ResponseEntity<Object> serverAuthTokenCallback() {
        System.out.println("------> Resource server callback called");
        return new ResponseEntity<Object>("iHCD", HttpStatus.OK);
    }

    @GetMapping("/{userid}/tokens/{servername}")
    public ResponseEntity<ResourceServerAccessData> getOAuthAccessToken(@PathVariable String userid, @PathVariable String servername) {
        ResourceServerAccessData tokenInfo = null;
        return new ResponseEntity<ResourceServerAccessData>(tokenInfo, HttpStatus.OK);
    }

    @GetMapping("/{username}/tokens")
    public ResponseEntity<Set<ResourceServerAccessData>> getOAuthAccessToken(@PathVariable String username) {
        System.out.println("\n----All Tokens for user("+username+")");
        Optional<User> optuser = userRepository.findById(username);
        if(!optuser.isPresent()) {
            throw new UserNotFoundException(AppConstants.ERROR_USER_NOT_FOUND+" - "+username);
        }
        User user = optuser.get();
        Set<ResourceServerAccessData> tokens = !CollectionUtils.isEmpty(user.getResourceServerTokens()) ? user.getResourceServerTokens() : new HashSet(1);
        return new ResponseEntity<Set<ResourceServerAccessData>>(tokens, HttpStatus.OK);
    }
}
