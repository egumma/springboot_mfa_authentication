package com.arch.ihcd.gateway.service.instance;

import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.request.ProjectUserRoleAssignRequest;

public interface IProjectInviteUserService {

	User inviteProjectUserRole(ProjectUserRoleAssignRequest request, String loginUserId);
	
}
