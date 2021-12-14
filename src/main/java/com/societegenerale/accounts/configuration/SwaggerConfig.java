package com.societegenerale.accounts.configuration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .setGroup("public")
                .pathsToMatch("/**")
                .pathsToExclude("/error")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title("BANK ACCOUNT TRANSACTIONS API").description("BANK ACCOUNT TRANSACTIONS APPLICATION"));
    }
}
