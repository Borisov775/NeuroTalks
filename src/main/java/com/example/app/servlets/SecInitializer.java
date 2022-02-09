package com.example.app.servlets;


import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecInitializer extends AbstractSecurityWebApplicationInitializer {
    @Override
    protected boolean enableHttpSessionEventPublisher() {
        return true;
    }
}
