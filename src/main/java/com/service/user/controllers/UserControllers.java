package com.service.user.controllers;

import com.service.user.dto.CredentialsDto;
import com.service.user.dto.UserDto;
import com.service.user.services.UserService;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserControllers {
	
	 

	@Autowired
    private  UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<UserDto> signIn(@RequestBody CredentialsDto credentialsDto) {
        //logger.info("Trying to login {}", credentialsDto.getLogin());
        return ResponseEntity.ok(userService.signIn(credentialsDto));
    }
    
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        //logger.info("Trying to login {}", credentialsDto.getLogin());
        return ResponseEntity.ok("Access_Token= " + userService.refresh(refreshToken));
    }
    
    

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam String token)  {
        //log.info("Trying to validate token {}", token);
    	try {
          return ResponseEntity.ok(userService.validateToken(token));
          
    	}catch(Exception ex){
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    	}
    }
    
    @PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody CredentialsDto user) throws Exception {
		return ResponseEntity.ok(userService.saveUser(user));
	}
    
}
