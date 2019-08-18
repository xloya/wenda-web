package com.xloya.wenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication()
public class WendaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
	// 继承SpringBootServletInitializer 实现configure 方便打war 外部服务器部署。

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WendaApplication.class);
	}

}
