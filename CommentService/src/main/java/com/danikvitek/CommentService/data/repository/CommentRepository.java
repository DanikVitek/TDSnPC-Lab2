package com.danikvitek.CommentService.data.repository;

import com.danikvitek.CommentService.data.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.userId = ?1 order by c.id")
    List<Comment> findByUserIdOrderById(long userId);

    @Query("select c from Comment c where c.pluginId = ?1 order by c.id")
    List<Comment> findByPluginIdOrderById(long pluginId);

    List<Comment> findByUserIdAndPluginIdOrderByPublicationTimeDesc(long userId, long pluginId);

    @Query("select r from Comment c " +
            "join CommentResponse cr on c.id = cr.parentId " +
            "join Comment r on cr.responseId = r.id where c.id = ?1 " +
            "order by r.id")
    List<Comment> findResponsesById(long id);
}
