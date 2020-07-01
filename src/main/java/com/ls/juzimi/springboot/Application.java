package com.ls.juzimi.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * SpringBoot启动器
 * @author ls
 *
 */
@SpringBootApplication
@EnableCaching
@ServletComponentScan		//在SpringBoot启动时扫描@WebServlet，并将该类实例化
@MapperScan("com.ls.juzimi.springboot.mapper") //@MapperScan 用户扫描MyBatis的Mapper接口
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

