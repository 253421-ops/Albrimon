package com.albrimon.api.client;
import com.albrimon.api.user.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class ClientService {
 private final ClientRepository repo;
 public ClientService(ClientRepository repo){this.repo=repo;}
 public List<ClientDtos.Response> list(UserEntity u){
  requireDesigner(u); return repo.findAllByDesignerIdOrderByName(u.getId()).stream().map(c->ClientDtos.Response.from(c,true)).toList();
 }
 public ClientEntity getOwnProfile(UserEntity u){\n  if(u.getRole()!=Role.CLIENTE) throw new IllegalArgumentException("Esta operación requiere el rol CLIENTE.");\n  return repo.findByLinkedUserId(u.getId()).orElseThrow(()->new IllegalArgumentException("Tu cuenta todavía no está vinculada a una ficha de cliente."));\n }\n public ClientDtos.Response get(Long id,UserEntity u){
  if(u.getRole()==Role.CLIENTE) return repo.findByLinkedUserId(u.getId())
    .filter(c->c.getId().equals(id)).map(c->ClientDtos.Response.from(c,false))
    .orElseThrow(()->new IllegalArgumentException("Cliente no encontrado."));
  return ClientDtos.Response.from(owned(id,u),true);
 }
 public ClientDtos.Response create(ClientDtos.Request r,UserEntity u){
  requireDesigner(u);
  var c=new ClientEntity(u,r.name().trim(),r.phone(),r.age(),r.size(),r.bustCm(),r.waistCm(),r.hipCm(),r.lengthCm());
  return ClientDtos.Response.from(repo.save(c),true);
 }
 public ClientDtos.Response update(Long id,ClientDtos.Request r,UserEntity u){
  var c=owned(id,u); c.update(r.name().trim(),r.phone(),r.age(),r.size(),
    r.active()==null?c.isActive():r.active(),r.bustCm(),r.waistCm(),r.hipCm(),r.lengthCm());
  return ClientDtos.Response.from(repo.save(c),true);
 }
 public ClientEntity owned(Long id,UserEntity u){
  requireDesigner(u); return repo.findByIdAndDesignerId(id,u.getId())
    .orElseThrow(()->new IllegalArgumentException("Cliente no encontrado."));
 }
 public static void requireDesigner(UserEntity u){
  if(u.getRole()!=Role.DISENADOR) throw new IllegalArgumentException("Esta operación requiere el rol DISENADOR.");
 }
}
