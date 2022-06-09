package com.arch.ihcd.gateway.service.instance;

import com.arch.ihcd.gateway.request.InstanceCreationRequest;
import com.arch.ihcd.gateway.request.InstanceUserCreationRequest;

public interface IInstanceService {
    String registerInstance(InstanceCreationRequest request);
    String registerInstanceUser(InstanceUserCreationRequest request);
}
