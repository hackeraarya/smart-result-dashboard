package com.smartresult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartResultDashboardApplication {
  public static void main(String[] args) {
    SpringApplication.run(SmartResultDashboardApplication.class, args);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyListener() {
    return event -> {
      WebServer webServer = ((WebServerApplicationContext) event.getApplicationContext()).getWebServer();
      int port = webServer.getPort();
      String baseUrl = "http://localhost:" + port + "/";
      String h2Url = "http://localhost:" + port + "/h2-console";
      System.out.println("Application started at: " + baseUrl);
      System.out.println("H2 console available at: " + h2Url);
    };
  }
}

