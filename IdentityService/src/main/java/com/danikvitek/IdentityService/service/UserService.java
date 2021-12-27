package com.danikvitek.IdentityService.service;


import com.danikvitek.IdentityService.api.dto.*;
import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.util.exception.RoleNotFoundException;
import com.danikvitek.IdentityService.util.exception.UserAlreadyExistsException;
import com.danikvitek.IdentityService.util.exception.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;

import java.util.Collection;

public interface UserService {
    @NotNull User fetchById(long id) throws UserNotFoundException;

    @NotNull Page<User> fetchPage(int page, int size) throws IllegalArgumentException;

    User fetchByUsername(String username) throws UserNotFoundException;

    User fetchByEmail(String email) throws UserNotFoundException;

    @NotNull Collection<PluginDto> fetchAuthoredPlugins(long id);

    @NotNull Collection<CommentDto> fetchComments(long id, HttpEntity<?> headers);

    @NotNull User create(@NotNull RegistrationDto registrationDto) throws UserAlreadyExistsException;

    @NotNull User updateRole(long id, String roleTitle) throws UserNotFoundException, RoleNotFoundException;

    User registrationDtoToUser(@NotNull RegistrationDto registrationDto);

    SimpleUserDto userToSimpleDto(@NotNull User user);

    FullUserDto userToFullDto(@NotNull User user);

    SimpleUserDto fullDtoToSimpleDto(@NotNull FullUserDto fullDto);
}
