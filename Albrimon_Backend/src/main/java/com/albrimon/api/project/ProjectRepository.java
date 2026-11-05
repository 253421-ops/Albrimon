package com.albrimon.api.project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
 List<ProjectEntity> findAllByDesignerIdOrderByDueDateAsc(Long designerId);
 List<ProjectEntity> findAllByClientLinkedUserIdOrderByDueDateAsc(java.util.UUID userId);
 Optional<ProjectEntity> findByIdAndDesignerId(Long id,Long designerId);
}
