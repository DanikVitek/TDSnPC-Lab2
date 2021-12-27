package com.danikvitek.PluginService.service;

import com.danikvitek.PluginService.api.dto.PluginDto;
import com.danikvitek.PluginService.api.dto.SimpleUserDto;
import com.danikvitek.PluginService.data.model.entity.Plugin;
import com.danikvitek.PluginService.util.exception.PluginNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface PluginService {
    @NotNull Page<Plugin> fetchAll(int page, int size);

    @NotNull Plugin fetchById(long id) throws IllegalArgumentException, PluginNotFoundException;

    Plugin fetchByTitle(String title) throws PluginNotFoundException;

    Set<Plugin> fetchByAuthorId(long userId);

    @NotNull Set<SimpleUserDto> findByAuthoredPlugin(long id);

    @NotNull Plugin create(@NotNull PluginDto pluginDto);

    void update(long id, @NotNull PluginDto pluginDto);

    void delete(long id);

    PluginDto pluginToDto(@NotNull Plugin plugin);
}
