package com.chatop.api.controller.dto.user;

import com.chatop.api.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserResponse(Integer id,
                           String name,
                           String email,
                           @JsonProperty("created_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime createdAt,
                           @JsonProperty("updated_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime updatedAt) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
  }
}
