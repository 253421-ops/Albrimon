package com.albrimon.api.scrap;
import com.albrimon.api.user.UserEntity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="scraps")
public class ScrapEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="designer_id") private UserEntity designer;
 @Column(nullable=false) private String name;
 @Column(nullable=false) private String fabricType;
 @Column(nullable=false,length=7) private String colorHex;
 @Column(nullable=false) private Integer widthMm;
 @Column(nullable=false) private Integer heightMm;
 @Column(nullable=false) private boolean hasGrain;
 private String photoPath;
 @Column(nullable=false) private boolean available=true;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected ScrapEntity(){}
 public ScrapEntity(UserEntity d,String n,String f,String color,int w,int h,boolean grain,String photo){
  designer=d;name=n;fabricType=f;colorHex=color;widthMm=w;heightMm=h;hasGrain=grain;photoPath=photo;
 }
 public Long getId(){return id;} public UserEntity getDesigner(){return designer;}
 public String getName(){return name;} public String getFabricType(){return fabricType;}
 public String getColorHex(){return colorHex;} public Integer getWidthMm(){return widthMm;}
 public Integer getHeightMm(){return heightMm;} public boolean isHasGrain(){return hasGrain;}
 public String getPhotoPath(){return photoPath;} public boolean isAvailable(){return available;}
 public void markUsed(){available=false;}
 public void update(String n,String f,String c,int w,int h,boolean grain,String photo,boolean a){
  name=n;fabricType=f;colorHex=c;widthMm=w;heightMm=h;hasGrain=grain;photoPath=photo;available=a;
 }
}
