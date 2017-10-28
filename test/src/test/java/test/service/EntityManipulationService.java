package test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.domain.BasicEntity;
import test.domain.ChildEntity;
import test.domain.ComplexEntity;
import test.repository.BasicEntityRepository;
import test.repository.ChildEntityRepository;
import test.repository.ComplexEntityRepository;

@Service
public class EntityManipulationService {

    @Autowired
    private BasicEntityRepository entityRepository;

    @Transactional
    public void transactionalMethodThrowsException() throws InterruptedException {
        BasicEntity entity = new BasicEntity();
        entityRepository.saveAndFlush(entity);
        throw new RuntimeException();
    }

    @Transactional
    public void transactionalMethodCreateTwo() {
        BasicEntity entity = new BasicEntity();
        entityRepository.save(entity);
        entity = new BasicEntity();
        entityRepository.save(entity);
    }
}
