package com.in28minutes.rest.webservices.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;



@RestController
public class UserResource {
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service = service;
	}

// to retrieve all the users
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return service.findAll();
	}

// to retrieve a specific user with given user id
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveOneUser(@PathVariable int id){
		 User user = service.findOne(id);
		 
		 if(user==null) {
			 throw new UserNotFoundException("id : " + id);
		 }
		 
		 EntityModel<User> entityModel = EntityModel.of(user);
		 
		 WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		 entityModel.add(link.withRel("all-users"));
		 return entityModel; 
	}

// to create a user to the static list
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		User savedUser = service.save(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
// to DELETE a specific user with given user id
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id){
			service.deleteById(id);
		 
		 
	}
	
	
}
