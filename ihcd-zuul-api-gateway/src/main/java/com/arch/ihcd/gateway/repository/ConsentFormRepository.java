package com.arch.ihcd.gateway.repository;

import com.arch.ihcd.gateway.entity.ConsentForm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsentFormRepository extends MongoRepository<ConsentForm, String> {
}
