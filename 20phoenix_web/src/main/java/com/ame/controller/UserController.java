package com.ame.controller;

import com.ame.pojo.User;
import com.ame.service.HbaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {
	
	@Autowired
	private HbaseService hbaseService;
	
	@RequestMapping("/userList")
	@ResponseBody
	public List<User> showUserList(HttpServletRequest request, HttpServletResponse response,User user){
		List<User> userList = hbaseService.queryUserByCondition(user);
		for (User user1 : userList) {
			System.out.println(user1.getUser_name());
		}
		return userList;
	}
}
