package com.albrimon.api.project;
import jakarta.validation.constraints.NotBlank;\nimport jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public class ProjectDtos {
 public record Request(@NotBlank String name,@NotNull Long clientId,ProjectStatus status,LocalDate dueDate,String notes){}
 public record Response(Long id,String name,Long clientId,String clientName,ProjectStatus status,LocalDate dueDate,String notes){
  static Response from(ProjectEntity p){return new Response(p.getId(),p.getName(),p.getClient().getId(),
   p.getClient().getName(),p.getStatus(),p.getDueDate(),p.getNotes());}
 }
}
