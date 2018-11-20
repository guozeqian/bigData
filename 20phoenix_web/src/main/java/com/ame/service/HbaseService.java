package com.ame.service;

import com.ame.pojo.User;

import java.util.List;

public interface HbaseService {
		
	/**
	 * 通过userId查询用户
	 * @param UserId
	 * @return List<User>
	 */
	public User queryUserByUserId(String UserId);
	
	/**
	 * 根据条件查询用户
	 * @return List<User>
	 */
	public List<User> queryUserByCondition(User user);
}
