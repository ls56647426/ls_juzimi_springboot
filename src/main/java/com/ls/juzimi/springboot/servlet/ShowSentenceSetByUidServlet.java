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
import com.ls.juzimi.springboot.service.JoinSentenceSetService;
import com.ls.juzimi.springboot.service.SentenceSetService;

/**
 * 
 * @author ls
 *
 */
@WebServlet("/sentence_set/showSentenceSetByUid")
public class ShowSentenceSetByUidServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	SentenceSetService sss;
	
	@Autowired
	JoinSentenceSetService jsss;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//设置请求编码格式
		req.setCharacterEncoding("utf-8");
		//设置相应编码格式
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset = utf-8");
		//获取请求信息
		String res1 = req.getParameter("uid");
		String res2 = req.getParameter("sid");
		System.out.println("ShowSentenceSetByUidServlet请求结果：" + res1 + " " + res2);
		//处理请求信息
		if(res1 != null && res2 != null) {
			//获取业务层对象
			Integer uid = Integer.valueOf(res1);
			Integer sid = Integer.valueOf(res2);
			System.out.println("uid = " + uid + " sid = " + sid);
			//处理业务
			//通过uid找该用户创建的句集
			List<SentenceSet> list = this.sss.findSentenceSetByUid(uid);
			System.out.println("ShowSentenceSetByUidServlet查询结果：" + list);
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
		} else {
			resp.getWriter().write("ShowSentenceSetByUidServlet错误提示：uid = null || sid = null");
		}
	}
}
