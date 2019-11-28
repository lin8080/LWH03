package com.lwh.dao;

import com.lwh.pojo.Bookadmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BooksDao {

    List<Bookadmin> list();

    Bookadmin getBookByBid(Integer bid);

    void update(Bookadmin bookadmin);

    void delete(int bid);

    void deletes(Integer[] bids);

    void insert(Bookadmin bookadmin);

    void saveAll(List<Bookadmin> booksList);

    Integer getTotal(Bookadmin bookadmin);

    List<Bookadmin> getConsumerRecordListPage(Bookadmin bookadmin);

}
