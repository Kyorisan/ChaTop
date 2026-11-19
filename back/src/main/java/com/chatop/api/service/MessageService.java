package com.chatop.api.service;

import com.chatop.api.dto.MessageRequest;
import com.chatop.api.entity.Message;
import com.chatop.api.entity.Rental;
import com.chatop.api.entity.User;
import com.chatop.api.exception.ResourceNotFoundException;
import com.chatop.api.repository.MessageRepository;
import com.chatop.api.repository.RentalRepository;
import com.chatop.api.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

  private final MessageRepository messageRepository;
  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;

  public MessageService(MessageRepository messageRepository,
                        RentalRepository rentalRepository,
                        UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public void send(MessageRequest request, String userEmail) throws AccessDeniedException {
    User user = userRepository.findById(request.userId())
      .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

    if (!user.getEmail().equals(userEmail)) {
      throw new AccessDeniedException("Cannot send a message on behalf of another user");
    }
    Rental rental = rentalRepository.findById(request.rentalId())
      .orElseThrow(() -> new ResourceNotFoundException("Rental not found: " + request.rentalId()));
    messageRepository.save(new Message(rental, user, request.message()));
  }
}
