package com.project1.project.Entity.Post;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.PostController;
import com.project1.project.Entity.Comment.Comment;

import jakarta.servlet.http.HttpServletRequest;
@Component
public class PostModelAss implements RepresentationModelAssembler<Post, EntityModel<Post>> {
    HttpServletRequest request;
    @Override
    public EntityModel<Post> toModel(Post post) {
       
       return EntityModel.of(post,
                linkTo(methodOn(PostController.class).postUser(post.getPostId())).withRel("the post owner"),
               linkTo(methodOn(PostController.class).getAllPostLikes(post.getPostId())).withRel("the post's like"),
               linkTo(methodOn(PostController.class).getAllPostComments(post.getPostId(),request)).withRel("the post's comment"));


    }
    


    





}
