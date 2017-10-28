package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.domain.BasicEntity;

@Repository
public interface BasicEntityRepository extends JpaRepository<BasicEntity, Integer> {
}
