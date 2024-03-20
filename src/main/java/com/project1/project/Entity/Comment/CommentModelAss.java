package com.project1.project.Entity.Comment;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Method;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.Controller;
import com.project1.project.Controllers.PostController;


import jakarta.servlet.http.HttpServletRequest;
@Component
public class CommentModelAss implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {

    @Override
    public EntityModel<Comment> toModel(Comment comment) {
        return null;
       // EntityModel.of(comment,linkTo(methodOn(Controller.class).allUser()).withSelfRel);
    }
public EntityModel<Comment> commentDelEdit(Comment comment, HttpServletRequest request) {
       
        return EntityModel.of(comment,
               linkTo(methodOn(PostController.class).deleteComment(comment.getCommentId(), request)).withRel("the post's like"));
              //  linkTo(methodOn(PostController.class).getAllPostComments(comment.getCommentId())).withRel("the post's comment")); ne3mal edit elha
 
 
     }




     }

   
    
