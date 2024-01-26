package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface CommentMapper {
    List<Comment> all(Query query);
    int count(Query query);
    int add(Comment comment);
    int update(Comment comment);
    int delete(Long id);
    int deleteByQuery(Query query);
}
