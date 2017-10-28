package test;

import org.junit.Assert;
import org.junit.Test;
import test.domain.BasicEntity;

public class EntityChangeTest extends ChangeTest {

    /**
     * Создание порождает запись журнала
     */
    @Test
    public void entityCreationProducesLogEntry() {
        BasicEntity entity = new BasicEntity();
        entityRepository.saveAndFlush(entity);
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
    }

    /**
     * Создание с последующим удалением порождает две записи журнала
     */
    @Test
    public void entityCreationAndRemoving() {
        BasicEntity entity = new BasicEntity();
        entityRepository.saveAndFlush(entity);
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(1, entityRepository.findAll().size());
        entityRepository.delete(entityRepository.findAll().get(0));
        Assert.assertEquals(2, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(0, entityRepository.findAll().size());
    }

    /**
     * Создание двух записей в транзакции порождает одну запись журнала
     */
    @Test
    public void createTwoInSameTrxTest() {
        entityManipulationService.transactionalMethodCreateTwo();
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(2, entityRepository.findAll().size());
    }


    /**
     * Откат транзакции не создает записи журнала
     */
    @Test
    public void transactionRollbackTest() {
        boolean throwed = false;
        try {
            entityManipulationService.transactionalMethodThrowsException();
        } catch (Exception e) {
            // swallow it
            throwed = true;
        }
        Assert.assertEquals(true, throwed);
        Assert.assertEquals(0, auditLogEntityRepository.findAll().size());
    }
}
