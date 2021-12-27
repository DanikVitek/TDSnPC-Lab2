package com.danikvitek.PluginService.api.controller;

import com.danikvitek.PluginService.api.dto.PluginDto;
import com.danikvitek.PluginService.api.dto.SimpleUserDto;
import com.danikvitek.PluginService.data.model.entity.Plugin;
import com.danikvitek.PluginService.service.PluginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/plugins")
public final class PluginController {
    private final PluginService pluginService;

    @GetMapping
    public @NotNull ResponseEntity<List<PluginDto>> index(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size) {
        if (size <= 20) {
            List<PluginDto> plugins = pluginService
                    .fetchAll(page, size)
                    .map(pluginService::pluginToDto)
                    .getContent();
            return ResponseEntity.ok(plugins);
        } else throw new IllegalArgumentException("Page size must not be greater than twenty!");
    }

    @GetMapping("/{id}")
    public @NotNull ResponseEntity<PluginDto> show(@PathVariable long id) {
        PluginDto pluginDto = pluginService.pluginToDto(pluginService.fetchById(id));
        return ResponseEntity.ok(pluginDto);
    }

    @GetMapping("/{id}/authors")
    public @NotNull ResponseEntity<Set<SimpleUserDto>> showAuthors(@PathVariable long id) {
        Set<SimpleUserDto> authors = pluginService.findByAuthoredPlugin(id);
        return ResponseEntity.ok(authors);
    }
    
    @PostMapping
    public @NotNull ResponseEntity<Void> create(@Valid @RequestBody PluginDto pluginDto) {
        Plugin plugin = pluginService.create(pluginDto);
        String location = String.format("/plugins/%d", plugin.getId());
        return ResponseEntity.created(URI.create(location)).build();
    }

    @PatchMapping("/{id}")
    public @NotNull ResponseEntity<Void> update(@PathVariable long id,
                                                @Valid @RequestBody PluginDto pluginDto) {
        pluginService.update(id, pluginDto);
        String location = String.format("/plugins/%d", id);
        return ResponseEntity.created(URI.create(location)).build();
    }
    
    @DeleteMapping("/{id}")
    public @NotNull ResponseEntity<Void> delete(@PathVariable long id) {
        pluginService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
