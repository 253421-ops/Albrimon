package com.albrimon.api.client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    List<ClientEntity> findAllByDesignerIdOrderByName(Long designerId);
    Optional<ClientEntity> findByIdAndDesignerId(Long id,Long designerId);
    Optional<ClientEntity> findByLinkCode(String linkCode);
    Optional<ClientEntity> findByLinkedUserId(UUID userId);
}
