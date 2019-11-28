package com.lwh.service.impl;


import com.lwh.dao.BooksDao;
import com.lwh.pojo.Bookadmin;
import com.lwh.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksDao booksDao;

    //    列出数据
    @Override
    public List<Bookadmin> list(){
        List<Bookadmin> list = booksDao.list();
        return list;
    }

    @Override
    public Bookadmin getBookByBid(int bid){
        Bookadmin bookadmin = booksDao.getBookByBid(bid);
        return bookadmin;
    }

    // 更新数据
    @Override
    public void update(Bookadmin bookadmin){
        booksDao.update(bookadmin);
    }

    // 删除数据
    @Override
    public void deleteBookByBid(int bid){
            booksDao.delete(bid);

    }

    // 删除数据
    @Override
    public void deleteBookByBids(Integer[] bids){
        booksDao.deletes(bids);

    }

    // 新增数据
    @Transactional
    @Override
    public void addBooks(Bookadmin bookadmin){
         booksDao.insert(bookadmin);
    }

    // 新增数据
    @Transactional
    @Override
    public void saveAll(List<Bookadmin> booksList){
        booksDao.saveAll(booksList);
    }

    @Override
    public Integer getTotal(Bookadmin bookadmin) {

        return  booksDao.getTotal(bookadmin);
    }

    @Override
    public List<Bookadmin> getConsumerRecordListPage(Bookadmin bookadmin) {
        return booksDao.getConsumerRecordListPage(bookadmin);
    }

}
