package com.arch.ihcd.gateway.service.mobile;

import com.arch.ihcd.gateway.bean.TwoFACode;
import com.arch.ihcd.gateway.entity.SmsOutboxTrans;
import com.arch.ihcd.gateway.repository.SMSRepository;
import com.arch.ihcd.gateway.util.AppConstants;
import com.arch.ihcd.gateway.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MobileServiceImpl implements MobileService {

    @Autowired
    private SMSRepository smsRepository;

    @Override
    public TwoFACode getMobile2FACode(){
        String twoFACode = String.valueOf(new Random().nextInt(9999)+1000);
        return new TwoFACode(AppConstants.TWOFATYPE_MOBILE, twoFACode, DateUtil.getTimeInMilliSeconds()+AppConstants.MOBILE_OTP_VALID_TIME_IN_MILLISECONDS);
    }

    @Override
    public SmsOutboxTrans saveSMSDetails(SmsOutboxTrans sms){
        return smsRepository.save(sms);
    }

}
