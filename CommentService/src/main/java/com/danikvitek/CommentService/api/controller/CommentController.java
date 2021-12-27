package com.danikvitek.CommentService.api.controller;

import com.danikvitek.CommentService.api.dto.CommentDto;
import com.danikvitek.CommentService.data.model.entity.Comment;
import com.danikvitek.CommentService.data.model.entity.Role;
import com.danikvitek.CommentService.service.CommentService;
import com.danikvitek.CommentService.service.IdentityService;
import com.danikvitek.CommentService.util.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.danikvitek.CommentService.data.model.entity.Role.admin;
import static com.danikvitek.CommentService.data.model.entity.Role.moderator;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public final class CommentController {
    private final CommentService commentService;
    private final IdentityService identityService;

    @GetMapping("/{id}")
    public @NotNull ResponseEntity<CommentDto> show(@PathVariable long id,
                                                    @RequestHeader @NotNull HttpHeaders headers) {
        Role role = identityService.getRole(headers);
        if (role == Role.moderator || role == Role.admin) {
            CommentDto commentDto = commentToDto(commentService.fetchById(id));
            return ResponseEntity.ok(commentDto);
        } else throw new AuthException();
    }

    @PostMapping
    public @NotNull ResponseEntity<Void> create(@Valid @RequestBody CommentDto commentDto) {
        long commentId = commentService.create(
                commentDto.getPluginId(), commentDto.getUserId(), commentDto.getContent()
        ).getId();
        String location = String.format("/comments/%d", commentId);
        return ResponseEntity.created(URI.create(location)).build();
    }
    
    @PostMapping("/{id}")
    public @NotNull ResponseEntity<Void> respond(@PathVariable long id,
                                                 @Valid @RequestBody @NotNull CommentDto commentDto) {
        long commentId = commentService.respond(
                id,
                commentDto.getPluginId(), commentDto.getUserId(), commentDto.getContent()
        ).getId();
        String location = String.format("/comments/%d", commentId);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @PatchMapping("/{id}")
    public @NotNull ResponseEntity<CommentDto> update(@PathVariable long id,
                                                      @Valid @RequestBody CommentDto commentDto,
                                                      @RequestHeader @NotNull HttpHeaders headers) {
        long userId = identityService.getUserId(headers);
        Comment comment = commentService.fetchById(id);
        if (Objects.equals(comment.getUserId(), userId)) {
            CommentDto updatedComment = commentToDto(commentService.update(id, commentDto.getContent()));
            return ResponseEntity.ok(updatedComment);
        } else throw new AuthException();
    }

    @DeleteMapping("/comments/{id}")
    public @NotNull ResponseEntity<Void> delete(@PathVariable long id,
                                                @RequestHeader @NotNull HttpHeaders headers) {
        long userId = identityService.getUserId(headers);
        Comment comment = commentService.fetchById(id);
        Role role = identityService.getRole(headers);
        if (Objects.equals(comment.getUserId(), userId) || role == moderator || role == admin) {
            commentService.delete(id);
            return ResponseEntity.noContent().build();
        } else throw new AuthException();
    }

    public CommentDto commentToDto(@NotNull Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .pluginId(comment.getPluginId())
                .content(comment.getContent())
                .publicationTime(comment.getPublicationTime())
                .responses(commentService.fetchResponsesById(comment.getId()).stream()
                        .map(this::commentToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
