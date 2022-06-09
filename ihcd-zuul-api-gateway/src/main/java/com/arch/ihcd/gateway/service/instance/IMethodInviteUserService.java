package com.arch.ihcd.gateway.service.instance;

import com.arch.ihcd.gateway.entity.User;
import com.arch.ihcd.gateway.request.MethodUserRoleAssignRequest;

public interface IMethodInviteUserService {

	User inviteMethodUserRole(MethodUserRoleAssignRequest request, String loginUserId);

}
