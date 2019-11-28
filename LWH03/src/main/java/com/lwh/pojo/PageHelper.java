package com.lwh.pojo;

import java.util.ArrayList;
import java.util.List;

public class PageHelper<T> {
    // 注意：这两个属性名称不能改变，是定死的
    // 实体类集合
    private List<T> rows = new ArrayList<T>();
    // 数据总条数
    private int total;
    // setter and getter...


    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
