package org.atatame.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("realproject")
                        .version("1.0")
                        .description("RP接口文档")
                        .contact(new Contact().name("atatame").email("10086@gmail.com")));
    }

    @Bean
    public GroupedOpenApi loginApi(){
         return GroupedOpenApi.builder().group("api").pathsToMatch("/api/**").build();
    }

}
