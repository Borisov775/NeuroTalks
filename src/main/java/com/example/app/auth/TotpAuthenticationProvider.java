package com.example.app.auth;

import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TotpAuthenticationProvider implements AuthenticationProvider {

    private final UserRepo userRepo;
    private final TotpService totpService;
    private final UserEntityDetailsService userDetailsService;

    public TotpAuthenticationProvider(UserRepo userRepo, TotpService totpService, UserEntityDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.totpService = totpService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object creds = authentication.getCredentials();
        String presented = creds == null ? "" : creds.toString();

        // If presented credential is 6 digits, try to authenticate via TOTP
        if (presented.matches("\\d{6}")) {
            UserEntity user = userRepo.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            if (user.isTwoFactorEnabled()) {
                String secret = user.getTwoFactorSecret();
                try {
                    int code = Integer.parseInt(presented);
                    if (secret != null && totpService.verifyCode(secret, code)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        token.setDetails(authentication.getDetails());
                        return token;
                    }
                } catch (NumberFormatException ex) {
                    // not numeric; fall through
                }
            }
        }

        // Not a TOTP attempt — return null to let other providers try
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
