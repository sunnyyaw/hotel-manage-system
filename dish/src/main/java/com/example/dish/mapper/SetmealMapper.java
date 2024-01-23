package com.example.dish.mapper;

import com.example.dish.common.Query;
import com.example.dish.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SetmealMapper {
    List<Setmeal> all(Query query);
    Setmeal get(Long id);
    int count(Query query);
    int add(Setmeal setmeal);
    int update(Setmeal setmeal);
    int delete(Long id);
}
