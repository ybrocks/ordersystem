package com.beyond.ordersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//ComponentScan에 의해 Application파일 하위 경로의 요소들이 싱글톤객체로 Scan
@SpringBootApplication
//스케쥴러 사용시 필요한 어노테이션
@EnableScheduling
//주로 web서블릿기반 구성요소(@WebServlet)을 스캔, 자동으로 빈으로 등록.
@ServletComponentScan
public class OrderSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderSystemApplication.class, args);
	}
}
