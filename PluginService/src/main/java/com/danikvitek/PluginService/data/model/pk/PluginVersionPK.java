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
public class PluginVersionPK implements Serializable {
    @Column(name = "plugin_id", nullable = false, updatable = false)
    @Id
    private Long pluginId;
    
    @Column(name = "version_title", nullable = false, length = 20)
    @Id
    private String versionTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginVersionPK that = (PluginVersionPK) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(versionTitle, that.versionTitle)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (versionTitle != null ? versionTitle.hashCode() : 0);
        return result;
    }
}
