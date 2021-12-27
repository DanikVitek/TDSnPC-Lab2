package com.danikvitek.PluginService.data.model.entity;

import com.danikvitek.PluginService.data.model.pk.PluginRatingPK;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plugin_ratings", schema = "course_project")
@IdClass(PluginRatingPK.class)
public class PluginRating {
    @Id
    @Column(name = "plugin_id", nullable = false, updatable = false)
    private Long pluginId;
    
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
    
    @Column(name = "rating", nullable = false)
    private Byte rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginRating that = (PluginRating) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(rating, that.rating)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }
}
