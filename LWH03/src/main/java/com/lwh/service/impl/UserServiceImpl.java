package com.lwh.service.impl;

import com.lwh.dao.UserDao;
import com.lwh.pojo.User;
import com.lwh.service.UserService;

import com.lwh.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getLogin(String username, String password) {
        String md5Pwd = MD5Util.MD5EncodeUtf8(password);
        //将MD5密码进行匹配
        User user=userDao.login(username,md5Pwd);

        return user;
    }


    @Override
    public User register(User user) {

        String md5Pwd= MD5Util.MD5EncodeUtf8(user.getPassword());
        //将md5密码设置进去
        user.setPassword(md5Pwd);
        int insert = userDao.insert(user);

        return user;
    }

}
