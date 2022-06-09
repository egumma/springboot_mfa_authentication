package com.arch.ihcd.gateway.entity;

import com.arch.ihcd.gateway.bean.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Document(collection= TableName.IHCD_USERS)
public class User {

    @Id
    private String id;

    @NotEmpty(message = "*Please provide username")
    private String username;

    @NotEmpty(message = "*Please provide password")
    private String password;

    @Email(message = "*Please provide a valid email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    private String firstname;
    private String lastname;
    private String mobile;
    private String signuptype ="ihcd";
    private String company;
    private ExperienceInfo expInfo;

    private int active = 1;

    private long signinTime;
    private long signoutTime;
    private String signinToken;

    //Roles & Privileges
    private Set<Role> roles;
    private Set<Privilege> privileges;
    private Set<ResourceServerAccessData> resourceServerTokens;
    private List<Address> addresses;

    //RQ Code fields
    private boolean isQRAuthEnabled = false;
    private boolean isQRSetupdone = false;
    private boolean isQRCodeverified = false;
    private String qrSecretcode = "";

    //OTP verification fields
    //mobile
    private boolean mobileverifyenabled = false;
    private boolean mobileverified = false;
    private String mobileotp = "";
    private long mobileotpvalidtime;
    private long mobileverifiedtime;
    //email
    private boolean emailverifyenabled = false;
    private boolean emailverified = false;
    private String emailotp = "";
    private long emailotpvalidtime;
    private long emailverifiedtime;

    //Edit profile - editpassword, changemobile, change email
    private boolean editprofileon = false;
    private boolean editprofileaverified = false;
    private String editprofilecode;
    private long editprofileverifiedtime;

    //Record fields
    private String createdby;
    private long createddtm;
    private String modifiedby;
    private long modifieddtm;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Set<ResourceServerAccessData> getResourceServerTokens() {
        return resourceServerTokens;
    }

    public void setResourceServerTokens(Set<ResourceServerAccessData> resourceServerTokens) {
        this.resourceServerTokens = resourceServerTokens;
    }

    public String getSignuptype() {
        return signuptype;
    }

    public void setSignuptype(String signuptype) {
        this.signuptype = signuptype;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public long getCreateddtm() {
        return createddtm;
    }

    public void setCreateddtm(long createddtm) {
        this.createddtm = createddtm;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public long getModifieddtm() {
        return modifieddtm;
    }

    public void setModifieddtm(long modifieddtm) {
        this.modifieddtm = modifieddtm;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public boolean isQRAuthEnabled() {
        return isQRAuthEnabled;
    }

    public void setQRAuthEnabled(boolean QRAuthEnabled) {
        isQRAuthEnabled = QRAuthEnabled;
    }

    public boolean isQRSetupdone() {
        return isQRSetupdone;
    }

    public void setQRSetupdone(boolean QRSetupdone) {
        isQRSetupdone = QRSetupdone;
    }

    public boolean isQRCodeverified() {
        return isQRCodeverified;
    }

    public void setQRCodeverified(boolean QRCodeverified) {
        isQRCodeverified = QRCodeverified;
    }

    public String getQrSecretcode() {
        return qrSecretcode;
    }

    public void setQrSecretcode(String qrSecretcode) {
        this.qrSecretcode = qrSecretcode;
    }

    public long getSigninTime() {
        return signinTime;
    }

    public void setSigninTime(long signinTime) {
        this.signinTime = signinTime;
    }

    public long getSignoutTime() {
        return signoutTime;
    }

    public void setSignoutTime(long signoutTime) {
        this.signoutTime = signoutTime;
    }

    public String getSigninToken() {
        return signinToken;
    }

    public void setSigninToken(String signinToken) {
        this.signinToken = signinToken;
    }

    public boolean isMobileverifyenabled() {
        return mobileverifyenabled;
    }

    public void setMobileverifyenabled(boolean mobileverifyenabled) {
        this.mobileverifyenabled = mobileverifyenabled;
    }

    public boolean isMobileverified() {
        return mobileverified;
    }

    public void setMobileverified(boolean mobileverified) {
        this.mobileverified = mobileverified;
    }

    public String getMobileotp() {
        return mobileotp;
    }

    public void setMobileotp(String mobileotp) {
        this.mobileotp = mobileotp;
    }

    public long getMobileotpvalidtime() {
        return mobileotpvalidtime;
    }

    public void setMobileotpvalidtime(long mobileotpvalidtime) {
        this.mobileotpvalidtime = mobileotpvalidtime;
    }

    public long getMobileverifiedtime() {
        return mobileverifiedtime;
    }

    public void setMobileverifiedtime(long mobileverifiedtime) {
        this.mobileverifiedtime = mobileverifiedtime;
    }

    public boolean isEmailverifyenabled() {
        return emailverifyenabled;
    }

    public void setEmailverifyenabled(boolean emailverifyenabled) {
        this.emailverifyenabled = emailverifyenabled;
    }

    public boolean isEmailverified() {
        return emailverified;
    }

    public void setEmailverified(boolean emailverified) {
        this.emailverified = emailverified;
    }

    public String getEmailotp() {
        return emailotp;
    }

    public void setEmailotp(String emailotp) {
        this.emailotp = emailotp;
    }

    public long getEmailotpvalidtime() {
        return emailotpvalidtime;
    }

    public void setEmailotpvalidtime(long emailotpvalidtime) {
        this.emailotpvalidtime = emailotpvalidtime;
    }

    public long getEmailverifiedtime() {
        return emailverifiedtime;
    }

    public void setEmailverifiedtime(long emailverifiedtime) {
        this.emailverifiedtime = emailverifiedtime;
    }

    public boolean isEditprofileon() {
        return editprofileon;
    }

    public void setEditprofileon(boolean editprofileon) {
        this.editprofileon = editprofileon;
    }

    public boolean isEditprofileaverified() {
        return editprofileaverified;
    }

    public void setEditprofileaverified(boolean editprofileaverified) {
        this.editprofileaverified = editprofileaverified;
    }

    public String getEditprofilecode() {
        return editprofilecode;
    }

    public void setEditprofilecode(String editprofilecode) {
        this.editprofilecode = editprofilecode;
    }

    public long getEditprofileverifiedtime() {
        return editprofileverifiedtime;
    }

    public void setEditprofileverifiedtime(long editprofileverifiedtime) {
        this.editprofileverifiedtime = editprofileverifiedtime;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ExperienceInfo getExpInfo() {
        return expInfo;
    }

    public void setExpInfo(ExperienceInfo expInfo) {
        this.expInfo = expInfo;
    }
}
