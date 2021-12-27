package com.danikvitek.PluginService.data.model.entity;

import com.danikvitek.PluginService.data.model.pk.PluginVersionPK;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plugin_versions", schema = "course_project")
@IdClass(PluginVersionPK.class)
public class PluginVersion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "plugin_id", nullable = false, updatable = false)
    private Long pluginId;
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "version_title", nullable = false, length = 20)
    private String versionTitle;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "upload_state", nullable = false)
    @Builder.Default
    private UploadState uploadState = UploadState.Processing;
    
    @Column(name = "file", nullable = false)
    private byte[] file;
    
    @Column(name = "upload_time", nullable = false, updatable = false)
    private Timestamp uploadTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginVersion that = (PluginVersion) o;

        if (!Objects.equals(pluginId, that.pluginId)) return false;
        if (!Objects.equals(versionTitle, that.versionTitle)) return false;
        if (!Objects.equals(uploadState, that.uploadState)) return false;
        if (!Arrays.equals(file, that.file)) return false;
        if (!Objects.equals(uploadTime, that.uploadTime)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pluginId != null ? pluginId.hashCode() : 0;
        result = 31 * result + (versionTitle != null ? versionTitle.hashCode() : 0);
        result = 31 * result + (uploadState != null ? uploadState.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(file);
        result = 31 * result + (uploadTime != null ? uploadTime.hashCode() : 0);
        return result;
    }
}
