package com.danikvitek.AuthService.api.controller;

import com.danikvitek.AuthService.api.dto.jwt.JwtRequestDto;
import com.danikvitek.AuthService.api.dto.jwt.JwtResponseDto;
import com.danikvitek.AuthService.service.AuthService;
import com.danikvitek.AuthService.service.IdentityService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.util.Try;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public final class AuthenticationController {
    private final AuthService authService;
    private final IdentityService identityService;

    @PostMapping("/login")
    public @NotNull ResponseEntity<JwtResponseDto> login(@Valid @RequestBody @NotNull JwtRequestDto jwtRequestDto) {
        String username = Optional.ofNullable(jwtRequestDto.getUsername())
                .orElse(Try.apply(() -> identityService.fetchByEmail(jwtRequestDto.getEmail()))
                        .get()
                        .getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        jwtRequestDto.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtProcessor.createJwt(
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
