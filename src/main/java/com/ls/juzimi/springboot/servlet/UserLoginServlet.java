package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/user/UserLogin")
public class UserLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserService us;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置请求编码格式
		req.setCharacterEncoding("utf-8");
		//设置相应编码格式
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset = utf-8");
		//获取请求信息
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		System.out.println("name = " + name + " password = " + password);
		//处理请求信息
		if(name != null && password != null) {
			//处理业务
			//通过name和password查询用户
			User user = this.us.findUserByNameAndByPassword(name, password);
			System.out.println("UserLoginServlet查询结果：" + user);
			//并响应处理结果， 0表示登录成功，其他表示登录失败
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			resp.getWriter().write(gson.toJson(user));
		} else {
			resp.getWriter().write("UserLoginServlet错误提示：user = null || password = null");
		}
	}
}
