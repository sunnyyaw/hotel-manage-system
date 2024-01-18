package com.example.dish.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
@Data
public class PageUtils{
    private int count;
    private int totalPage;
    private List<?> data;
    public PageUtils(List<?> data,int count,int pageSize){
        this.data = data;
        this.count = count;
        if(pageSize != 0)
            this.totalPage = count/pageSize + (count%pageSize!=0 ? 1 : 0);
    }
}
