package com.arch.ihcd.gateway.service.instance;

import com.arch.ihcd.gateway.request.InstanceUserCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arch.ihcd.gateway.config.UserConfig;
import com.arch.ihcd.gateway.controller.helper.UserSignInHelper;
import com.arch.ihcd.gateway.entity.InstanceUserRolesXRef;
import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.exception.GenericException;
import com.arch.ihcd.gateway.repository.InstanceUserRoleXRefRepository;
import com.arch.ihcd.gateway.repository.UserRepository;
import com.arch.ihcd.gateway.request.ProjectUserRoleAssignRequest;
import com.arch.ihcd.gateway.service.proxy.UserInviteServiceProxy;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.RoleConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectInviteUserService implements IProjectInviteUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserConfig userConfig;
	@Autowired
	private UserSignInHelper userSignInHelper;
	@Autowired
	private InstanceService instanceService;
	@Autowired
    private InstanceUserRoleXRefRepository instanceUserRoleXRefRepository;
	@Autowired
	private UserInviteServiceProxy userInviteServiceProxy;

	@Override
	public User inviteProjectUserRole(ProjectUserRoleAssignRequest request, String loginUserId) {
		log.info(">>>>ProjectInviteUserService.inviteProjectUserRole<<<<START");
		/*Integer active = 1;
		// Verify is user already exist
		User user = userRepository.findByUsername(request.getEmail());		
		 String loginId = userConfig.getLoginUser();
		if (user != null) { // Make user active
			user.setActive(active);
			user.setModifiedby(loginId);
			user.setModifieddtm(System.currentTimeMillis());
			userRepository.save(user);
			log.info("---> User now active: ", user.getUsername());
		} else {// Register new user
			if (StringUtils.isEmpty(request.getUsername())) {
				request.setUsername(request.getEmail());
			}
			if (StringUtils.isEmpty(request.getFirstname())) {
				request.setFirstname(request.getEmail());
			}
			if (StringUtils.isEmpty(request.getPassword())) {
				request.setPassword("123");// ToDo: generate somepassword
			}
			user = userSignInHelper.registerUser(request, RoleConstants.ROLE_IHCD_USER);
			log.info("---> User created: ", user.getUsername());
		}
		 //Assign User with given role to the given instanace
        InstanceUserRolesXRef xref = assignUserToInstanceRole(request.getInstId(), user.getId(), RoleConstants.ROLE_INST_USER, loginId);
        log.info("---> User("+user.getUsername()+") assigned as "+xref.getRoleId()+" to the instance("+request.getInstId()+")");*/
		User user = userRepository.findByUsername(request.getEmail());
		if(user == null || instanceUserRoleXRefRepository.findByInstIdAndUserIdAndStatus(request.getInstId(), user.getId(), true) == null){
			InstanceUserCreationRequest instRequest = new InstanceUserCreationRequest();
			instRequest.setInstId(request.getInstId());
			instRequest.setInstRole(RoleConstants.ROLE_INST_USER);
			instRequest.setEmail(request.getEmail());
			instanceService.registerInstanceUser(instRequest);
			user = userRepository.findByUsername(request.getEmail());
			log.info("<<ProjectInviteUserService>>:<<inviteProjectUserRole>> Created/Updated user and associated to instance role");
		}
		request.setUserId(user.getId());
		log.info(">>>>ProjectInviteUserService.inviteProjectUserRole<<<<calling master data service");
		userInviteServiceProxy.assignProjectUserRole(request, loginUserId);
		log.info(">>>>ProjectInviteUserService.inviteProjectUserRole<<<< Proxy call completed");
		return user;
	}
	
	  /*private InstanceUserRolesXRef assignUserToInstanceRole(String instId, String userId, String role, String loginId){
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
	  }*/

}
