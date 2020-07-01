package com.ls.juzimi.springboot.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ls.juzimi.springboot.pojo.SentenceSet;
import com.ls.juzimi.springboot.pojo.User;
import com.ls.juzimi.springboot.service.JoinSentenceSetService;
import com.ls.juzimi.springboot.service.SentenceSetService;
import com.ls.juzimi.springboot.service.UserService;

@WebServlet("/sentence_set/ShowSentenceSetByUnameAndUpwdAndSid")
public class ShowSentenceSetByUnameAndUpwdAndSidServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private SentenceSetService sss;
	
	@Autowired
	private JoinSentenceSetService jsss;

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
		System.out.println("ShowSentenceSetByUnameAndByUpwdAndBySidServlet请求结果：" + uname + " " + upassword + " " + res3);
		//处理请求信息
		if(res3 == null) {
			System.out.println("ShowSentenceSetByUnameAndByUpwdAndBySidServlet错误提示：sid = null");
		} else if(uname.equals("undefined") || upassword.equals("undefined")) {
			System.out.println("ShowSentenceSetByUnameAndByUpwdAndBySidServlet错误提示：uname = null, upassword = null");
			resp.getWriter().write("-1");		//这里的-1表示尚未登录
		} else {
			//获取业务层对象
			Integer sid = Integer.valueOf(res3);
			System.out.println("uname = " + uname + ", upassword = " + upassword + ", sid = " + sid);
			//处理业务
			User user = this.us.findUserByNameAndByPassword(uname, upassword);
			System.out.println("ShowSentenceSetByUnameAndByUpwdAndBySidServlet查询结果：user = " + user);
			//通过uid找该用户创建的句集
			List<SentenceSet> list = this.sss.findSentenceSetByUid(user.getId());
			System.out.println("ShowSentenceSetByUnameAndByUpwdAndBySidServlet查询结果：" + list);
			//通过sid判断该句子是否在各个句集内
			Map<Integer, Integer> exists = new HashMap<Integer, Integer>();
			for(SentenceSet sentenceSet : list) {
				Integer k = sentenceSet.getId();
				Integer v = this.jsss.find(sid, k) != null ? 1 : 0;
				exists.put(k, v);
			}
			//响应处理结果
//			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			//多个数据用"||||||"隔开一起传回
			resp.getWriter().write(gson.toJson(list).toString() + "||||||" + gson.toJson(exists).toString());
		}
	}
}
