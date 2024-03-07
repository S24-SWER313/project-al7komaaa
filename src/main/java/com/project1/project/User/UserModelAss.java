package com.project1.project.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.project1.project.Controller;

@Component
public class UserModelAss implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
              return EntityModel.of(user,
                linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel());
    }
    
}
