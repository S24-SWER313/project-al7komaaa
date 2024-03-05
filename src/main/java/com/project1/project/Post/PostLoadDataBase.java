package com.project1.project.Post;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PostLoadDataBase {

  private static final Logger log = LoggerFactory.getLogger(PostLoadDataBase.class);

  @Bean
  CommandLineRunner initDatabase2(PostRepo repository) {
    return args -> {
     

     // log.info("Preloading patients and payments into the database"+patient1.getPayments());

    };
  }
}