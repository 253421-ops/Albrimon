package com.albrimon.api.config;

import com.albrimon.api.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapDesigner implements CommandLineRunner {
 private final UserRepository users; private final PasswordEncoder encoder;
 @Value("${app.bootstrap.designer.name:}") private String name;
 @Value("${app.bootstrap.designer.email:}") private String email;
 @Value("${app.bootstrap.designer.password:}") private String password;
 public BootstrapDesigner(UserRepository users,PasswordEncoder encoder){this.users=users;this.encoder=encoder;}
 @Override public void run(String... args){
  if(email.isBlank() || password.isBlank()) return;
  if(!users.existsByEmailIgnoreCase(email)){
   users.save(new UserEntity(name.isBlank()?"Confeccionador":name,email,encoder.encode(password),Role.DISENADOR));
   System.out.println("Cuenta inicial DISENADOR creada para: "+email);
  }
 }
}
