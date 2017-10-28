package test;

import org.junit.Assert;
import org.junit.Test;
import test.domain.BasicEntity;
import test.domain.ComplexEntity;
import test.domain.EmbeddableEntity;

public class EmbeddedChangeTest extends ChangeTest {

    /**
     * Изменение встроенной сущности порождает запись журнала
     */
    @Test
    public void updateEmbeddedEntityProducesLogEntry() {
        ComplexEntity entity = new ComplexEntity();
        EmbeddableEntity embeddable = new EmbeddableEntity();
        entity.setPersonInfo(embeddable);
        embeddable.setFirstName("first");
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        entity = complexEntityRepository.findAll().get(0);
        entity.getPersonInfo().setLastName("last");
        complexEntityRepository.saveAndFlush(entity);
        Assert.assertEquals(2, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(1, complexEntityRepository.findAll().size());
    }
}
