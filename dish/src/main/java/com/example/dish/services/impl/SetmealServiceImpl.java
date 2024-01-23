package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.entity.Setmeal;
import com.example.dish.mapper.SetmealMapper;
import com.example.dish.services.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public List<Setmeal> all(Query query) {
        return setmealMapper.all(query);
    }

    @Override
    public Setmeal get(Long id)throws Exception{
        Setmeal setmeal = setmealMapper.get(id);
        if(setmeal==null)
            throw new Exception("找不到套餐");
        return setmeal;
    }

    @Override
    public int count(Query query) {
        return setmealMapper.count(query);
    }

    @Override
    public void add(Setmeal setmeal)throws Exception {
        if(setmealMapper.add(setmeal)==0)
            throw new Exception("添加失败");
    }

    @Override
    public void update(Setmeal setmeal)throws Exception{
        if(setmealMapper.update(setmeal)==0)
            throw new Exception("更新失败");
    }

    @Override
    public void delete(Long id)throws Exception {
        if(setmealMapper.delete(id)==0)
            throw new Exception("找不到套餐");
    }
}
