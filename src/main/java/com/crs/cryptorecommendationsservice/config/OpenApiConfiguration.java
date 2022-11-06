package com.crs.cryptorecommendationsservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * Sets the common "info" section in the Open API spec and the default media type for the whole app
 */
@OpenAPIDefinition(info = @Info(
        title = "Crypto recommendations service",
        description = "A service to provide cryptocurrency recommendations to invest into",
        version = "V0.0.1"))
@Configuration
public class OpenApiConfiguration {

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor(SpringDocConfigProperties springDocConfigProperties) {
        return beanFactory -> springDocConfigProperties.setDefaultProducesMediaType(MediaType.APPLICATION_JSON_VALUE);
    }
}
