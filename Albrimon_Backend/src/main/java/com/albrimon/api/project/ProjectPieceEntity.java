package com.albrimon.api.project;
import jakarta.persistence.*;
@Entity @Table(name="project_pieces")
public class ProjectPieceEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="project_id") private ProjectEntity project;
 @Column(nullable=false) private String name;
 @Column(nullable=false) private Integer widthMm;
 @Column(nullable=false) private Integer heightMm;
 @Column(nullable=false) private boolean canRotate;
 protected ProjectPieceEntity(){}
 public ProjectPieceEntity(ProjectEntity p,String n,int w,int h,boolean rotate){project=p;name=n;widthMm=w;heightMm=h;canRotate=rotate;}
 public Long getId(){return id;} public ProjectEntity getProject(){return project;}
 public String getName(){return name;} public Integer getWidthMm(){return widthMm;}
 public Integer getHeightMm(){return heightMm;} public boolean isCanRotate(){return canRotate;}
 public void update(String n,int w,int h,boolean r){name=n;widthMm=w;heightMm=h;canRotate=r;}
}
