package com.arch.ihcd.gateway.repository;


import com.arch.ihcd.gateway.entity.EmailOutboxTrans;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<EmailOutboxTrans, String> {
}
