package com.project1.project;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.project1.project.Comment.Comment;
import com.project1.project.Comment.CommentRepo;
import com.project1.project.Like.Like;
import com.project1.project.Like.LikeRepo;
import com.project1.project.Like.likeType;
import com.project1.project.Post.Post;
import com.project1.project.Post.PostRepo;
import com.project1.project.Post.Type;
import com.project1.project.Share.Share;
import com.project1.project.Share.ShareRepo;
import com.project1.project.User.Gender;
import com.project1.project.User.User;
import com.project1.project.User.UserRepo;

import java.time.LocalDate;

import org.slf4j.Logger;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepo userRepository, PostRepo postRepository,
                                    CommentRepo commentRepository, LikeRepo likeRepository,
                                    ShareRepo shareRepository) {
        return args -> {
            // Create and persist users
            // User mary = new User();
            User mary = new User("mary", "hamad", "123456789", "john@example.com", Gender.MALE,LocalDate.of(2003, 4, 2));
            userRepository.save(mary);
            // User jad = new User();
            User jad = new User("Jad", "Smith", "987654321", "jane@example.com", Gender.FEMAL, LocalDate.of(2003, 5,31));
            userRepository.save(jad);

            // Create and persist posts
            Post postMary = new Post("post1", mary);
                        // Post postMary = new Post((long) 1, Type.TEXT);


            postRepository.save(postMary);
            // Post postJad = new Post((long) 2, Type.IMAGE);

            Post postJad = new Post( "post2", jad);
            postRepository.save(postJad);

            // Create and persist comments
            Comment commentMary = new Comment("coment 1", mary, postMary);
            commentRepository.save(commentMary);

            Comment commentJad = new Comment("coment2", jad, postMary);
            commentRepository.save(commentJad);

            // Create and persist likes
            Like likeMary = new Like(likeType.LIKE, postMary, mary);
            likeRepository.save(likeMary);

            Like likeJad = new Like(likeType.LOVE, postMary,commentMary, jad);
            likeRepository.save(likeJad);

            // Create and persist shares
            Share shareJad = new Share("Content of share 1", jad, postMary);
            shareRepository.save(shareJad);

            Share shareMary = new Share("Content of share 2", mary, postJad);
            shareRepository.save(shareMary);

            log.info("Preloading data into the database: " + mary + ", " + jad +
                    ", " + postMary + ", " + postJad + ", " + commentMary + ", " + commentJad +
                    ", " + likeMary + ", " + likeJad + ", " + shareJad + ", " + shareMary);
        };
    }
}
