package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/user/UserRegister")
public class UserRegisterServlet extends HttpServlet {

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
				String nickname = req.getParameter("nickname");
				String profile = req.getParameter("profile");
				String sex = req.getParameter("sex");
				String borthday = req.getParameter("borthday");
				String mobile = req.getParameter("mobile");
				
				System.out.println("name = " + name + " password = " + password + " nickname = " + nickname + " profile = " + profile + " sex = " + sex + " borthday = " + borthday + " mobile = " + mobile);
				//处理请求信息
				if(name != null && password != null && nickname != null && mobile != null) {
					//处理业务
					//通过name和password查询用户
					User user = new User();
					user.setName(name);
					user.setPassword(password);
					user.setNickname(nickname);
					user.setProfile(profile);
					user.setSex(sex);
					user.setBorthday(borthday);
					user.setMobile(mobile);
					Integer status = this.us.addUser(user);
					System.out.println("UserRegister注册结果：" + status);
					//并响应处理结果， 0表示登录成功，其他表示登录失败
					resp.getWriter().write(status.toString());
				} else {
					resp.getWriter().write("UserRegister错误提示：user = null || password = null");
				}
	}
}
