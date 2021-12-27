package com.danikvitek.AuthService.api.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class JwtRequestDto implements Serializable {
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}
