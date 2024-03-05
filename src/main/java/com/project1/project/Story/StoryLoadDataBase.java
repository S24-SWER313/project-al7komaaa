package com.project1.project.Story;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StoryLoadDataBase {

  private static final Logger log = LoggerFactory.getLogger(StoryLoadDataBase.class);

  @Bean
  CommandLineRunner initDatabase4(StoryRepo repository) {
    return args -> {
     

     // log.info("Preloading patients and payments into the database"+patient1.getPayments());

    };
  }
}