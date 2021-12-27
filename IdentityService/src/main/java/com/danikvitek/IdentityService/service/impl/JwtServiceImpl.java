package com.danikvitek.IdentityService.service.impl;

import com.danikvitek.IdentityService.config.security.jwt.JwtProcessor;
import com.danikvitek.IdentityService.data.model.entity.Role;
import com.danikvitek.IdentityService.service.JwtService;
import com.danikvitek.IdentityService.util.exception.NoTokenHeaderException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {
    private final JwtProcessor jwtProcessor;

    @Override
    public long getUserId(@NotNull HttpHeaders headers) {
        String token = Optional.ofNullable(headers.getFirst("Authorization"))
                .orElseThrow(NoTokenHeaderException::new)
                .replace("Bearer ", "");
        return jwtProcessor.getUserId(token);
    }

    @Override
    public Role getRole(HttpHeaders headers) {
        String token = Optional.ofNullable(headers.getFirst("Authorization"))
                .orElseThrow(NoTokenHeaderException::new)
                .replace("Bearer ", "");
        return jwtProcessor.getRole(token);
    }
}
