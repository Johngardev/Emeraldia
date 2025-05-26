package com.emeraldia.backend.controller;

import com.emeraldia.backend.model.EmployeeRole;
import com.emeraldia.backend.model.Role;
import com.emeraldia.backend.model.User;
import com.emeraldia.backend.payload.request.LoginRequest;
import com.emeraldia.backend.payload.request.SignupRequest;
import com.emeraldia.backend.payload.response.JwtResponse;
import com.emeraldia.backend.payload.response.MessageResponse;
import com.emeraldia.backend.repository.RoleRepository;
import com.emeraldia.backend.repository.UserRepository;
import com.emeraldia.backend.security.jwt.JwtUtils;
import com.emeraldia.backend.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  /**
   * Authenticate user and return a JWT token if successful.
   *
   * @param loginRequest The login request containing username and password.
   * @return A ResponseEntity containing the JWT response or an error message.
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    // Authenticate the user with the provided username and password
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));

    // Set the authentication in the security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Generate JWT token based on the authentication
    String jwt = jwtUtils.generateJwtToken(authentication);

    // Get user details from the authentication object
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Extract user roles into a list
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    // Return a response containing the JWT and user details
    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  /**
   * Register a new user account.
   *
   * @param signUpRequest The signup request containing user details.
   * @return A ResponseEntity indicating success or error message.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest) {
    // Check if the username is already taken
    if (userRepository.existsById(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    // Check if the email is already in use
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create a new user's account
    User user = new User(); // Encode the password

    Set<String> strRoles = signUpRequest.getRoles(); // Get the roles from the request
    Set<Role> roles = new HashSet<>(); // Initialize a set to hold the user roles

    if (strRoles.isEmpty()) {
      Role userRole = roleRepository.findByName(EmployeeRole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
      roles.add(userRole);
    } else  {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(EmployeeRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;
          case "customer":
            Role costumerRole = roleRepository.findByName(EmployeeRole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(costumerRole);
          default:
            Role userRole = roleRepository.findByName(EmployeeRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        }
      });
    }

    // Assign roles to the user and save it to the database
    user.setRoles(roles);
    userRepository.save(user);

    // Return a success message upon successful registration
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
