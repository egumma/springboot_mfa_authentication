package com.arch.ihcd.namingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class IhcdEurekaNamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IhcdEurekaNamingServerApplication.class, args);
	}

}
