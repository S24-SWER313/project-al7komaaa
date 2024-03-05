package com.project1.project.Like;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LikeLoadDataBase {

  private static final Logger log = LoggerFactory.getLogger(LikeLoadDataBase.class);

  @Bean
  CommandLineRunner initDatabase1(LikeRepo repository) {
    return args -> {
     

     // log.info("Preloading patients and payments into the database"+patient1.getPayments());

    };
  }
}