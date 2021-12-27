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
public class PluginAuthorPK implements Serializable {
    @Column(name = "user_id", nullable = false, updatable = false)
    @Id
    private Long userId;
    
    @Column(name = "plugin_id", nullable = false, updatable = false)
    @Id
    private Long pluginId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginAuthorPK that = (PluginAuthorPK) o;

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
