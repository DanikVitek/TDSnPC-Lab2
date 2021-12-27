package com.danikvitek.IdentityService.api.controller;

import com.danikvitek.IdentityService.data.model.entity.Role;
import com.danikvitek.IdentityService.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("jwt")
public class JwtController {
    private final JwtService jwtService;

    @GetMapping("/user_id")
    public ResponseEntity<Long> showUserId(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(jwtService.getUserId(headers));
    }

    @GetMapping("/role")
    public ResponseEntity<Role> showRole(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(jwtService.getRole(headers));
    }
}
