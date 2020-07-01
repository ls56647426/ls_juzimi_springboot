/**
 * 
 */
package com.ls.juzimi.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.ls.juzimi.springboot.service.JoinSentenceSetService;

/**
 * @author ls
 *
 */
@WebServlet("/join_sentence_set/JoinSentenceSetBySidAndSsid")
public class JoinSentenceSetBySidAndSsidServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		String res1 = req.getParameter("sid");
		String res2 = req.getParameter("ssid");
		System.out.println("joinSentenceSetBySidAndSsidServlet请求结果：" + res1 + " " + res2);
		//处理请求信息
		if(res1 != null && res2 != null) {
			//获取业务层对象
			Integer sid = Integer.valueOf(res1);
			Integer ssid = Integer.valueOf(res2);
			System.out.println("sid = " + sid + ", ssid = " + ssid);
			//处理业务
			Integer status = this.jsss.add(sid, ssid);
			System.out.println("joinSentenceSetBySidAndSsidServlet添加结果：" + status);
			resp.getWriter().write(status.toString());
		} else {
			System.out.println("joinSentenceSetBySidAndSsidServlet错误提示：sid = null || ssid = null");
			resp.getWriter().write("14");
		}
	}
}
