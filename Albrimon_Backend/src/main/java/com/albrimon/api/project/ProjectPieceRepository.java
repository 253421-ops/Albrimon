package com.albrimon.api.project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProjectPieceRepository extends JpaRepository<ProjectPieceEntity,Long>{
 List<ProjectPieceEntity> findAllByProjectIdOrderById(Long projectId);
 Optional<ProjectPieceEntity> findByIdAndProjectId(Long id,Long projectId);
}
