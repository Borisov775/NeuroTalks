package com.example.app.auth;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class TotpService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecret() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public boolean verifyCode(String secret, int code) {
        if (secret == null) return false;
        return gAuth.authorize(secret, code);
    }

    public String getOtpAuthUrl(String issuer, String accountName, String secret) {
        // otpauth://totp/{issuer}:{account}?secret={secret}&issuer={issuer}
        return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, accountName, secret, issuer);
    }
}
