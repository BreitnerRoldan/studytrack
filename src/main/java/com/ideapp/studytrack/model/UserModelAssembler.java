package com.ideapp.studytrack.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Map;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ideapp.studytrack.controller.UserController;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, UserModel> {

	//implementa HateOAS
	@Override
	public UserModel toModel(User user) {

		UserModel model = new UserModel();
		model.setId(user.getId());
		model.setNombre(user.getName());
		model.setEmail(user.getEmail());
		
		model.add(linkTo(methodOn(UserController.class).register(user)).withRel("register"));
		model.add(linkTo(methodOn(UserController.class).login(null)).withRel("login"));
		model.add(linkTo(methodOn(UserController.class).forgotPassword(Map.of("email", user.getEmail()))).withRel("forgot-password"));
		
		return model;
	}

}
