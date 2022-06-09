package com.arch.ihcd.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Single entry point for all swagger configurations.
 *
 */

@Configuration
@EnableSwagger2

public class SwaggerConfig extends WebMvcConfigurationSupport {

	@Value("${application.name}")
	String appName;
	@Value("${application.description}")
	String appDescription;
	@Value("${application.version}")
	private String appVersion;

	@Bean
	public Docket api() {

		ApiInfoBuilder builder = new ApiInfoBuilder().title(appName).description(appDescription).version(appVersion);

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.arch.ihcd.gateway"))
				.paths(PathSelectors.any()).build().apiInfo(builder.build())
				.globalResponseMessage(RequestMethod.GET, getCustomizeResponseMessages());
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	private List<ResponseMessage> getCustomizeResponseMessages(){
		List<ResponseMessage> messages = new ArrayList<>();
		messages.add(new ResponseMessageBuilder().code(500).message("Server has crashed").responseModel(new ModelRef("string")).build());
		messages.add(new ResponseMessageBuilder().code(200).message("Success").responseModel(new ModelRef("string")).build());
		messages.add(new ResponseMessageBuilder().code(201).message("Created").responseModel(new ModelRef("string")).build());
		messages.add(new ResponseMessageBuilder().code(400).message("Invalid Request").responseModel(new ModelRef("string")).build());
		messages.add(new ResponseMessageBuilder().code(401).message("Authorization Failed").responseModel(new ModelRef("string")).build());
		messages.add(new ResponseMessageBuilder().code(403).message("You are not authorized to access this API").responseModel(new ModelRef("string")).build());

		return messages;
	}
}
