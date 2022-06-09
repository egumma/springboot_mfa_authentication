package com.arch.ihcd.gateway.repository;

import com.arch.ihcd.gateway.entity.InstancesRef;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstanceRepository extends MongoRepository<InstancesRef, String> {
    InstancesRef findByInstId(String instId);
    InstancesRef findByInstName(String instName);
}
