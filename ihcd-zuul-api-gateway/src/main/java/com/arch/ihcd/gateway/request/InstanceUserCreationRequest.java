package com.arch.ihcd.gateway.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InstanceUserCreationRequest extends UserCreationRequest {
    @NotEmpty(message="Instance Role is required")
    private String instRole;
    @NotEmpty(message="Instance Id is required")
    private String instId;
    @NotEmpty(message="UserId Id is required")
    private String userId;
    
}
