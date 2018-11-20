package com.ame.dao;

import com.ame.pojo.User;

import java.util.List;

public interface PhoenixHbaseDao {
	  public List<User> query(String querySql);
	  public void update(String querySql);
	  public void batchUpdate(String updateSQL);
}
