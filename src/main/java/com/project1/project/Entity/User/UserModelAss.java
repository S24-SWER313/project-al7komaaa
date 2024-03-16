package com.project1.project.Entity.User;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.project1.project.Controllers.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAss implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
    
              return EntityModel.of(user,
                linkTo(methodOn(Controller.class).getUserById(user.getId())).withSelfRel());
              //  linkTo(methodOn(Controller.class).addFriend(new Long(1),user.getId())).withRel("Add Friend"));

    }
    
}
