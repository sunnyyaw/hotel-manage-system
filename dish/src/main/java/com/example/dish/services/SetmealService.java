package com.example.dish.services;

import com.example.dish.common.Query;
import com.example.dish.entity.Setmeal;

import java.util.List;

public interface SetmealService {
    List<Setmeal> all(Query query);
    Setmeal get(Long id)throws Exception;
    int count(Query query);
    void add(Setmeal setmeal) throws Exception;
    void update(Setmeal setmeal) throws Exception;
    void delete(Long id) throws Exception;
    void updateBatch(List<Setmeal> setmealList)throws Exception;
    void deleteBatch(List<Long> ids)throws Exception;
}
