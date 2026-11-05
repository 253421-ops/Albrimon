package com.albrimon.api.project;
import com.albrimon.api.user.*;
import com.albrimon.api.client.ClientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/projects/{projectId}/pieces")
public class ProjectPieceController {
 private final ProjectRepository projects; private final ProjectPieceRepository pieces;
 public ProjectPieceController(ProjectRepository p,ProjectPieceRepository x){projects=p;pieces=x;}
 public record PieceRequest(@NotBlank String name,@Min(1) int widthMm,@Min(1) int heightMm,boolean canRotate){}
 public record PieceResponse(Long id,String name,int widthMm,int heightMm,boolean canRotate){}
 private ProjectEntity owned(Long id,UserEntity u){
  ClientService.requireDesigner(u);
  return projects.findByIdAndDesignerId(id,u.getId()).orElseThrow(()->new IllegalArgumentException("Proyecto no encontrado."));
 }
 @GetMapping public List<PieceResponse> list(@PathVariable Long projectId,@AuthenticationPrincipal UserEntity u){
  owned(projectId,u); return pieces.findAllByProjectIdOrderById(projectId).stream().map(p->new PieceResponse(p.getId(),p.getName(),p.getWidthMm(),p.getHeightMm(),p.isCanRotate())).toList();
 }
 @PostMapping public PieceResponse create(@PathVariable Long projectId,@Valid @RequestBody PieceRequest r,@AuthenticationPrincipal UserEntity u){
  var p=pieces.save(new ProjectPieceEntity(owned(projectId,u),r.name(),r.widthMm(),r.heightMm(),r.canRotate()));
  return new PieceResponse(p.getId(),p.getName(),p.getWidthMm(),p.getHeightMm(),p.isCanRotate());
 }
 @PutMapping("/{pieceId}") public PieceResponse update(@PathVariable Long projectId,@PathVariable Long pieceId,@Valid @RequestBody PieceRequest r,@AuthenticationPrincipal UserEntity u){
  owned(projectId,u); var p=pieces.findByIdAndProjectId(pieceId,projectId).orElseThrow(()->new IllegalArgumentException("Pieza no encontrada."));
  p.update(r.name(),r.widthMm(),r.heightMm(),r.canRotate()); pieces.save(p);
  return new PieceResponse(p.getId(),p.getName(),p.getWidthMm(),p.getHeightMm(),p.isCanRotate());
 }
 @DeleteMapping("/{pieceId}") public void delete(@PathVariable Long projectId,@PathVariable Long pieceId,@AuthenticationPrincipal UserEntity u){
  owned(projectId,u); var p=pieces.findByIdAndProjectId(pieceId,projectId).orElseThrow(()->new IllegalArgumentException("Pieza no encontrada.")); pieces.delete(p);
 }
}
