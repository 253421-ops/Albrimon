package com.albrimon.api.client;
import com.albrimon.api.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/clients")
public class ClientController {
 private final ClientService service;
 public ClientController(ClientService service){this.service=service;}
 @GetMapping public List<ClientDtos.Response> list(@AuthenticationPrincipal UserEntity u){return service.list(u);}
 @GetMapping("/me") public ClientDtos.Response me(@AuthenticationPrincipal UserEntity u){\n  if(u.getRole()!=com.albrimon.api.user.Role.CLIENTE) throw new IllegalArgumentException("Esta ruta es para cuentas CLIENTE.");\n  var c=service.getOwnProfile(u); return ClientDtos.Response.from(c,false);\n }\n @GetMapping("/{id}") public ClientDtos.Response get(@PathVariable Long id,@AuthenticationPrincipal UserEntity u){return service.get(id,u);}
 @PostMapping public ClientDtos.Response create(@Valid @RequestBody ClientDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.create(r,u);}
 @PutMapping("/{id}") public ClientDtos.Response update(@PathVariable Long id,@Valid @RequestBody ClientDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.update(id,r,u);}
}
