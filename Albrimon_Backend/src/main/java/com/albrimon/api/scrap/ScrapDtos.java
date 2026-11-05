package com.albrimon.api.scrap;
import jakarta.validation.constraints.*;
public class ScrapDtos {
 public record Request(@NotBlank String name,@NotBlank String fabricType,
  @NotBlank @Pattern(regexp="^#[0-9A-Fa-f]{6}$") String colorHex,
  @Min(1) int widthMm,@Min(1) int heightMm,boolean hasGrain,String photoPath){}
 public record Response(Long id,String name,String fabricType,String colorHex,int widthMm,
  int heightMm,boolean hasGrain,String photoPath,boolean available){
  static Response from(ScrapEntity s){return new Response(s.getId(),s.getName(),s.getFabricType(),
   s.getColorHex(),s.getWidthMm(),s.getHeightMm(),s.isHasGrain(),s.getPhotoPath(),s.isAvailable());}
 }
 public record ApplyPlanRequest(@NotNull Long projectId,@NotEmpty java.util.List<UsageInput> usages){}
 public record UsageInput(@NotNull Long scrapId,@Positive long usedAreaMm2,
  @Min(0) int leftoverWmm,@Min(0) int leftoverHmm){}
 public record UsageResponse(Long id,Long projectId,Long scrapId,String scrapName,
  long usedAreaMm2,int leftoverWmm,int leftoverHmm,java.time.Instant createdAt){}
}
