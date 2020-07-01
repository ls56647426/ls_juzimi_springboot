package com.ls.juzimi.springboot.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ls.juzimi.springboot.pojo.Review;
import com.ls.juzimi.springboot.pojo.Sentence;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceService;
import com.ls.juzimi.springboot.service.ReviewService;
import com.ls.juzimi.springboot.service.SentenceService;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/user/UserRecentDevelopments")
public class UserRecentDevelopmentsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SentenceService ss;
	
	@Autowired
	private ReviewService rs;
	
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
		String name = req.getParameter("name");
		System.out.println("name = " + name);
		
		//处理请求信息
		if(name != null) {
			//处理业务
			//获取最近10条发布句子
			List<Sentence> sList = this.ss.findSentenceByRecentDevelopments(name);
			//获取最近10条评论
			List<Review> rList = this.rs.findReviewByRecentDevelopments(name);
			//取最近10条动态
			Integer[] type = new Integer[10];
			Integer[] like = new Integer[10];
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			User user = this.us.findUserByName(name);
			for(int i = 0, j = 0, k = 0; i < 10; i++) {
				//没有句子也没有评论了
				if(j == sList.size() && k == rList.size()) break;
				
				//没有句子了，直接添加评论
				if(j == sList.size()) {
					type[i] = 2;		//2.Review
					k++;
					continue;
				}
				
				//没有评论了，直接添加句子
				if(k == rList.size()) {
					type[i] = 1;		//1.Sentence
					like[i] = this.lss.findLikeSentenceByUidAndBySid(user.getId(), sList.get(j).getId());
					j++;
					continue;
				}
				
				//比较句子和评论哪个动态更晚
				try {
					Date d1 = format.parse(sList.get(j).getPublishDate());
					Date d2 = format.parse(rList.get(k).getPublishDate());
					if(d1.compareTo(d2) == 1) {		//取更晚发布的动态
						type[i] = 1;		//1.Sentence
						like[i] = this.lss.findLikeSentenceByUidAndBySid(user.getId(), sList.get(j).getId());
						j++;
					} else {
						type[i] = 2;		//2.Review
						k++;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//响应处理结果
//			Gson gson = new Gson();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			resp.getWriter().write(gson.toJson(type).toString() + "||||||" + gson.toJson(sList).toString()
					+ "||||||" + gson.toJson(rList).toString() + "||||||" + gson.toJson(like).toString());
		} else {
			resp.getWriter().write("UserRecentDevelopmentsServlet错误提示：user = null");
		}
	}
}
