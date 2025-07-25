package com.ideapp.studytrack.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class UserModel extends RepresentationModel<UserModel> {

	private long id;
	private String nombre;
	private String email;
	
}
