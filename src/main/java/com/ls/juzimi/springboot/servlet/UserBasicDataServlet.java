package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.ls.juzimi.springboot.service.LikeSentenceService;
import com.ls.juzimi.springboot.service.LikeSentenceSetService;
import com.ls.juzimi.springboot.service.SentenceService;

@WebServlet("/user/UserBasicData")
public class UserBasicDataServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private LikeSentenceService lss;
	
	@Autowired
	private SentenceService ss;
	
	@Autowired
	private LikeSentenceSetService lsss;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置请求编码格式
		req.setCharacterEncoding("utf-8");
		//设置相应编码格式
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset = utf-8");
		//获取请求信息
		String name = req.getParameter("name");
		System.out.println("name = " + name);
		//处理请求信息
		if(name != null) {
			//处理业务
			//关注、粉丝、句库、原创、句集、留言
			Integer[] userBasicData = new Integer[6];
			userBasicData[0] = 0;
			userBasicData[1] = 0;
			userBasicData[2] = this.lss.findLikeSentenceNumbersByUser(name);
			userBasicData[3] = this.ss.findSentenceNumbersByOriginal(name);
			userBasicData[4] = this.lsss.findLikeSentenceSetNumbersByUser(name);
			userBasicData[5] = 0;
			System.out.println("UserBasicDataServlet查询结果：关注：" + userBasicData[0] + "，粉丝：" + userBasicData[1] +
					"，句库：" + userBasicData[2] + "，原创：" + userBasicData[3] + "，句集：" + userBasicData[4] +
					"，留言：" + userBasicData[5]);
			//并响应处理结果， 0表示登录成功，其他表示登录失败
			Gson gson = new Gson();
//			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			resp.getWriter().write(gson.toJson(userBasicData).toString());
		} else {
			resp.getWriter().write("UserBasicDataServlet错误提示：user = null");
		}
	}
}
