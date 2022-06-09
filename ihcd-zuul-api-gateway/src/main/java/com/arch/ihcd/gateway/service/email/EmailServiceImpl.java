package com.arch.ihcd.gateway.service.email;

import com.arch.ihcd.gateway.bean.TwoFACode;
import com.arch.ihcd.gateway.entity.EmailOutboxTrans;
import com.arch.ihcd.gateway.repository.EmailRepository;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Override
    public TwoFACode getEmail2FACode(){
        String twoFACode = String.valueOf(new Random().nextInt(999999)+100000);
        return new TwoFACode(AppConstants.TWOFATYPE_EMAIL, twoFACode, DateUtil.getTimeInMilliSeconds()+AppConstants.EMAIL_OTP_VALID_TIME_IN_MILLISECONDS);
    }

    @Override
    public EmailOutboxTrans saveEmailDetails(EmailOutboxTrans email) {
        return emailRepository.save(email);
    }
}
