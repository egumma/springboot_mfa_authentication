package com.arch.ihcd.gateway.request;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MethodUserRoleAssignRequest extends UserCreationRequest{	
	
	@NotEmpty(message="Instance id is required")
    private String instId;   
    @NotEmpty(message="Project id is required")
    private String projId;   
    @NotEmpty(message="Stage Type id is required")
    private String stageTypeId;
    @NotEmpty(message="Method Type id is required")
    private String methodTypeId;
    @NotEmpty(message="Method id is required")
    private String methodId;
    @NotEmpty(message="User id is required")
    private String userId;   
    @NotEmpty(message="Method role is required")
    private String methodRole;
}
