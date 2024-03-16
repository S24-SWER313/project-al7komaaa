package com.project1.project.Entity.Comment;


import java.lang.reflect.Method;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.Controller;
public class CommentModelAss implements RepresentationModelAssembler<Comment, EntityModel<Comment>> {

    @Override
    public EntityModel<Comment> toModel(Comment comment) {
        return null;
       // EntityModel.of(comment,linkTo(methodOn(Controller.class).allUser()).withSelfRel);
     }

   
    
}