package com.efms.employee_file_ms_be.config;

import com.efms.employee_file_ms_be.controller.Constants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API of EMPLOYEE FILE MS")
                        .version("1.0.0")
                        .description("Employee File MS BE Documentation")
                );
    }

    @Bean
    public GroupedOpenApi customGroupedOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Employee - FMS")
                .addOperationCustomizer(((operation, handlerMethod) -> {
                   operation.addParametersItem(
                           new HeaderParameter()
                                   .name("X-COMPANY-ID")
                                   .description("X-COMPANY-ID")
                                   .required(true)
                   );
                   return operation;
                }))
                .pathsToExclude(Constants.Path.COMPANY_PATH + "/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicGroupedOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Company - FMS")
                .pathsToMatch(Constants.Path.COMPANY_PATH + "/**")
                .build();
    }
}
