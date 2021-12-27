package com.danikvitek.IdentityService.service.impl;

import com.danikvitek.IdentityService.api.dto.*;
import com.danikvitek.IdentityService.data.model.entity.Role;
import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.data.repository.UserRepository;
import com.danikvitek.IdentityService.service.UserService;
import com.danikvitek.IdentityService.util.exception.RoleNotFoundException;
import com.danikvitek.IdentityService.util.exception.UserAlreadyExistsException;
import com.danikvitek.IdentityService.util.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import scala.util.Try;

import java.util.*;

@Slf4j
@Service
public final class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String pluginServiceUrl;
    private final String commentServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @NotNull Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pluginServiceUrl = environment.getRequiredProperty("plugin-service-url");
        this.commentServiceUrl = environment.getRequiredProperty("comment-service-url");
    }


    public @NotNull User fetchById(long id) throws UserNotFoundException {
        if (id >= 1)
            return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        else throw new IllegalArgumentException("ID must be >= 1");
    }

    public @NotNull Page<User> fetchPage(int page, int size) throws IllegalArgumentException {
        if (page < 0) throw new IllegalArgumentException("Page index must be >= 0");
        else if (size < 1) throw new IllegalArgumentException("Page size must be >= 1");
        else return userRepository.findAll(Pageable.ofSize(size).withPage(page));
    }

    public User fetchByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User fetchByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public @NotNull Collection<PluginDto> fetchAuthoredPlugins(long id) {
        ResponseEntity<Collection<PluginDto>> result = restTemplate.exchange(
                pluginServiceUrl + "/users/{id}/authored_plugins",
                HttpMethod.GET,
                null,
                ParameterizedTypeReference.forType(Collection.class),
                id
        );
        if (result.getStatusCode() == HttpStatus.OK)
            return Objects.requireNonNull(result.getBody());
        else throw new UserNotFoundException();
    }

    public @NotNull Collection<CommentDto> fetchComments(long id, HttpEntity<?> headers) {
        ResponseEntity<Collection<CommentDto>> result = restTemplate.exchange(
                commentServiceUrl + "/users/" + id + "/comments",
                HttpMethod.GET,
                headers,
                ParameterizedTypeReference.forType(Collection.class)
        );
        if (result.getStatusCode() == HttpStatus.OK)
            return Objects.requireNonNull(result.getBody());
        else throw new UserNotFoundException();
    }

    public @NotNull User create(@NotNull RegistrationDto registrationDto) {
        try {
            fetchByUsername(registrationDto.getUsername());
            fetchByEmail(registrationDto.getEmail());
        } catch (UserNotFoundException e) {
            User newUser = registrationDtoToUser(registrationDto);
            return userRepository.save(newUser);
        }
        throw new UserAlreadyExistsException();
    }

    public @NotNull User updateRole(long id, String roleTitle) throws UserNotFoundException, RoleNotFoundException {
        User user = fetchById(id);
        Role role = Try.apply(() -> Role.valueOf(roleTitle)).getOrElse(() -> {
            throw new RoleNotFoundException();
        });
        user.setRole(role);
        return userRepository.save(user);
    }

    public User registrationDtoToUser(@NotNull RegistrationDto registrationDto) {
        return User.builder()
                .username(registrationDto.getUsername())
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .build();
    }

    public SimpleUserDto userToSimpleDto(@NotNull User user) {
        return SimpleUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    public FullUserDto userToFullDto(@NotNull User user) {
        return FullUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .password(user.getPassword())
                .registrationTime(user.getRegistrationTime())
                .build();
    }

    public SimpleUserDto fullDtoToSimpleDto(@NotNull FullUserDto fullDto) {
        return SimpleUserDto.builder()
                .id(fullDto.getId())
                .username(fullDto.getUsername())
                .firstName(fullDto.getFirstName())
                .lastName(fullDto.getLastName())
                .email(fullDto.getEmail())
                .role(fullDto.getRole())
                .build();
    }
}
