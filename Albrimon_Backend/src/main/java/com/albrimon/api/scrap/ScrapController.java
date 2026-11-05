package com.albrimon.api.scrap;
import com.albrimon.api.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/scraps")
public class ScrapController {
 private final ScrapService service;
 public ScrapController(ScrapService s){service=s;}
 @GetMapping public List<ScrapDtos.Response> list(@AuthenticationPrincipal UserEntity u){return service.list(u);}
 @GetMapping("/{id}") public ScrapDtos.Response get(@PathVariable Long id,@AuthenticationPrincipal UserEntity u){return service.get(id,u);}
 @PostMapping public ScrapDtos.Response create(@Valid @RequestBody ScrapDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.create(r,u);}
 @PutMapping("/{id}") public ScrapDtos.Response update(@PathVariable Long id,@Valid @RequestBody ScrapDtos.Request r,@AuthenticationPrincipal UserEntity u){return service.update(id,r,u);}
 @DeleteMapping("/{id}") public void delete(@PathVariable Long id,@AuthenticationPrincipal UserEntity u){service.delete(id,u);}
 @PostMapping("/apply-plan") public List<ScrapDtos.UsageResponse> apply(@Valid @RequestBody ScrapDtos.ApplyPlanRequest r,@AuthenticationPrincipal UserEntity u){return service.applyPlan(r,u);}
 @GetMapping("/usage/project/{projectId}") public List<ScrapDtos.UsageResponse> history(@PathVariable Long projectId,@AuthenticationPrincipal UserEntity u){return service.history(projectId,u);}
}
