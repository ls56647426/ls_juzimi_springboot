package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.SentenceSetService;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/sentence_set/PublishSentenceSetByUnameAndUpwdAndSsnAndSsd")
public class PublishSentenceSetByUnameAndUpwdAndSsnAndSsdServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private UserService us;
	
	@Autowired
	private SentenceSetService sss;
	
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
		String sentenceSetName = req.getParameter("sentenceSetName");
		String sentenceSetDescriptions = req.getParameter("sentenceSetDescriptions");
		System.out.println("PublishSentenceSetByUnameAndUpwdAndSsnAndSsdServlet请求结果：" + uname + " " + upassword + " " + sentenceSetName + " " + sentenceSetDescriptions);
		//处理请求信息
		if(!uname.equals("undefined") && !upassword.equals("undefined") && sentenceSetName != null && sentenceSetDescriptions != null) {
			//获取业务层对象
			//处理业务
			User user = this.us.findUserByNameAndByPassword(uname, upassword);
			System.out.println("PublishSentenceSetByUnameAndUpwdAndSsnAndSsdServlet查询结果：user = " + user);
			SentenceSet sentenceSet = new SentenceSet();
			sentenceSet.setUser(user);
			sentenceSet.setName(sentenceSetName);
			sentenceSet.setDescriptions(sentenceSetDescriptions);
			Integer status = this.sss.addSentenceSet(sentenceSet);
			resp.getWriter().write(status.toString());
		} else {
			System.out.println("PublishSentenceSetByUnameAndUpwdAndSsnAndSsdServlet错误提示：uname = null || upassword = null || sentenceSetName = null || sentenceSetDescriptions");
		}
	}
}
