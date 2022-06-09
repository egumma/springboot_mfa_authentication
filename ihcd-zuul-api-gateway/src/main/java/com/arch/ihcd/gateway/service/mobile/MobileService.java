package com.arch.ihcd.gateway.service.mobile;

import com.arch.ihcd.gateway.bean.TwoFACode;
import com.arch.ihcd.gateway.entity.SmsOutboxTrans;

public interface MobileService {

    public TwoFACode getMobile2FACode();

    public SmsOutboxTrans saveSMSDetails(SmsOutboxTrans sms);
}
