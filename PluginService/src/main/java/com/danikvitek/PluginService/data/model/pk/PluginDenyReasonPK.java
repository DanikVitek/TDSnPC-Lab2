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
public class PluginDenyReasonPK implements Serializable {
    @Column(name = "plugin_id", nullable = false, updatable = false)
    @Id
    private Long pluginId;
    
    @Column(name = "plugin_version", nullable = false, length = 20, updatable = false)
    @Id
    private String pluginVersion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginDenyReasonPK that = (PluginDenyReasonPK) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(pluginVersion, that.pluginVersion))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (pluginVersion != null ? pluginVersion.hashCode() : 0);
        return result;
    }
}
