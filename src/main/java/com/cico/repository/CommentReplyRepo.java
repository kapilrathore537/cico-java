package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.CommentReply;

@Repository
public interface CommentReplyRepo extends JpaRepository<CommentReply, Integer> {

}
