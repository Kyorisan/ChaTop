package com.chatop.api.controller;

import com.chatop.api.dto.MessageRequest;
import com.chatop.api.dto.MessageResponse;
import com.chatop.api.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request,
                                                     Authentication authentication) {
    messageService.send(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Message send with success"));
  }
}
