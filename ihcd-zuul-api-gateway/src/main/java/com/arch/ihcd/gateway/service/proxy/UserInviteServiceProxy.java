package com.arch.ihcd.gateway.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.arch.ihcd.gateway.request.MethodUserRoleAssignRequest;
import com.arch.ihcd.gateway.request.ProjectUserRoleAssignRequest;



@FeignClient(name = "ihcd-master-data", url = "${proxy.ihcd-master-data.url}")
public interface UserInviteServiceProxy {

	@PostMapping(value= "/api/project-permission/projects/assignRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> assignProjectUserRole(@RequestBody ProjectUserRoleAssignRequest request, @RequestHeader(name = "login-userid") String loginUserId); 
	
	@PostMapping(value = "/api/method-permission/methods/assignRole", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> assignMethodUserRole(@RequestBody MethodUserRoleAssignRequest request, @RequestHeader(name = "login-userid") String loginUserId);
}
