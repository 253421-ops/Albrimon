package com.albrimon.api.client;

import com.albrimon.api.user.UserEntity;
import jakarta.persistence.*;
import java.security.SecureRandom;

@Entity @Table(name="clients")
public class ClientEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="designer_id") private UserEntity designer;
    @OneToOne @JoinColumn(name="linked_user_id", unique=true) private UserEntity linkedUser;
    @Column(nullable=false) private String name;
    private String phone;
    private Integer age;
    private String size;
    @Column(nullable=false) private boolean active=true;
    private Integer bustCm, waistCm, hipCm, lengthCm;
    @Column(unique=true) private String linkCode;
    protected ClientEntity(){}
    public ClientEntity(UserEntity designer,String name,String phone,Integer age,String size,
                        Integer bustCm,Integer waistCm,Integer hipCm,Integer lengthCm){
        this.designer=designer;this.name=name;this.phone=phone;this.age=age;this.size=size;
        this.bustCm=bustCm;this.waistCm=waistCm;this.hipCm=hipCm;this.lengthCm=lengthCm;
        this.linkCode=generateCode();
    }
    private String generateCode(){
        String chars="ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; SecureRandom r=new SecureRandom();
        StringBuilder b=new StringBuilder(); for(int i=0;i<6;i++) b.append(chars.charAt(r.nextInt(chars.length())));
        return b.toString();
    }
    public Long getId(){return id;} public UserEntity getDesigner(){return designer;}
    public UserEntity getLinkedUser(){return linkedUser;} public void setLinkedUser(UserEntity u){linkedUser=u;}
    public String getName(){return name;} public String getPhone(){return phone;}
    public Integer getAge(){return age;} public String getSize(){return size;}
    public boolean isActive(){return active;} public Integer getBustCm(){return bustCm;}
    public Integer getWaistCm(){return waistCm;} public Integer getHipCm(){return hipCm;}
    public Integer getLengthCm(){return lengthCm;} public String getLinkCode(){return linkCode;}
    public void setLinkCode(String c){linkCode=c;}
    public void update(String name,String phone,Integer age,String size,boolean active,
                       Integer bust,Integer waist,Integer hip,Integer length){
        this.name=name;this.phone=phone;this.age=age;this.size=size;this.active=active;
        this.bustCm=bust;this.waistCm=waist;this.hipCm=hip;this.lengthCm=length;
    }
}
