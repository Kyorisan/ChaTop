package com.chatop.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class WebConfig implements WebMvcConfigurer {

  private final AppProperties appProperties;

  public WebConfig(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String location = Path.of(appProperties.uploadDir()).toAbsolutePath().normalize().toUri().toString();
    if (!location.endsWith("/")) {
      location += "/";
    }
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}
