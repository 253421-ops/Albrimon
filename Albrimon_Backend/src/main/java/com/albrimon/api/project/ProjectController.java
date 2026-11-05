package com.albrimon.api.project;
import com.albrimon.api.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/projects")
public class ProjectController {
 private final ProjectService service;
 public ProjectController(ProjectService s){service=s;}
 @GetMapping public List<ProjectDtos.Response> list(@AuthenticationPrincipal UserEntity u){return service.list(u);}
 @GetMapping("/{id}") public ProjectDtos.Response get(@PathVariable Long id,@AuthenticationPrincipal UserEntity u){return service.get(id,u);}
 @PostMapping public ProjectDtos.Response create(@Valid @RequestBody ProjectDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.create(r,u);}
 @PutMapping("/{id}") public ProjectDtos.Response update(@PathVariable Long id,@Valid @RequestBody ProjectDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.update(id,r,u);}
}
