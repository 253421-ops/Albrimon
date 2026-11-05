package com.albrimon.api.scrap;
import com.albrimon.api.project.ProjectEntity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="scrap_usage", indexes={@Index(name="idx_usage_project",columnList="project_id"),@Index(name="idx_usage_scrap",columnList="scrap_id")})
public class ScrapUsageEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="project_id") private ProjectEntity project;
 @ManyToOne(optional=false) @JoinColumn(name="scrap_id") private ScrapEntity scrap;
 @Column(nullable=false) private Long usedAreaMm2;
 @Column(nullable=false) private Integer leftoverWmm;
 @Column(nullable=false) private Integer leftoverHmm;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected ScrapUsageEntity(){}
 public ScrapUsageEntity(ProjectEntity p,ScrapEntity s,long area,int w,int h){project=p;scrap=s;usedAreaMm2=area;leftoverWmm=w;leftoverHmm=h;}
 public Long getId(){return id;} public ProjectEntity getProject(){return project;}
 public ScrapEntity getScrap(){return scrap;} public Long getUsedAreaMm2(){return usedAreaMm2;}
 public Integer getLeftoverWmm(){return leftoverWmm;} public Integer getLeftoverHmm(){return leftoverHmm;}
 public Instant getCreatedAt(){return createdAt;}
}
