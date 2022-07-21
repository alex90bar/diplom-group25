package ru.skillbox.diplom.group25.microservice.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * NewClass
 *
 * @author alex90bar
 */

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class}) //отключение Security через exclude
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
