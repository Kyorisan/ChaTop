package com.chatop.api.controller;

import com.chatop.api.controller.dto.error.ErrorResponse;
import com.chatop.api.controller.dto.message.MessageRequest;
import com.chatop.api.controller.dto.message.MessageResponse;
import com.chatop.api.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Messages", description = "Envoi de messages aux propriétaires")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping
  @Operation(summary = "Envoyer un message au propriétaire d'une location",
    description = "Le user_id doit correspondre à l'utilisateur connecté.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Message enregistré"),
    @ApiResponse(responseCode = "400", description = "Champ manquant",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content),
    @ApiResponse(responseCode = "403", description = "Le user_id ne correspond pas à l'utilisateur connecté",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "404", description = "Location ou utilisateur introuvable",
    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request,
                                                     Authentication authentication) {
    messageService.send(request, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Message send with success"));
  }
}
