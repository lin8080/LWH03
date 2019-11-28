package com.lwh.service;

import com.lwh.pojo.User;

public interface UserService {

     User getLogin(String username,String password);

     User register(User user);

}
