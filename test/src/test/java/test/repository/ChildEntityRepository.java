package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.domain.ChildEntity;
import test.domain.ComplexEntity;

public interface ChildEntityRepository extends JpaRepository<ChildEntity, Integer> {
}
