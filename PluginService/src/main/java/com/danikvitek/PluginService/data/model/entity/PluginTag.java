package com.danikvitek.PluginService.data.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "plugin_tags", schema = "course_project")
@IdClass(PluginTagPK.class)
public class PluginTag {
    @Id
    @Column(name = "plugin_id", nullable = false, updatable = false)
    private Long pluginId;
    
    @Id
    @Column(name = "tag_id", nullable = false, updatable = false)
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginTag pluginTag = (PluginTag) o;

        if (!Objects.equals(pluginId, pluginTag.pluginId)) return false;
        if (!Objects.equals(tagId, pluginTag.tagId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }
}
