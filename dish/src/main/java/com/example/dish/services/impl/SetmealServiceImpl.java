package com.example.dish.services.impl;

import com.example.dish.common.Query;
import com.example.dish.entity.Setmeal;
import com.example.dish.entity.Setmeal_Dish;
import com.example.dish.mapper.DishMapper;
import com.example.dish.mapper.SetmealMapper;
import com.example.dish.mapper.Setmeal_DishMapper;
import com.example.dish.services.SetmealService;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private Setmeal_DishMapper setmeal_dishMapper;
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
    @Transactional
    public void add(Setmeal setmeal)throws Exception {
        if(setmealMapper.add(setmeal)==0)
            throw new Exception("添加失败");
        List<Setmeal_Dish> setmeal_dishList = setmeal.getSetmeal_dishList();
        if(setmeal_dishList!=null){
            setmeal_dishList.forEach(setmeal_dish -> {
                setmeal_dish.setSetmealId(setmeal.getId());
                setmeal_dishMapper.add(setmeal_dish);
            });
        }
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal)throws Exception{
        if(setmealMapper.update(setmeal)==0)
            throw new Exception("更新失败");
        Query query=new Query();
        query.put("setmealId",setmeal.getId());
        setmeal_dishMapper.deleteByQuery(query);
        List<Setmeal_Dish> setmeal_dishList = setmeal.getSetmeal_dishList();
        if(setmeal_dishList!=null){
            setmeal_dishList.forEach(setmeal_dish -> setmeal_dishMapper.add(setmeal_dish));
        }
    }

    @Override
    @Transactional
    public void updateBatch(List<Setmeal> setmealList) throws Exception {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
        SetmealMapper setmealMapper1 = session.getMapper(SetmealMapper.class);
        if(setmealList!=null){
            setmealList.forEach(setmealMapper1::update);
        }
        session.commit();
        session.close();
    }

    @Override
    @Transactional
    public void delete(Long id)throws Exception {
        Query query = new Query();
        query.put("setmealId",id);
        setmeal_dishMapper.deleteByQuery(query);
        if(setmealMapper.delete(id)==0)
            throw new Exception("找不到套餐");
    }
}
