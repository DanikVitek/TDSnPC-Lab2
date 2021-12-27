package com.danikvitek.IdentityService.api.controller;

import com.danikvitek.IdentityService.api.dto.RegistrationDto;
import com.danikvitek.IdentityService.api.dto.SimpleUserDto;
import com.danikvitek.IdentityService.api.dto.jwt.JwtRequestDto;
import com.danikvitek.IdentityService.api.dto.jwt.JwtResponseDto;
import com.danikvitek.IdentityService.config.security.UserDetailsImpl;
import com.danikvitek.IdentityService.config.security.jwt.JwtProcessor;
import com.danikvitek.IdentityService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.util.Try;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public final class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProcessor jwtProcessor;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public @NotNull ResponseEntity<JwtResponseDto> login(@Valid @RequestBody @NotNull JwtRequestDto jwtRequestDto) {
        String login = jwtRequestDto.getLogin(); // login is either email or username
        String username = Try.apply(() -> userService.fetchByUsername(login))
                .orElse(() -> Try.apply(() -> userService.fetchByEmail(login)))
                .get()
                .getUsername();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        jwtRequestDto.getPassword()
                )
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        String token = jwtProcessor.createJwt(
                userDetails.getUser().getId(),
                username,
                userDetails.getAuthorities().stream().findFirst().get()
        );
        return ResponseEntity.ok(new JwtResponseDto(token));
    }

    @PostMapping("/signup")
    public @NotNull ResponseEntity<SimpleUserDto> signUp(@Valid @RequestBody RegistrationDto registrationDto) {
        SimpleUserDto userDto = userService.userToSimpleDto(userService.create(registrationDto));
        return ResponseEntity.ok(userDto);
    }
}
