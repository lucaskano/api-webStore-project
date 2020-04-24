package br.com.webstore.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Project: dev-springboot
 *
 * @author : Lucas Kanô de Oliveira (lucaskano)
 * @since : 24/04/2020
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apiDoc(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("br.com.lucaskano.endpoint"))
                    .paths(regex("/v1.*"))
                    .build()
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                .name("Authorization")
                .description("Bearer token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build()))
                .apiInfo(metaData());
    }

    private ApiInfo metaData(){
        return new ApiInfoBuilder()
                .title("API Rest for Webstore")
                .description("Api developed with the objective of simulating a Webstore")
                .version("1.0")
                .contact(new Contact("Lucas Kanô","https://www.linkedin.com/in/lucas-kanô-de-oliveira-262425123/","lucaskano@gmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/license/LICENSE-2.0")
                .build();
    }
}
