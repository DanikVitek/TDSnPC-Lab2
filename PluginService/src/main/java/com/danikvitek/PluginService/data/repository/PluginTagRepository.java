package com.danikvitek.PluginService.data.repository;

import com.danikvitek.PluginService.data.model.entity.PluginTag;
import com.danikvitek.PluginService.data.model.entity.PluginTagPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginTagRepository extends JpaRepository<PluginTag, PluginTagPK> {
}
