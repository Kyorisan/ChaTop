package com.chatop.api.service;

import com.chatop.api.controller.dto.connection.AuthResponse;
import com.chatop.api.controller.dto.connection.LoginRequest;
import com.chatop.api.controller.dto.connection.RegisterRequest;
import com.chatop.api.entity.User;
import com.chatop.api.exception.EmailAlreadyUsedException;
import com.chatop.api.repository.UserRepository;
import com.chatop.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new EmailAlreadyUsedException(request.email());
    }
    User user = new User(request.email(), request.name(), passwordEncoder.encode(request.password()));
    userRepository.save(user);
    return new AuthResponse(jwtService.generateToken(user.getEmail()));
  }

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    return new AuthResponse(jwtService.generateToken(request.email()));
  }
}
