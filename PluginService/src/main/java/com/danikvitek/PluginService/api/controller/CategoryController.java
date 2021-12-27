package com.danikvitek.PluginService.api.controller;

import com.danikvitek.PluginService.api.dto.CategoryDto;
import com.danikvitek.PluginService.data.model.entity.Category;
import com.danikvitek.PluginService.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public final class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public @NotNull ResponseEntity<Set<CategoryDto>> index() {
        Set<CategoryDto> categories = categoryService.fetchAll().stream()
                .map(this::categoryToDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public @NotNull ResponseEntity<CategoryDto> show(@PathVariable short id) {
        CategoryDto category = categoryToDto(categoryService.fetchById(id));
        return ResponseEntity.ok(category);
    }

    private CategoryDto categoryToDto(@NotNull Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }
}
