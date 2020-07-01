package com.ls.juzimi.springboot.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceService;
import com.ls.juzimi.springboot.service.SentenceService;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/sentence/ShowSentenceSortByPublishDate")
public class ShowSentenceSortByPublishDateServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private SentenceService ss;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private LikeSentenceService lss;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置请求编码格式
		req.setCharacterEncoding("utf-8");
		//设置相应编码格式
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset = utf-8");
		//获取请求信息
		String uname = req.getParameter("uname");
		String upassword = req.getParameter("upassword");
		String res3 = req.getParameter("start");
		System.out.println("ShowSentenceSortByPublishDate请求结果：" + uname + " " + upassword);
		//处理请求信息
		if(res3 != null) {
			//获取业务层对象
			Integer start = Integer.valueOf(res3);
			System.out.println("uname = " + uname + ", upassword = " + upassword + ", start = " + start);
			//处理业务
			Page<Sentence> page = this.ss.findSentenceAllPageAndSort(start, 20, Direction.DESC, "publishDate");
			List<Sentence> list = page.getContent();
			
			User user = null;
			Integer[] like = new Integer[list.size()];
			if(!uname.equals("undefined") && !upassword.equals("undefined")) {
				user = this.us.findUserByNameAndByPassword(uname, upassword);
				
				for(int i = 0; i < list.size(); i++) {
					like[i] = this.lss.findLikeSentenceByUidAndBySid(user.getId(), list.get(i).getId());
				}
			}
			
			Integer totalPages = page.getTotalPages();
			//响应处理结果
//			Gson gson = new Gson();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			resp.getWriter().write(gson.toJson(list).toString() + "||||||" + gson.toJson(like).toString() + "||||||" + totalPages.toString());
		} else {
			System.out.println("ShowSentenceSortByPublishDate错误提示：res3 = null");
		}
	}
	
}
