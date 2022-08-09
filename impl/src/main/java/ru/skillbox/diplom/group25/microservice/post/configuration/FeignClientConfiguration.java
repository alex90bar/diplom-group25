package ru.skillbox.diplom.group25.microservice.post.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * FeignClientConfiguration
 *
 * @author alex90bar
 */
//TODO Можно убрать после реализации jwt token
public class FeignClientConfiguration {

  @Value("${rest.password}")
  private String pass;

  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor("user", pass);
  }
}


