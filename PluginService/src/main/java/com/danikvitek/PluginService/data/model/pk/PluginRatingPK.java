package com.danikvitek.PluginService.data.model.pk;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PluginRatingPK implements Serializable {
    @Column(name = "plugin_id", nullable = false, updatable = false)
    @Id
    private Long pluginId;
    
    @Column(name = "user_id", nullable = false, updatable = false)
    @Id
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginRatingPK that = (PluginRatingPK) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(userId, that.userId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
