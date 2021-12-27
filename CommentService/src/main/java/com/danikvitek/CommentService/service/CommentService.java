package com.danikvitek.CommentService.service;

import com.danikvitek.CommentService.data.model.entity.Comment;
import com.danikvitek.CommentService.util.exception.CommentNotFoundException;
import com.danikvitek.CommentService.util.exception.PluginNotFoundException;
import com.danikvitek.CommentService.util.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CommentService {
    Comment fetchById(long id) throws CommentNotFoundException;

    Collection<Comment> fetchByPluginId(long pluginId) throws PluginNotFoundException;

    Collection<Comment> fetchByUserId(long userId) throws UserNotFoundException;

    Collection<Comment> fetchResponsesById(long id);

    @NotNull Comment create(long pluginId, long userId, @NotNull String content)
            throws UserNotFoundException, PluginNotFoundException;

    @NotNull Comment respond(long id,
                             long pluginId,
                             long userId,
                             @NotNull String content)
            throws IllegalArgumentException;

    void delete(long id);

    @NotNull Comment update(long id, @NotNull String content) throws CommentNotFoundException;
}
