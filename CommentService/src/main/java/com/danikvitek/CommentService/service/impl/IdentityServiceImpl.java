package com.danikvitek.CommentService.service.impl;

import com.danikvitek.CommentService.data.model.entity.Role;
import com.danikvitek.CommentService.service.IdentityService;
import com.danikvitek.CommentService.util.exception.AuthException;
import com.danikvitek.CommentService.util.exception.NoTokenHeaderException;
import com.danikvitek.CommentService.util.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class IdentityServiceImpl implements IdentityService {
    private final String identityServiceUrl;

    public IdentityServiceImpl(@NotNull Environment environment) {
        this.identityServiceUrl = environment.getRequiredProperty("identity-service-url");
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public long getUserId(HttpHeaders jwtHeaders) throws NoTokenHeaderException {
        try {
            ResponseEntity<Long> userDtoResponse = restTemplate.exchange(
                    identityServiceUrl + "/jwt/user_id",
                    HttpMethod.GET,
                    new HttpEntity<>(jwtHeaders),
                    long.class
            );
            return Objects.requireNonNull(userDtoResponse.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Role getRole(HttpHeaders headers) throws NoTokenHeaderException {
        try {
            ResponseEntity<Role> userDtoResponse = restTemplate.exchange(
                    identityServiceUrl + "/jwt/role",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Role.class
            );
            return userDtoResponse.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new AuthException();
        }
    }
}
