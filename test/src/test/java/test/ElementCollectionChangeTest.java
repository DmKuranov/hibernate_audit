package test;

import org.junit.Assert;
import org.junit.Test;
import test.ChangeTest;
import test.domain.ComplexEntity;
import test.domain.EmbeddableEntity;

public class ElementCollectionChangeTest extends ChangeTest {

    /**
     * Изменение коллекции примитивов порождает запись журнала
     */
    @Test
    public void updateElementCollectionProducesLogEntry() {
        ComplexEntity entity = new ComplexEntity();
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        entity = complexEntityRepository.findAll().get(0);
        entity.getSetOfStrings().add("value1");
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(2, auditLogEntityRepository.findAll().size());
        entity.getSetOfStrings().add("value2");
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(3, auditLogEntityRepository.findAll().size());
        entity.getSetOfStrings().clear();
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(4, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(1, complexEntityRepository.findAll().size());
    }
}
