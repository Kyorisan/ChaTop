package com.chatop.api.service;

import com.chatop.api.controller.dto.user.UserResponse;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public UserResponse getById(Integer id) {
    return userRepository.findById(id)
      .map(UserResponse::from)
      .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
  }

  @Transactional(readOnly = true)
  public UserResponse getByEmail(String email) {
    return userRepository.findByEmail(email)
      .map(UserResponse::from)
      .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
  }
}
