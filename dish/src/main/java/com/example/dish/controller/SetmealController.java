package com.example.dish.controller;

import com.example.dish.common.PageUtils;
import com.example.dish.common.Query;
import com.example.dish.common.Result;
import com.example.dish.entity.Setmeal;
import com.example.dish.services.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @GetMapping("/setmeals")
    public PageUtils all(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        List<Setmeal> setmealList = setmealService.all(query);
        int count = setmealService.count(query);
        return new PageUtils(setmealList,count, query.getPageSize());
    }
    @GetMapping("/setmeals/{id}")
    public Result<Setmeal> get(@PathVariable Long id) throws Exception {
        return Result.success("查询成功",setmealService.get(id));
    }
    @PostMapping("/setmeals")
    public Result<String> add(@RequestBody Setmeal setmeal) throws Exception {
        setmealService.add(setmeal);
        return Result.success("添加成功");
    }
    @PutMapping("/setmeals")
    public Result<String> update(@RequestBody Setmeal setmeal) throws Exception {
        setmealService.update(setmeal);
        return Result.success("更新成功");
    }
    @PutMapping("/setmealsBatch")
    public Result<String> updateSetmealBatch(@RequestBody List<Setmeal> setmealList)throws Exception{
        setmealService.updateBatch(setmealList);
        return Result.success("批量更新成功");
    }
    @DeleteMapping("/setmeals/{id}")
    public Result<String> delete(@PathVariable Long id) throws Exception {
        setmealService.delete(id);
        return Result.success("删除成功");
    }
    @DeleteMapping("/setmeals")
    public Result<String> deleteBatch(@RequestParam List<Long> ids) throws Exception {
        setmealService.deleteBatch(ids);
        return Result.success("删除成功");
    }
}
