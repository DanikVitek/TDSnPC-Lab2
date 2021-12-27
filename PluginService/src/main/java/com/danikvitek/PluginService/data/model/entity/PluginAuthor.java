package com.danikvitek.PluginService.data.model.entity;

import com.danikvitek.PluginService.data.model.pk.PluginAuthorPK;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plugin_authors", schema = "course_project")
@IdClass(PluginAuthorPK.class)
public class PluginAuthor {
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
    
    @Id
    @Column(name = "plugin_id", nullable = false, updatable = false)
    private Long pluginId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginAuthor that = (PluginAuthor) o;

        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(pluginId, that.pluginId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (pluginId != null ? pluginId.hashCode() : 0);
        return result;
    }
}
