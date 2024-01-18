package com.example.dish.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.Map;
@EqualsAndHashCode(callSuper = true)
@Data
public class Query extends LinkedHashMap<String,Object> {
    private int pageSize;
    private int currentPage;
    public Query(){}

    public Query(Map<String,Object> params){
        this.putAll(params);
        String currentPageStr = (String) params.get("currentPage");
        String pageSizeStr = (String) params.get("pageSize");
        if(currentPageStr!=null && pageSizeStr!=null){
            this.currentPage = Integer.parseUnsignedInt(currentPageStr);
            this.pageSize = Integer.parseUnsignedInt(pageSizeStr);
            this.put("current", (currentPage - 1) * pageSize);
            this.put("pageSize",pageSize);
        }
    }
}
