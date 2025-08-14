package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
