package com.spring.jwt.poc.springjwtpoc;

import com.spring.jwt.poc.springjwtpoc.models.AuthenticationRequest;
import com.spring.jwt.poc.springjwtpoc.models.AuthenticationResponse;
import com.spring.jwt.poc.springjwtpoc.services.MyUserDetailsService;
import com.spring.jwt.poc.springjwtpoc.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello() {
        return "Hello Welcome to the world of spring security";
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                           authenticationRequest.getPassword()));
       }
       catch (BadCredentialsException e)
       {
           throw new Exception("Invalid credentials",e);
       }
       final UserDetails userDetails = myUserDetailsService.loadUserByUsername
                (authenticationRequest.getUserName());

       final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

