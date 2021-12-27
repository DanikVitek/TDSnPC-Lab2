package com.danikvitek.PluginService.service.impl;

import com.danikvitek.PluginService.api.dto.RatingDto;
import com.danikvitek.PluginService.data.model.entity.Plugin;
import com.danikvitek.PluginService.data.model.entity.PluginRating;
import com.danikvitek.PluginService.data.model.pk.PluginRatingPK;
import com.danikvitek.PluginService.data.repository.PluginRatingRepository;
import com.danikvitek.PluginService.data.repository.PluginRepository;
import com.danikvitek.PluginService.util.exception.PluginNotFoundException;
import com.danikvitek.PluginService.util.exception.PluginRatingNotFoundException;
import com.danikvitek.PluginService.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public final class RatingServiceImpl {
    private final PluginRatingRepository pluginRatingRepository;
    private final PluginRepository pluginRepository;

    public PluginRating fetchById(long pluginId, long userId)
            throws PluginNotFoundException, UserNotFoundException {
        if (pluginId >= 1) {
            if (userId >= 1)
                return pluginRatingRepository.findById(PluginRatingPK.builder()
                                .pluginId(pluginId)
                                .userId(userId)
                                .build())
                        .orElseThrow(PluginRatingNotFoundException::new);
            else throw new IllegalArgumentException("User ID must be >= 1");
        } else throw new IllegalArgumentException("Plugin ID must be >= 1");
    }

    public Double fetchForPlugin(long pluginId) throws PluginNotFoundException {
        if (pluginId >= 1) {
            Plugin plugin = pluginRepository
                    .findById(pluginId)
                    .orElseThrow(PluginNotFoundException::new);
            return pluginRatingRepository
                    .getAvgByPluginId(plugin.getId())
                    .orElse(null);
        }
        else throw new IllegalArgumentException("Plugin ID must be >= 1");
    }

    public long fetchAmountForPlugin(long pluginId) {
        if (pluginId >= 1) {
            Plugin plugin = pluginRepository.findById(pluginId).orElseThrow(PluginNotFoundException::new);
            return pluginRatingRepository.getCountByPluginId(plugin.getId());
        }
        else throw new IllegalArgumentException("Plugin ID must be >= 1");
    }

    public @NotNull PluginRating create(RatingDto ratingDto)
            throws PluginNotFoundException, UserNotFoundException {
        PluginRating rating = dtoToRating(ratingDto);
        return pluginRatingRepository.save(rating);
    }

    public @NotNull PluginRating update(long pluginId, long userId, @NotNull RatingDto ratingDto)
            throws PluginNotFoundException, UserNotFoundException {
        PluginRating rating = fetchById(pluginId, userId);
        rating.setRating(ratingDto.getRating());
        return pluginRatingRepository.save(rating);
    }

    public PluginRating dtoToRating(@NotNull RatingDto ratingDto)
            throws PluginNotFoundException, UserNotFoundException {
        Plugin plugin = pluginRepository
                .findById(ratingDto.getPluginId())
                .orElseThrow(PluginNotFoundException::new);
        return PluginRating.builder()
                .pluginId(plugin.getId())
                .userId(ratingDto.getUserId())
                .rating(ratingDto.getRating())
                .build();
    }

    public RatingDto ratingToDto(@NotNull PluginRating pluginRating) {
        return RatingDto.builder()
                .rating(pluginRating.getRating())
                .pluginId(pluginRating.getPluginId())
                .userId(pluginRating.getUserId())
                .build();
    }
}
