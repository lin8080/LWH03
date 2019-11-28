package com.lwh.service;

import com.lwh.pojo.Bookadmin;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface BooksService {

    List<Bookadmin> list();

    Bookadmin getBookByBid(int bid);

    void update(Bookadmin bookadmin);

    void deleteBookByBid(int bid);

    void deleteBookByBids(Integer[] bids);

    void addBooks(Bookadmin bookadmin);

    void saveAll(List<Bookadmin> booksList);

    /**
     * 获取记录条数
     *
     * @param
     * @return
     */
    Integer getTotal(Bookadmin bookadmin);

    /**
     * 分页查询集合
     *
     * @param
     * @return
     */
    List<Bookadmin> getConsumerRecordListPage(Bookadmin bookadmin);


}
