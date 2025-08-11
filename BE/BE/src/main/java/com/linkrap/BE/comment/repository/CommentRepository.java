package com.linkrap.BE.comment.repository;


import com.linkrap.BE.comment.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> { }
