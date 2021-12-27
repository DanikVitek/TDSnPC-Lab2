package com.danikvitek.CommentService.data.repository;

import com.danikvitek.CommentService.data.model.entity.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentResponseRepository extends JpaRepository<CommentResponse, Long> {
}