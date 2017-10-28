package test;

import org.junit.Assert;
import org.junit.Test;
import test.domain.ChildEntity;
import test.domain.ComplexEntity;
import test.domain.EmbeddableEntity;

import java.util.Collections;

public class ReferenceChangeTest extends ChangeTest {

    /**
     * Изменение дочерней сущности порождает запись журнала
     */
    @Test
    public void childEntityChangeProducesLogEntry() {
        ComplexEntity entity = new ComplexEntity();
        ChildEntity childEntity = new ChildEntity();
        entity.setChilds(Collections.singletonList(childEntity));
        childEntity.setParent(entity);
        complexEntityRepository.saveAndFlush(entity);
        childEntityRepository.saveAndFlush(childEntity);
        ComplexEntity anotherEntity = new ComplexEntity();
        complexEntityRepository.saveAndFlush(anotherEntity);
        Assert.assertEquals(3, auditLogEntityRepository.findAll().size());
        childEntity = childEntityRepository.findAll().get(0);
        childEntity.setParent(anotherEntity);
        childEntityRepository.saveAndFlush(childEntity);
        Assert.assertEquals(4, auditLogEntityRepository.findAll().size());
        childEntity.setParent(null);
        childEntityRepository.saveAndFlush(childEntity);
        Assert.assertEquals(5, auditLogEntityRepository.findAll().size());
    }

    @Test
    public void siblingEntityChangeProducesLogEntry() {
        ComplexEntity entity = new ComplexEntity();
        ChildEntity childEntity = new ChildEntity();
        childEntityRepository.saveAndFlush(childEntity);
        entity.setSibling(childEntity);
        childEntity.setSibling(entity);
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(2, auditLogEntityRepository.findAll().size());
        entity.setSibling(null);
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(3, auditLogEntityRepository.findAll().size());
    }
}
