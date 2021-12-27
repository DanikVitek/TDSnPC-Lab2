package com.danikvitek.PluginService.data.repository;

import com.danikvitek.PluginService.data.model.entity.PluginAuthor;
import com.danikvitek.PluginService.data.model.pk.PluginAuthorPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginAuthorRepository extends JpaRepository<PluginAuthor, PluginAuthorPK> {}