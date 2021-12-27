package com.danikvitek.IdentityService.service.impl;

import com.danikvitek.IdentityService.api.dto.SimpleUserDto;
import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.data.repository.UserRepository;
import com.danikvitek.IdentityService.service.PluginService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PluginServiceImpl implements PluginService {
    private final UserRepository userRepository;

    @Override
    public Set<SimpleUserDto> fetchByPluginId(long pluginId) {
        return userRepository.findByAuthoredPlugin(pluginId).stream()
                .map(this::userToSimpleDto)
                .collect(Collectors.toSet());
    }

    private SimpleUserDto userToSimpleDto(@NotNull User user) {
        return SimpleUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().toString())
                .build();
    }
}
