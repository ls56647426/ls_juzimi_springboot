package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.LikeSentenceService;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/like/DeleteLikeSentenceByUnameAndUpwdAndSid")
public class DeleteLikeSentenceByUnameAndUpwdAndSidServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		String res3 = req.getParameter("sid");
		System.out.println("DeleteLikeSentenceByUnameAndUpwdAndBySid请求结果：" + uname + " " + upassword + " " + res3);
		//处理请求信息
		if(!uname.equals("undefined") && !upassword.equals("undefined") && res3 != null) {
			//获取业务层对象
			Integer sid = Integer.valueOf(res3);
			System.out.println("uname = " + uname + ", upassword = " + upassword + ", sid = " + sid);
			//处理业务
			User user = this.us.findUserByNameAndByPassword(uname, upassword);
			System.out.println("DeleteLikeSentenceByUnameAndUpwdAndBySid查询结果：user = " + user);
			Integer status = this.lss.delLikeSentenceBySidAndByUid(sid, user.getId());
			System.out.println("DeleteLikeSentenceByUnameAndUpwdAndBySid删除结果：status = " + status);
	
			resp.getWriter().write(status.toString());
		} else {
			System.out.println("DeleteLikeSentenceByUnameAndUpwdAndBySid错误提示：uname = null || upassword = null || sid = null");
		}
	}
}
