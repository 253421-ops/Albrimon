package com.albrimon.api.project;
import com.albrimon.api.client.ClientEntity;
import com.albrimon.api.user.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="projects")
public class ProjectEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="designer_id") private UserEntity designer;
 @ManyToOne(optional=false) @JoinColumn(name="client_id") private ClientEntity client;
 @Column(nullable=false) private String name;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private ProjectStatus status=ProjectStatus.PENDIENTE;
 private LocalDate dueDate;
 @Column(length=1000) private String notes;
 protected ProjectEntity(){}
 public ProjectEntity(UserEntity d,ClientEntity c,String n,ProjectStatus s,LocalDate date,String notes){
  designer=d;client=c;name=n;status=s==null?ProjectStatus.PENDIENTE:s;dueDate=date;this.notes=notes;
 }
 public Long getId(){return id;} public UserEntity getDesigner(){return designer;}
 public ClientEntity getClient(){return client;} public String getName(){return name;}
 public ProjectStatus getStatus(){return status;} public LocalDate getDueDate(){return dueDate;}
 public String getNotes(){return notes;}
 public void update(String n,ProjectStatus s,LocalDate d,String notes){name=n;status=s;dueDate=d;this.notes=notes;}
}
