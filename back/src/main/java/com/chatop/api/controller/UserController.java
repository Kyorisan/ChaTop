package com.chatop.api.controller;

import com.chatop.api.controller.dto.error.ErrorResponse;
import com.chatop.api.controller.dto.user.UserResponse;
import com.chatop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Utilisateurs", description = "Consultation des utilisateurs")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @Operation(summary = "Détail d'un utilisateur", description = "Utilisé notamment pour afficher le propriétaire d'une location.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content),
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
    return ResponseEntity.ok(userService.getById(id));
  }
}
