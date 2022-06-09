package com.arch.ihcd.gateway.repository;

import com.arch.ihcd.gateway.entity.InstanceUserRolesXRef;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceUserRoleXRefRepository extends MongoRepository<InstanceUserRolesXRef, String> {
    InstanceUserRolesXRef findByInstIdAndUserId(String instId, String userId);
    InstanceUserRolesXRef findByInstIdAndUserIdAndStatus(String instId, String userId, boolean status);
    InstanceUserRolesXRef findByInstId(String instId);
    InstanceUserRolesXRef findByUserId(String userId);
}
