package com.albrimon.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

public class AuthDtos {
    public record RegisterRequest(@NotBlank @Size(max=120) String name,
        @Email @NotBlank String email, @Size(min=6,max=100) String password,
        @Size(max=6) String linkCode) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record UserResponse(UUID id, String name, String email, Role role, Instant createdAt) {
        public static UserResponse from(UserEntity u) {
            return new UserResponse(u.getId(),u.getName(),u.getEmail(),u.getRole(),u.getCreatedAt());
        }
    }
    public record AuthResponse(String token, String tokenType, UserResponse user) {}
}
