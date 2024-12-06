package com.in28minutes.rest.webservices.restful_web_services.jpa;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.in28minutes.rest.webservices.restful_web_services.PostRepository;
import com.in28minutes.rest.webservices.restful_web_services.UserRepository;
import com.in28minutes.rest.webservices.restful_web_services.user.Post;
import com.in28minutes.rest.webservices.restful_web_services.user.User;
import com.in28minutes.rest.webservices.restful_web_services.user.UserNotFoundException;

import jakarta.validation.Valid;



@RestController
public class UserJpaResource {
	
	private UserRepository repository;
	
	private PostRepository postRepository;
	
	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		
		this.repository = repository;
		this.postRepository = postRepository;
	}

// to retrieve all the users
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers(){
		return repository.findAll();
	}

// to retrieve a specific user with given user id
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveOneUser(@PathVariable int id){
		 Optional<User> user = repository.findById(id);
		 
		 if(user.isEmpty()) {
			 throw new UserNotFoundException("id : " + id);
		 }
		 
		 EntityModel<User> entityModel = EntityModel.of(user.get());
		 
		 WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		 entityModel.add(link.withRel("all-users"));
		 return entityModel; 
	}

// to create a user and save it to the the H2 database
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		User savedUser = repository.save(user);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
// to DELETE a specific user with given user id
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id){
			repository.deleteById(id);
		 
		 
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> findPostForUser(@PathVariable int id){
		Optional<User> user = repository.findById(id);
		 
		 if(user.isEmpty()) {
			 throw new UserNotFoundException("id : " + id);
		 }
		 
		 return user.get().getPosts();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int id,@Valid @RequestBody Post post){
		Optional<User> user = repository.findById(id);
		 
		 if(user.isEmpty()) {
			 throw new UserNotFoundException("id : " + id);
		 }
		 
		 post.setUser(user.get());
		 
		 Post savedPost = postRepository.save(post);
		 
		 URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(savedPost.getId())
					.toUri();
			return ResponseEntity.created(location).build();
		
		
	}
	
	@GetMapping("/jpa/users/{id}/posts/{post_id}")
	public Post findPostForUserWithId(@PathVariable int id,@PathVariable int post_id){
		Optional<User> user = repository.findById(id);
		 
		 if(user.isEmpty()) {
			 throw new UserNotFoundException("id : " + id);
		 }
		 
		 Optional<Post> postsForGivenId = postRepository.findById(post_id);
		 
		 System.out.print(postsForGivenId.get());
		 return postsForGivenId.get();
		 
	}
	
	
}
