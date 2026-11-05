package com.albrimon.api.scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ScrapUsageRepository extends JpaRepository<ScrapUsageEntity,Long> {
 List<ScrapUsageEntity> findAllByProjectIdOrderByCreatedAtDesc(Long projectId);
}
