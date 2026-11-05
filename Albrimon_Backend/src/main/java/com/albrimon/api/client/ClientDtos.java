package com.albrimon.api.client;
import jakarta.validation.constraints.*;
public class ClientDtos {
 public record Request(@NotBlank String name,String phone,@Min(1) @Max(120) Integer age,
   String size, @Min(0) Integer bustCm,@Min(0) Integer waistCm,
   @Min(0) Integer hipCm,@Min(0) Integer lengthCm, Boolean active) {}
 public record Response(Long id,String name,String phone,Integer age,String size,boolean active,
   Integer bustCm,Integer waistCm,Integer hipCm,Integer lengthCm,String linkCode) {
  static Response from(ClientEntity c,boolean includeCode){return new Response(c.getId(),c.getName(),c.getPhone(),
   c.getAge(),c.getSize(),c.isActive(),c.getBustCm(),c.getWaistCm(),c.getHipCm(),c.getLengthCm(),
   includeCode?c.getLinkCode():null);}
 }
}
