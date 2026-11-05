package com.albrimon.api.scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ScrapRepository extends JpaRepository<ScrapEntity,Long> {
 List<ScrapEntity> findAllByDesignerIdOrderByAvailableDescCreatedAtDesc(java.util.UUID designerId);
 List<ScrapEntity> findAllByDesignerIdAndAvailableTrueOrderByWidthMmAscHeightMmAsc(java.util.UUID designerId);
 Optional<ScrapEntity> findByIdAndDesignerId(Long id,java.util.UUID designerId);
}
