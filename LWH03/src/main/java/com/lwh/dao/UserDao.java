package com.lwh.dao;

import com.lwh.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
	
	 User login(@Param("username") String username, @Param("password") String password);

	/**
	 * 通过用户名查找用户
	 * @param username
	 * @return
	 */
	 int selectCountByUsername(String username);

	 int insert(User record);


	

}