package com.arch.ihcd.gateway.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserRoleAssignRequest extends UserCreationRequest{

    @NotEmpty(message="Instance id is required")
    private String instId;    
    @NotEmpty(message="User id is required")
    private String userId;
    @NotEmpty(message="Project id is required")
    private String projId;
    @NotEmpty(message="Project role is required")
    private String projRole;
   
}
