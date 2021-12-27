package com.danikvitek.PluginService.data.repository;

import com.danikvitek.PluginService.data.model.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Long> {
    @Query("select p from Plugin p join PluginAuthor pa on pa.pluginId = p.id where pa.userId = ?1")
    Set<Plugin> findAuthoredPlugins(long authorId);
    
    Optional<Plugin> findByTitle(String title);
}