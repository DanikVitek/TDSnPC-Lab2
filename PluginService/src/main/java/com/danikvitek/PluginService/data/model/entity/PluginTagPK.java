package com.danikvitek.PluginService.data.model.entity;

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
public class PluginTagPK implements Serializable {
    @Column(name = "plugin_id", nullable = false, updatable = false)
    @Id
    private Long pluginId;
    
    @Column(name = "tag_id", nullable = false, updatable = false)
    @Id
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginTagPK that = (PluginTagPK) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(tagId, that.tagId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }
}
