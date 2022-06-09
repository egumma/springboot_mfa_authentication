package com.arch.ihcd.gateway.service.barcode;

import com.arch.ihcd.gateway.request.LoginViewModel;

public interface BarcodeService {

    public String generateToken(LoginViewModel credentials);

    public String generate2faQRCodeSecretKey();

    public String getTOTPCode(String secretKey);

    public String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer);

//    public String validatebarcode
}
