package com.chatop.api.controller;

import com.chatop.api.controller.dto.connection.AuthResponse;
import com.chatop.api.controller.dto.connection.LoginRequest;
import com.chatop.api.controller.dto.connection.RegisterRequest;
import com.chatop.api.controller.dto.error.ErrorResponse;
import com.chatop.api.controller.dto.user.UserResponse;
import com.chatop.api.service.AuthService;
import com.chatop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Inscription, connexion et utilisateur connecté")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping("/register")
  @SecurityRequirements
  @Operation(summary = "Créer un compte", description = "Crée un utilisateur et renvoie un token JWT pour le connecter directement.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Compte crée"),
    @ApiResponse(responseCode = "400", description = "Champ manquant, email invalide ou déjà utilisé",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  @SecurityRequirements
  @Operation(summary = "Se connecter", description = "Vérifie l'email et le mot de passe puis renvoie un token JWT.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Identifiants valides"),
    @ApiResponse(responseCode = "400", description = "Champ manquant",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect",
      content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @GetMapping("/me")
  @Operation(summary = "Utilisateur connecté", description = "Renvoie l'utilisateur correspondant au token JWT.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
    @ApiResponse(responseCode = "401", description = "Token absent, invalide ou expiré", content = @Content)
  })
  public ResponseEntity<UserResponse> me(Authentication authentication) {
    return ResponseEntity.ok(userService.getByEmail(authentication.getName()));
  }
}
