package com.danikvitek.CommentService.api.controller;

import com.danikvitek.CommentService.api.dto.CommentDto;
import com.danikvitek.CommentService.data.model.entity.Comment;
import com.danikvitek.CommentService.service.CommentService;
import com.danikvitek.CommentService.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/plugins")
public class PluginController {
    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public @NotNull ResponseEntity<Collection<CommentDto>> showComments(@PathVariable long id) {
        Collection<CommentDto> comments = commentService.fetchByPluginId(id).stream()
                .map(this::commentToDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(comments);
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
