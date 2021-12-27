package com.danikvitek.IdentityService.api.controller;

import com.danikvitek.IdentityService.api.dto.SimpleUserDto;
import com.danikvitek.IdentityService.service.PluginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/plugins")
public class PluginController {
    private final PluginService pluginService;

    @GetMapping("/{id}/authors")
    public ResponseEntity<Set<SimpleUserDto>> indexAuthors(@PathVariable long id) {
        Set<SimpleUserDto> authors = pluginService.fetchByPluginId(id);
        return ResponseEntity.ok(authors);
    }
}
