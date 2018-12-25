package org.santa.config;

import com.google.common.net.HttpHeaders;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;

@EnableSwagger2
@Profile("local")
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket santaApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .apiInfo(new ApiInfo(
                        "Secret Santa Api",
                        "APIs for managing the Secret Santa application",
                        "1.0",
                        null,
                        null,
                        null,
                        null,
                        EMPTY_LIST))
                .securitySchemes(singletonList(apiKey()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(
                        RequestMethod.GET,
                        asList(
                                new ResponseMessageBuilder()
                                        .code(500)
                                        .message("Internal server error")
                                        .responseModel(new ModelRef("ErrorResponse"))
                                        .build()));
    }

    private ApiKey apiKey() {
        return new ApiKey("jwt",
                HttpHeaders.AUTHORIZATION,
                In.HEADER.name());
    }
}
