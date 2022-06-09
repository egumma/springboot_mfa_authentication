package com.arch.ihcd.gateway.repository;


import com.arch.ihcd.gateway.entity.SmsOutboxTrans;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMSRepository extends MongoRepository<SmsOutboxTrans, String> {
}
