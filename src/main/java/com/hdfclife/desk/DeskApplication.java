package com.hdfclife.desk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hdfclife.desk.config.HdfcProperties;

@SpringBootApplication
@EnableConfigurationProperties(HdfcProperties.class)
public class DeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeskApplication.class, args);
	}
}
