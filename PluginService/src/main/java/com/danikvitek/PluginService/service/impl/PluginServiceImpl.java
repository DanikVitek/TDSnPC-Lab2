package com.danikvitek.PluginService.service.impl;

import com.danikvitek.PluginService.api.dto.PluginDto;
import com.danikvitek.PluginService.api.dto.SimpleUserDto;
import com.danikvitek.PluginService.data.model.entity.Plugin;
import com.danikvitek.PluginService.data.model.entity.PluginAuthor;
import com.danikvitek.PluginService.data.model.entity.PluginTag;
import com.danikvitek.PluginService.data.model.entity.Tag;
import com.danikvitek.PluginService.data.repository.PluginAuthorRepository;
import com.danikvitek.PluginService.data.repository.PluginRatingRepository;
import com.danikvitek.PluginService.data.repository.PluginRepository;
import com.danikvitek.PluginService.data.repository.PluginTagRepository;
import com.danikvitek.PluginService.service.PluginService;
import com.danikvitek.PluginService.util.exception.*;
import com.fasterxml.classmate.GenericType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.DuplicateMappingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import scala.util.Try;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public final class PluginServiceImpl implements PluginService {
    private final PluginRepository pluginRepository;
    private final PluginAuthorRepository pluginAuthorRepository;
    private final PluginTagRepository pluginTagRepository;
    private final PluginRatingRepository pluginRatingRepository;

    private final CategoryServiceImpl categoryService;
    private final TagServiceImpl tagService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String identityServiceUrl;

    public PluginServiceImpl(PluginRepository pluginRepository,
                             PluginAuthorRepository pluginAuthorRepository,
                             PluginTagRepository pluginTagRepository,
                             PluginRatingRepository pluginRatingRepository,
                             CategoryServiceImpl categoryService,
                             TagServiceImpl tagService,
                             @NotNull Environment environment) {
        this.pluginRepository = pluginRepository;
        this.pluginAuthorRepository = pluginAuthorRepository;
        this.pluginTagRepository = pluginTagRepository;
        this.pluginRatingRepository = pluginRatingRepository;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.identityServiceUrl = environment.getRequiredProperty("identity-service-url");
    }

    public @NotNull Page<Plugin> fetchAll(int page, int size) {
        return pluginRepository.findAll(Pageable.ofSize(size).withPage(page));
    }

    public @NotNull Plugin fetchById(long id) throws IllegalArgumentException, PluginNotFoundException {
        if (id >= 1) return pluginRepository.findById(id)
                .orElseThrow(PluginNotFoundException::new);
        else throw new IllegalArgumentException("ID must be >= 1");
    }

    public Plugin fetchByTitle(String title) throws PluginNotFoundException {
        return pluginRepository.findByTitle(title).orElseThrow(PluginNotFoundException::new);
    }

    public Set<Plugin> fetchByAuthorId(long userId)
            throws IllegalArgumentException, UserNotFoundException {
        if (userId >= 1)
            try {
                return pluginRepository.findAuthoredPlugins(userId);
            } catch (Exception e) {
                log.warn(String.format("Caught exception while fetching authored plugins: %s", e.getMessage()));
                throw new UserNotFoundException();
            }
        else throw new IllegalArgumentException("User id must be >= 1");
    }

    public @NotNull Plugin create(@NotNull PluginDto pluginDto)
            throws AuthorsSetIsEmptyException, CategoryNotFoundException,
                   PluginAlreadyExistsException, UserNotFoundException {
        Plugin plugin = dtoToPlugin(pluginDto);
        Set<String> authors = Optional.ofNullable(pluginDto.getAuthors())
                .filter(maybeAuthors -> !maybeAuthors.isEmpty())
                .orElseThrow(AuthorsSetIsEmptyException::new);
        Plugin savedPlugin = Try.apply(() -> pluginRepository.save(plugin)).getOrElse(() -> {
            throw new PluginAlreadyExistsException(fetchByTitle(pluginDto.getTitle()).getId());
        });
        Optional.ofNullable(pluginDto.getTags())
                .ifPresent(maybeTags -> maybeTags.forEach(tagTitle -> {
                    Tag tag;
                    try {
                        tag = tagService.create(tagTitle);
                    } catch (Exception e) {
                        tag = tagService.fetchByTitle(tagTitle);
                    }
                    PluginTag pluginTag = PluginTag.builder()
                            .pluginId(savedPlugin.getId())
                            .tagId(tag.getId())
                            .build();
                    pluginTagRepository.save(pluginTag);
                }));
        authors.forEach(author -> {
            SimpleUserDto userDtoResponse = fetchUserByUsername(author);
            long authorId = userDtoResponse.getId();
            PluginAuthor pluginAuthor = PluginAuthor.builder()
                    .pluginId(savedPlugin.getId())
                    .userId(authorId)
                    .build();
            log.info(String.format("Saving plugin-author pair for plugin \"%s\" and user \"%s\"", savedPlugin.getTitle(), author));
            pluginAuthorRepository.save(pluginAuthor);
            log.info(String.format("Saved plugin-author pair for plugin \"%s\" and user \"%s\"", savedPlugin.getTitle(), author));
        });
        return savedPlugin;
    }

    @NotNull
    private SimpleUserDto fetchUserByUsername(String username) throws UserNotFoundException {
        ResponseEntity<SimpleUserDto> userDtoResponse;
        try {
            userDtoResponse = restTemplate.getForEntity(
                    identityServiceUrl + "?username=" + username,
                    SimpleUserDto.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException();
        }
        return Objects.requireNonNull(userDtoResponse.getBody());
    }

    public void update(long id, @NotNull PluginDto pluginDto)
            throws IllegalArgumentException, PluginNotFoundException, CategoryNotFoundException {
        Plugin plugin = fetchById(id);

        String title = pluginDto.getTitle();
        if (title != null && !Objects.equals(plugin.getTitle(), title)) plugin.setTitle(title);

        String description = pluginDto.getDescription();
        if (description != null && !Objects.equals(plugin.getDescription(), description))
            plugin.setDescription(description);

        Optional.ofNullable(pluginDto.getCategoryTitle())
                .map(categoryService::fetchByTitle)
                .filter(maybeCategory -> !Objects.equals(plugin.getCategoryId(), maybeCategory.getId()))
                .ifPresent(category -> plugin.setCategoryId(category.getId()));

        byte[] icon = pluginDto.getIcon();
        if (icon != null && plugin.getIcon() != icon) plugin.setIcon(icon);

        BigDecimal price = pluginDto.getPrice();
        if (price != null && !Objects.equals(plugin.getPrice(), price)) plugin.setPrice(price);

        Optional.ofNullable(pluginDto.getAuthors())
                .map(maybeAuthors -> maybeAuthors.stream()
                        .map(username -> Try.apply(() -> fetchUserByUsername(username)))
                        .filter(Try::isSuccess)
                        .map(Try::get)
                        .collect(Collectors.toSet()))
                .map(maybeAuthors -> maybeAuthors.stream()
                        .map(author -> {
                            try {
                                ResponseEntity<SimpleUserDto> userDtoResponse = restTemplate.getForEntity(
                                        identityServiceUrl + "/users?username=" + author,
                                        SimpleUserDto.class
                                );
                                return userDtoResponse.getBody();
                            } catch (HttpClientErrorException.NotFound e) {
                                throw new UserNotFoundException();
                            }
                        })
                        .collect(Collectors.toSet())) // Set<SimpleUserDto>
                .filter(maybeAuthors -> findByAuthoredPlugin(id) != maybeAuthors)
                .ifPresent(authors -> authors // если множество авторов не равно тому, которое дано тут, то заменяем на множество, которое дано тут
                        .forEach(user -> {
                            PluginAuthor pluginAuthor = PluginAuthor.builder()
                                    .pluginId(plugin.getId())
                                    .userId(user.getId())
                                    .build();
                            pluginAuthorRepository.save(pluginAuthor);
                        }));

        Optional.ofNullable(pluginDto.getTags())
                .map(maybeTags -> maybeTags.stream()
                        .map(tagTitle -> Try
                                .apply(() -> tagService.fetchByTitle(tagTitle))
                                .getOrElse(() -> tagService.create(tagTitle)))
                        .collect(Collectors.toSet()))
                .filter(maybeTags -> tagService.fetchByPlugin(plugin.getId()) != maybeTags)
                .ifPresent(tags -> tags
                        .forEach(tag -> {
                            PluginTag pluginTag = PluginTag.builder()
                                    .pluginId(plugin.getId())
                                    .tagId(tag.getId())
                                    .build();
                            pluginTagRepository.save(pluginTag);
                        }));
    }

    public void delete(long id) {
        pluginRepository.findById(id).ifPresent(pluginRepository::delete);
    }

    public @NotNull Set<SimpleUserDto> findByAuthoredPlugin(long pluginId) {
        try {
            ResponseEntity<Set<SimpleUserDto>> usersDtoResponse = restTemplate.exchange(
                    identityServiceUrl + "/plugins/" + pluginId + "/authors",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(usersDtoResponse.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new PluginNotFoundException();
        }
    }

    public PluginDto pluginToDto(@NotNull Plugin plugin) {
        return PluginDto.builder()
                .id(plugin.getId())
                .title(plugin.getTitle())
                .description(plugin.getDescription())
                .categoryTitle(categoryService.fetchById(plugin.getCategoryId()).getTitle())
                .icon(plugin.getIcon())
                .price(plugin.getPrice())
                .authors(findByAuthoredPlugin(plugin.getId()).stream()
                        .map(SimpleUserDto::getUsername)
                        .collect(Collectors.toSet()))
                .tags(tagService.fetchByPlugin(plugin.getId()).stream()
                        .map(Tag::getTitle)
                        .collect(Collectors.toSet()))
                .rating(pluginRatingRepository.getAvgByPluginId(plugin.getId()).orElse(null))
                .ratingAmount(pluginRatingRepository.getCountByPluginId(plugin.getId()))
                .build();
    }

    /**
     * Maps DTO to Plugin including ID, provided by the DTO
     *
     * @return the mapped plugin
     */
    public Plugin dtoToPlugin(PluginDto pluginDto) {
        return dtoToPlugin(pluginDto, false);
    }

    /**
     * Maps DTO to Plugin
     *
     * @param includeId weather to include the ID provided by the DTO
     * @return the mapped plugin
     */
    public Plugin dtoToPlugin(PluginDto pluginDto, boolean includeId) throws CategoryNotFoundException {
        Plugin.PluginBuilder builder = Plugin.builder();
        if (includeId) builder = builder.id(pluginDto.getId());
        return builder
                .title(pluginDto.getTitle())
                .description(pluginDto.getDescription())
                .categoryId(categoryService.fetchByTitle(pluginDto.getCategoryTitle()).getId())
                .icon(pluginDto.getIcon())
                .price(pluginDto.getPrice())
                .build();
    }
}
