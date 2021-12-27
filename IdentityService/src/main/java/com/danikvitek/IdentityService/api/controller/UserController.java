package com.danikvitek.IdentityService.api.controller;

import com.danikvitek.IdentityService.api.dto.CommentDto;
import com.danikvitek.IdentityService.api.dto.PluginDto;
import com.danikvitek.IdentityService.api.dto.SimpleUserDto;
import com.danikvitek.IdentityService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public final class UserController {
    private final UserService userService;

    @GetMapping
    public @NotNull ResponseEntity<List<SimpleUserDto>> index(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        if (size <= 20) {
            List<SimpleUserDto> users = userService.fetchPage(page, size)
                    .map(userService::userToSimpleDto)
                    .getContent();
            return ResponseEntity.ok(users);
        }
        else throw new IllegalArgumentException("Page size must not be greater than twenty!");
    }

    @GetMapping("/{id}")
    public @NotNull ResponseEntity<SimpleUserDto> show(@PathVariable long id) {
        SimpleUserDto user = userService.userToSimpleDto(userService.fetchById(id));
        return ResponseEntity.ok(user);
    }

    @GetMapping(params = "username")
    public @NotNull ResponseEntity<SimpleUserDto> show(@RequestParam String username) {
        SimpleUserDto user = userService.userToSimpleDto(userService.fetchByUsername(username));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/authored_plugins")
    public @NotNull ResponseEntity<Collection<PluginDto>> showAuthoredPlugins(@PathVariable long id) {
        Collection<PluginDto> plugins = userService.fetchAuthoredPlugins(id);
        return ResponseEntity.ok(plugins);
    }

    @GetMapping("/{id}/comments")
    @PreAuthorize("hasAnyRole('moderator', 'admin')")
    public @NotNull ResponseEntity<Collection<CommentDto>> showComments(@PathVariable long id,
                                                                        @RequestHeader HttpHeaders headers) {
        Collection<CommentDto> comments = userService.fetchComments(id, new HttpEntity<>(headers));
        return ResponseEntity.ok(comments);
    }
}