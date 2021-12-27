package com.danikvitek.PluginService.data.model.entity;

import com.danikvitek.PluginService.data.model.pk.PluginDenyReasonPK;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plugin_deny_reasons", schema = "course_project")
@IdClass(PluginDenyReasonPK.class)
public class PluginDenyReason {
    @Id
    @Column(name = "plugin_id", nullable = false, updatable = false)
    private Long pluginId;
    
    @Id
    @Column(name = "plugin_version", nullable = false, updatable = false, length = 20)
    private String pluginVersion;
    
    @Column(name = "reason", length = 200)
    private String reason;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginDenyReason that = (PluginDenyReason) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(pluginVersion, that.pluginVersion)) return false;
        if (!Objects.equals(reason, that.reason)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (pluginVersion != null ? pluginVersion.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
