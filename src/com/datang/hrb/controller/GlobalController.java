package com.datang.hrb.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.datang.hrb.dao.ConnectionUtil;
import com.datang.hrb.service.LoginService;
import com.datang.hrb.service.Impl.LoginServiceImpl;
import com.datang.hrb.vo.User;

public class GlobalController extends HttpServlet {

	private Map<String, String> userMap = new HashMap<String, String>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("run in doget");
		/*
		 * resp.setContentType("text/html;charset=UTF-8"); PrintWriter pw =
		 * resp.getWriter(); pw.write("<p style='color:red;'>恭喜！访问到后台了"); pw.flush();
		 * pw.close();
		 */
		resp.sendRedirect("ok.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		// System.out.println("run in dopost");
		System.out.println("uri====" + req.getRequestURI());
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf("."));
		if (action.contentEquals("register")) {// 注册
			// userMap.put(username, password);
			// resp.sendRedirect("register_success.jsp");
			Connection conn = ConnectionUtil.getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("insert into user(username,password) values(?,?)");
				ps.setString(1, username);
				ps.setString(2, password);
				int i = ps.executeUpdate();
				if (i == 1) {
					resp.sendRedirect("register_success.jsp");
				} else {
					resp.sendRedirect("register_fail.jsp");
				}
			} catch (SQLException e) {
				resp.sendRedirect("register_fail.jsp");
				e.printStackTrace();
			} finally {
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		else if (action.equals("login")) {// 登录
			LoginService loginService=new LoginServiceImpl();
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			User dbuser=loginService.login(user);
			if (dbuser==null) {
				//登录失败
				resp.sendRedirect("login_fail.jsp");
			} else {
				HttpSession session=req.getSession();
				session.setAttribute("user", dbuser);
				List<User> userList=loginService.getUserList();
				session.setAttribute("userList",userList);
				//登录成功
				resp.sendRedirect("login_success.jsp");
			}
			/*
			 * Connection conn = ConnectionUtil.getConnection(); PreparedStatement ps =
			 * null; // 获取session对象 HttpSession session = req.getSession(); try { ps =
			 * conn.prepareStatement("select * from user where username=?"); ps.setString(1,
			 * username); ResultSet rs = ps.executeQuery(); if (rs != null && rs.next()) {
			 * String dbpwd = rs.getString(2); if (password.equals(dbpwd)) { //
			 * 在登录成功后，使用session存储用户名，以供页面上展示用户名 session.setAttribute("username", username);
			 * PreparedStatement ps_second = null; // 查询所有数据 ps_second =
			 * conn.prepareStatement("select * from user"); ResultSet rsList =
			 * ps_second.executeQuery(); // 定义泛型，存储返回的数据，这里使用了User对数据。进行封装 List<User>
			 * userList = new ArrayList<User>(); // 遍历返回数据并将其存储到userList中 while
			 * (rsList.next()) { // 实例化User User user = new User(); // 向user中赋值
			 * user.setUsername(rsList.getString(1)); user.setAge(rsList.getInt(3)); //
			 * 将user存储到userList userList.add(user); } session.setAttribute("userList",
			 * userList); resp.sendRedirect("login_success.jsp"); } else {
			 * resp.sendRedirect("login_fail.jsp"); } } else {
			 * resp.sendRedirect("login_fail.jsp"); } } catch (SQLException e) {
			 * resp.sendRedirect("login_fail.jsp"); e.printStackTrace(); } finally { if (ps
			 * != null) { try { ps.close(); } catch (SQLException e) {
			 * 
			 * e.printStackTrace(); } } }
			 * 
			 * // 根据用户名获取密码 String existPassword = userMap.get(username); if
			 * (existPassword.equals(null)) {// 密码为空，用户不存在
			 * resp.sendRedirect("login_fail.jsp"); } else { if
			 * (existPassword.equals(password)) {// 密码对比一致
			 * resp.sendRedirect("login_success.jsp"); } else {// 密码错误
			 * resp.sendRedirect("login_fail.jsp"); } } } else {// 错误的请求
			 */
		}
	}
}
