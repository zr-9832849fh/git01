package com.msb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.xml.stream.events.StartDocument;

@SpringBootApplication
@MapperScan("com.msb.mappers")
public class SpingBootCrmApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SpingBootCrmApplication.class, args);
	}

	/*设置web项目的启动入口*/
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpingBootCrmApplication.class);
	}
}
