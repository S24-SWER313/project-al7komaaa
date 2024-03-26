package com.project1.project.Entity.Comment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.project1.project.Controllers.Controller;
import com.project1.project.Controllers.PostController;
import com.project1.project.Entity.User.User;
import com.project1.project.Entity.User.UserRepo;
import com.project1.project.Security.Jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommentModelAss implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepo userRepo;

    @Override
    public EntityModel<Comment> toModel(Comment comment) {
        return null;
        // EntityModel.of(comment,linkTo(methodOn(Controller.class).allUser()).withSelfRel);
    }

    public EntityModel<Comment> commentDelEdit(Comment comment, HttpServletRequest request) {
        User user = null;
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            user = userRepo.findByUsername(username).get();
        }
        if (comment.user.getId() == user.getId()) {
            return EntityModel.of(comment,
                    linkTo(methodOn(PostController.class).deleteComment(comment.getCommentId()))
                            .withRel("delete comment"),

                    linkTo(methodOn(PostController.class).editCoumment(comment, request))
                            .withRel("edit your comment "));
                            //  linkTo(methodOn(PostController.class).findById(comment.getPost().getPostId()))
                            // .withRel("go to the post "));
        } else {
            return EntityModel.of(comment,
                  
                    linkTo(methodOn(PostController.class).findById(comment.getPost().getPostId()))
                            .withRel("go to the post "));

        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;

    }

}
