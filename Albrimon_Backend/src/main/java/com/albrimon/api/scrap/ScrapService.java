package com.albrimon.api.scrap;
import com.albrimon.api.client.ClientService;
import com.albrimon.api.project.*;
import com.albrimon.api.user.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class ScrapService {
 private final ScrapRepository scraps; private final ScrapUsageRepository usages; private final ProjectRepository projects;
 public ScrapService(ScrapRepository s,ScrapUsageRepository u,ProjectRepository p){scraps=s;usages=u;projects=p;}
 public List<ScrapDtos.Response> list(UserEntity u){
  ClientService.requireDesigner(u);
  return scraps.findAllByDesignerIdOrderByAvailableDescCreatedAtDesc(u.getId()).stream().map(ScrapDtos.Response::from).toList();
 }
 public ScrapDtos.Response get(Long id,UserEntity u){return ScrapDtos.Response.from(owned(id,u));}
 public ScrapDtos.Response create(ScrapDtos.Request r,UserEntity u){
  ClientService.requireDesigner(u);
  return ScrapDtos.Response.from(scraps.save(new ScrapEntity(u,r.name(),r.fabricType(),r.colorHex(),
   r.widthMm(),r.heightMm(),r.hasGrain(),r.photoPath())));
 }
 public ScrapDtos.Response update(Long id,ScrapDtos.Request r,UserEntity u){
  var s=owned(id,u);if(!s.isAvailable()) throw new IllegalStateException("No se puede editar un retazo que ya fue utilizado.");s.update(r.name(),r.fabricType(),r.colorHex(),r.widthMm(),r.heightMm(),r.hasGrain(),r.photoPath(),s.isAvailable());
  return ScrapDtos.Response.from(scraps.save(s));
 }
 public void delete(Long id,UserEntity u){
  var s=owned(id,u);
  if(!s.isAvailable()) throw new IllegalStateException("No se puede eliminar un retazo que ya tiene usos registrados.");
  scraps.delete(s);
 }
 public ScrapEntity owned(Long id,UserEntity u){
  ClientService.requireDesigner(u);
  return scraps.findByIdAndDesignerId(id,u.getId()).orElseThrow(()->new IllegalArgumentException("Retazo no encontrado."));
 }
 @Transactional
 public List<ScrapDtos.UsageResponse> applyPlan(ScrapDtos.ApplyPlanRequest r,UserEntity u){
  ClientService.requireDesigner(u);
  ProjectEntity project=projects.findByIdAndDesignerId(r.projectId(),u.getId())
   .orElseThrow(()->new IllegalArgumentException("Proyecto no encontrado."));
  List<ScrapDtos.UsageResponse> result=new ArrayList<>();
  Set<Long> seen=new HashSet<>();
  for(var item:r.usages()){
   if(!seen.add(item.scrapId())) throw new IllegalArgumentException("Un retazo no puede repetirse en el mismo plan.");
   ScrapEntity original=owned(item.scrapId(),u);
   if(!original.isAvailable()) throw new IllegalStateException("El retazo "+original.getName()+" ya no está disponible.");
   long maxArea=(long)original.getWidthMm()*original.getHeightMm();
   if(item.usedAreaMm2()>maxArea) throw new IllegalArgumentException("El área utilizada supera el tamaño del retazo "+original.getName()+".");
   if(item.leftoverWmm()>0 && item.leftoverHmm()>0 &&
      ((long)item.leftoverWmm()*item.leftoverHmm()+item.usedAreaMm2()>maxArea))
      throw new IllegalArgumentException("Las medidas del sobrante no son consistentes con el área utilizada.");
   original.markUsed();
   ScrapUsageEntity usage=usages.save(new ScrapUsageEntity(project,original,item.usedAreaMm2(),item.leftoverWmm(),item.leftoverHmm()));
   if(item.leftoverWmm()>=100 && item.leftoverHmm()>=100){
    scraps.save(new ScrapEntity(u,original.getName()+" - sobrante",original.getFabricType(),original.getColorHex(),
      item.leftoverWmm(),item.leftoverHmm(),original.isHasGrain(),null));
   }
   result.add(new ScrapDtos.UsageResponse(usage.getId(),project.getId(),original.getId(),original.getName(),
    usage.getUsedAreaMm2(),usage.getLeftoverWmm(),usage.getLeftoverHmm(),usage.getCreatedAt()));
  }
  return result;
 }
 public List<ScrapDtos.UsageResponse> history(Long projectId,UserEntity u){
  ClientService.requireDesigner(u);
  projects.findByIdAndDesignerId(projectId,u.getId()).orElseThrow(()->new IllegalArgumentException("Proyecto no encontrado."));
  return usages.findAllByProjectIdOrderByCreatedAtDesc(projectId).stream().map(x->
   new ScrapDtos.UsageResponse(x.getId(),x.getProject().getId(),x.getScrap().getId(),x.getScrap().getName(),
    x.getUsedAreaMm2(),x.getLeftoverWmm(),x.getLeftoverHmm(),x.getCreatedAt())).toList();
 }
}
