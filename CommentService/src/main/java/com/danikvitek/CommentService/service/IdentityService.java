package com.danikvitek.CommentService.service;

import com.danikvitek.CommentService.data.model.entity.Role;
import com.danikvitek.CommentService.util.exception.NoTokenHeaderException;
import org.springframework.http.HttpHeaders;

public interface IdentityService {
    long getUserId(HttpHeaders jwtHeaders) throws NoTokenHeaderException;

    Role getRole(HttpHeaders headers) throws NoTokenHeaderException;
}
