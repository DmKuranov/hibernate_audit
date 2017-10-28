package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.domain.ComplexEntity;

public interface ComplexEntityRepository extends JpaRepository<ComplexEntity, Integer> {
}
