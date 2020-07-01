package com.ls.juzimi.springboot.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理类
 */
@Configuration
public class GlobalException implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv = new ModelAndView();

		// 传递key
		mv.addObject("error", ex.toString());
		mv.addObject("url", request.getRequestURL());

		//异常视图跳转
		mv.setViewName("error");

		return mv;
	}
}