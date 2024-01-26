package com.example.dish;

import com.example.dish.entity.Comment;
import com.example.dish.mapper.CommentMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CommentTest {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private CommentMapper commentMapper;
    @Test
    @Transactional
    public void insertData(){
        for(int i=0;i<100000;i++){
            commentMapper.add(new Comment());
        }
    }
}
