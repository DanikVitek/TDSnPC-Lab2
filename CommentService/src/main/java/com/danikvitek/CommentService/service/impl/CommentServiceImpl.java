package com.danikvitek.CommentService.service.impl;

import com.danikvitek.CommentService.api.dto.PluginDto;
import com.danikvitek.CommentService.data.model.entity.Comment;
import com.danikvitek.CommentService.data.model.entity.CommentResponse;
import com.danikvitek.CommentService.data.repository.CommentRepository;
import com.danikvitek.CommentService.data.repository.CommentResponseRepository;
import com.danikvitek.CommentService.service.CommentService;
import com.danikvitek.CommentService.util.exception.CommentNotFoundException;
import com.danikvitek.CommentService.util.exception.PluginNotFoundException;
import com.danikvitek.CommentService.util.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Objects;

@Service
public final class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentResponseRepository commentResponseRepository;

    private final String pluginServiceUrl;

    public CommentServiceImpl(CommentRepository commentRepository,
                              CommentResponseRepository commentResponseRepository,
                              @NotNull Environment environment) {
        this.commentRepository = commentRepository;
        this.commentResponseRepository = commentResponseRepository;
        this.pluginServiceUrl = environment.getRequiredProperty("plugin-service-url");
    }

    public Comment fetchById(long id) throws CommentNotFoundException {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    public Collection<Comment> fetchByPluginId(long pluginId) throws PluginNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<PluginDto> responsePlugin = restTemplate.getForEntity(
                    pluginServiceUrl + "/plugins/" + pluginId,
                    PluginDto.class
            );
            return commentRepository.findByPluginIdOrderById(Objects.requireNonNull(responsePlugin.getBody()).getId());
        } catch (HttpClientErrorException.NotFound e) {
            throw new PluginNotFoundException();
        }
    }

    public Collection<Comment> fetchByUserId(long userId) throws UserNotFoundException {
        return commentRepository.findByUserIdOrderById(userId);
    }

    public Collection<Comment> fetchResponsesById(long id) {
        return commentRepository.findResponsesById(id);
    }

    public @NotNull Comment create(long pluginId, long userId, @NotNull String content) 
            throws UserNotFoundException, PluginNotFoundException {
        Comment newComment = Comment.builder()
                .pluginId(pluginId)
                .userId(userId)
                .content(content)
                .build();
        return commentRepository.save(newComment);
    }

    public @NotNull Comment respond(long id, 
                                    long pluginId, long userId, @NotNull String content) 
            throws IllegalArgumentException {
        Comment parentComment = fetchById(id);
        if (!Objects.equals(parentComment.getPluginId(), pluginId))
            throw new IllegalArgumentException("Parent comment plugin ID must be the same for both parent and response comment");
        Comment newComment = create(pluginId, userId, content);
        CommentResponse commentResponse = CommentResponse.builder()
                .parentId(parentComment.getId())
                .responseId(newComment.getId())
                .build();
        commentResponseRepository.save(commentResponse);
        return newComment;
    }

    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    public @NotNull Comment update(long id, @NotNull String content) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
}
