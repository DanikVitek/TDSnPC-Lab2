package com.danikvitek.IdentityService.service;

import com.danikvitek.IdentityService.data.model.entity.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;

public interface JwtService {
    long getUserId(@NotNull HttpHeaders headers);

    Role getRole(HttpHeaders headers);
}
