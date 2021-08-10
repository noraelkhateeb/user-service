package com.service.user.services;

import java.nio.CharBuffer;


import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.service.user.dto.CredentialsDto;
import com.service.user.dto.UserDto;
import com.service.user.entities.DAOUser;
import com.service.user.exceptions.AppException;
import com.service.user.mappers.UserMapper;
import com.service.user.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
	private  UserRepository userRepository;
    
    @Autowired
    private  PasswordEncoder passwordEncoder;
    private  UserMapper userMapper;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    
    @Value("${jwt.expirationDateInMs}")
    private int expPeriod;
    
    @Value("${jwt.refreshExpirationDateInMs}")
    private Long refreshExpirationDateInMs;


    @PostConstruct
    protected void init() {
         secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UserDto signIn(CredentialsDto credentialsDto) {
    	DAOUser user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
             return userMapper.toUserDto(user, createToken(user),GenerateRefreshToken(user));
         }

        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }
    
    public String refresh(String refreshToken)  {
    	
    	String newToken=null;
    	try {
    		
    		if(validateToken(refreshToken)) {
    			
    			newToken=Jwts.builder().setSubject(getLoginNameFromToken(refreshToken)).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+ expPeriod))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
    		
    	}
    		return newToken;
    		
    	}catch(Exception ex){
            throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);

    	}
    }


    public boolean validateToken(String token) throws Exception{
    	
    try {
        String login =getLoginNameFromToken(token)  ;
        Optional<DAOUser> userOptional = userRepository.findByLogin(login);

        if (userOptional == null) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        DAOUser user = userOptional.get();
        return true;

        
     
	 } catch (Exception ex) {
		  throw ex;
	 }
    }

    private String createToken(DAOUser user) {
        Claims claims = Jwts.claims().setSubject(user.getLogin());

        Date now = new Date();
        Date validity = new Date(now.getTime() + expPeriod); // 1 hour

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    
    public Boolean saveUser(CredentialsDto user) {
    	DAOUser newUser = new DAOUser();
    	System.out.println("password "+user.getPassword()+"loginname "+user.getLogin());
		newUser.setLogin(user.getLogin());
    	System.out.println("password "+passwordEncoder.encode(user.getPassword()));

		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    	
    	userRepository.save(newUser);
    	
    	return true;
    	
    	
    }
    
    public String getLoginNameFromToken(String Token) {

		return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(Token)
                .getBody()
                .getSubject();

	}
    
    public String GenerateRefreshToken(DAOUser user) {
    	
    	return Jwts.builder().setSubject(user.getLogin()).setIssuedAt(new Date())
    	.setExpiration(new Date(new Date().getTime()+ refreshExpirationDateInMs))
    	.signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }
    
}
