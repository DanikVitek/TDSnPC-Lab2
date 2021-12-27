package com.danikvitek.IdentityService.data.repository;

import com.danikvitek.IdentityService.data.model.entity.PluginAuthor;
import com.danikvitek.IdentityService.data.model.pk.PluginAuthorPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PluginAuthorRepository extends JpaRepository<PluginAuthor, PluginAuthorPK> {

}
