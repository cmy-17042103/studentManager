package com.datang.hrb.service.Impl;

import java.util.List;

import com.datang.hrb.dao.UserDao;
import com.datang.hrb.service.LoginService;
import com.datang.hrb.vo.User;

public class LoginServiceImpl implements LoginService {

	private UserDao userDao = new UserDao();

	@Override
	// 实现接口内定义的Login方法
	public User login(User user) {
		// 从数据库中查找数据(用户名对应的密码)进行比对确认是否登录
		// 这里需要从数据库中查找了 就需要使用dao层
		// 调用dao层 UserDao 根据输入的用户名获取对应的密码
		User dbUser = userDao.getUserByUsername(user.getUsername());
		if (dbUser != null && dbUser.getPassword() != null && dbUser.getPassword().equals(user.getPassword())) {
			return user;
		} else {
			return null;
		}
	}

	@Override
	public List<User> getUserList() {

		return userDao.getUserList();
	}

}
