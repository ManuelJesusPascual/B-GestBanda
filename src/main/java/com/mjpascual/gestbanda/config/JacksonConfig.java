package com.mjpascual.gestbanda.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.util.StdDateFormat;

@Configuration
public class JacksonConfig {

    /**
     * Keeps the date format of the API (2026-11-27T19:40:11.714+00:00). Jackson 3 writes UTC as "Z" by default.
     */
    @Bean
    JsonMapperBuilderCustomizer keepUtcOffsetInDates() {
        return builder -> builder.defaultDateFormat(new StdDateFormat().withZeroOffsetAsZ(false));
    }
}
