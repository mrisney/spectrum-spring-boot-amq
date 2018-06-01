package com.spectrum.threescale.poc;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	@EnableSwagger2
	class SwaggerConfig {
		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2).select()
					.apis(RequestHandlerSelectors.basePackage("com.spectrum.threescale.poc")).paths(PathSelectors.any())
					.build().apiInfo(apiInfo());
		}

		private ApiInfo apiInfo() {
			return new ApiInfo("Spectrum Health, simple proof of concept service REST API",
					"Simple REST Endpoints, written for demonstration at Spectrum Health, allows user to send and retrieve messages from ActiveMQ Queues.",
					"1.0.0", "Terms of service",
					new Contact("Marc Risney", "https://www.redhat.com", "mrisney@redhat.com"), "License of API",
					"https://opensource.org/licenses/MIT", Collections.emptyList());
		}
	}
}
