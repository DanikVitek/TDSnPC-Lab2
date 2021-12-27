package com.danikvitek.AuthService.service.impl;

import com.danikvitek.AuthService.api.dto.SimpleUserDto;
import com.danikvitek.AuthService.service.IdentityService;
import com.danikvitek.AuthService.util.exception.UserNotFoundException;
import org.springframework.core.env.Environment;

public class IdentityServiceImpl implements IdentityService {
    private final String identityServiceUrl;

    public IdentityServiceImpl(Environment environment) {
        this.identityServiceUrl = environment.getRequiredProperty("identity-service-url");
    }

    @Override
    public SimpleUserDto fetchByEmail(String email) throws UserNotFoundException {
        return null;
    }
}
