package com.albrimon.api.user;

import com.albrimon.api.client.ClientRepository;
import com.albrimon.api.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder;
    private final JwtService jwt; private final ClientRepository clients;
    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt, ClientRepository clients) {
        this.users=users; this.encoder=encoder; this.jwt=jwt; this.clients=clients;
    }
    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest r) {
        if (users.existsByEmailIgnoreCase(r.email())) throw new IllegalStateException("El correo ya está registrado.");
        UserEntity user = users.save(new UserEntity(r.name().trim(),r.email().trim(),
            encoder.encode(r.password()),Role.CLIENTE));
        if (r.linkCode()!=null && !r.linkCode().isBlank()) {
            var client=clients.findByLinkCode(r.linkCode().trim().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Código de vínculo inválido."));
            if (client.getLinkedUser()!=null) throw new IllegalStateException("Este código ya fue utilizado.");
            client.setLinkedUser(user);
            client.setLinkCode(null);
        }
        return response(user);
    }
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest r) {
        UserEntity user=users.findByEmailIgnoreCase(r.email().trim())
            .orElseThrow(() -> new IllegalArgumentException("Correo o contraseña incorrectos."));
        if (!encoder.matches(r.password(),user.getPasswordHash()))
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        return response(user);
    }
    public AuthDtos.UserResponse me(UserEntity user) { return AuthDtos.UserResponse.from(user); }
    private AuthDtos.AuthResponse response(UserEntity u) {
        return new AuthDtos.AuthResponse(jwt.generate(u),"Bearer",AuthDtos.UserResponse.from(u));
    }
}
