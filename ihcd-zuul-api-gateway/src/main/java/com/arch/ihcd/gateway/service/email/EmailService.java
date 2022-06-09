package com.arch.ihcd.gateway.service.email;

import com.arch.ihcd.gateway.bean.TwoFACode;
import com.arch.ihcd.gateway.entity.EmailOutboxTrans;

public interface EmailService {

    public TwoFACode getEmail2FACode();

    public EmailOutboxTrans saveEmailDetails(EmailOutboxTrans email);
}
