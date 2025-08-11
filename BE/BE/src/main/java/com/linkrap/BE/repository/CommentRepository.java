package com.linkrap.BE.repository;


import com.linkrap.BE.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> { }
