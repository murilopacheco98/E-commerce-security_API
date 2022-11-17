package com.ecommerce.educative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.ecommerce.educative.security.RsaKeyProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableSwagger2
public class EducativeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EducativeApplication.class, args);
	}

}
