package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.domain.AuditLogEntity;

@Repository
public interface AuditLogEntityRepository extends JpaRepository<AuditLogEntity, Integer> {
}
