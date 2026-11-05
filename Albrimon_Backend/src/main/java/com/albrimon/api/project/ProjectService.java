package com.albrimon.api.project;
import com.albrimon.api.client.*;
import com.albrimon.api.user.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class ProjectService {
 private final ProjectRepository repo; private final ClientService clients;
 public ProjectService(ProjectRepository r,ClientService c){repo=r;clients=c;}
 public List<ProjectDtos.Response> list(UserEntity u){
  if(u.getRole()==Role.DISENADOR) return repo.findAllByDesignerIdOrderByDueDateAsc(u.getId()).stream().map(ProjectDtos.Response::from).toList();
  return repo.findAllByClientLinkedUserIdOrderByDueDateAsc(u.getId()).stream().map(ProjectDtos.Response::from).toList();
 }
 public ProjectDtos.Response get(Long id,UserEntity u){
  ProjectEntity p=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Proyecto no encontrado."));
  if(u.getRole()==Role.DISENADOR && !p.getDesigner().getId().equals(u.getId())) throw new IllegalArgumentException("Proyecto no encontrado.");
  if(u.getRole()==Role.CLIENTE && (p.getClient().getLinkedUser()==null || !p.getClient().getLinkedUser().getId().equals(u.getId()))) throw new IllegalArgumentException("Proyecto no encontrado.");
  return ProjectDtos.Response.from(p);
 }
 public ProjectDtos.Response create(ProjectDtos.Request r,UserEntity u){
  ClientService.requireDesigner(u);
  var c=clients.owned(r.clientId(),u);
  return ProjectDtos.Response.from(repo.save(new ProjectEntity(u,c,r.name(),r.status(),r.dueDate(),r.notes())));
 }
 public ProjectDtos.Response update(Long id,ProjectDtos.Request r,UserEntity u){
  ClientService.requireDesigner(u);
  var p=repo.findByIdAndDesignerId(id,u.getId()).orElseThrow(()->new IllegalArgumentException("Proyecto no encontrado."));
  if(r.clientId()!=null && !r.clientId().equals(p.getClient().getId())) throw new IllegalArgumentException("No se permite cambiar el cliente asociado al proyecto.");
  p.update(r.name(),r.status()==null?p.getStatus():r.status(),r.dueDate(),r.notes());
  return ProjectDtos.Response.from(repo.save(p));
 }
}
