package com.albrimon.api.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "users")
public class UserEntity {
    @Id private UUID id;
    @Column(nullable=false, length=120) private String name;
    @Column(nullable=false, unique=true, length=180) private String email;
    @Column(nullable=false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private Role role;
    @Column(nullable=false) private Instant createdAt;
    protected UserEntity() {}
    public UserEntity(String name, String email, String passwordHash, Role role) {
        this.id=UUID.randomUUID(); this.name=name; this.email=email.toLowerCase();
        this.passwordHash=passwordHash; this.role=role; this.createdAt=Instant.now();
    }
    public UUID getId(){return id;} public String getName(){return name;}
    public String getEmail(){return email;} public String getPasswordHash(){return passwordHash;}
    public Role getRole(){return role;} public Instant getCreatedAt(){return createdAt;}
    public void setRole(Role role){this.role=role;}
}
