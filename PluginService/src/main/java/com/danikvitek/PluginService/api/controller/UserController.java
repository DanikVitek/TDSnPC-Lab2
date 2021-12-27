package com.danikvitek.PluginService.api.controller;

import com.danikvitek.PluginService.api.dto.PluginDto;
import com.danikvitek.PluginService.service.PluginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public final class UserController {
    private final PluginService pluginService;

    @GetMapping("/{id}/authored_plugins")
    public ResponseEntity<Collection<PluginDto>> indexAuthoredPlugins(@PathVariable long id) {
        Collection<PluginDto> plugins = pluginService.fetchByAuthorId(id).stream()
                .map(pluginService::pluginToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(plugins);
    }
}
