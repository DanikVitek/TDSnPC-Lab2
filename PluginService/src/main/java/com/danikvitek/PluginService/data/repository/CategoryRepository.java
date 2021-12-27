package com.danikvitek.PluginService.data.repository;

import com.danikvitek.PluginService.data.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Short> {
//    @Query("select c from Category c where c.title = ?1")
    Optional<Category> findByTitle(String title);
}