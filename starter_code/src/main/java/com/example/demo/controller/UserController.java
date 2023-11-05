package com.example.demo.controller;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;import org.springframework.web.bind.annotation.*;
import com.example.demo.model.requests.CreateUserRequest;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;
	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if (createUserRequest.getPassword().length() < 7
				|| !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			ResponseEntity<User> response = ResponseEntity.badRequest().build();
			log.info("Create user failure!", response); // Log create user fail
			return response; // Api response
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword())); // Set password
		userRepository.save(user); // Save user
		ResponseEntity<User> response = ResponseEntity.ok(user);
		log.info("Create user successful.", response); // Log create successful
		return response; // Api response
	} //
}