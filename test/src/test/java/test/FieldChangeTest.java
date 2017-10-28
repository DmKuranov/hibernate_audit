package test;

import org.junit.Assert;
import org.junit.Test;
import test.domain.BasicEntity;

import java.util.Date;

public class FieldChangeTest extends ChangeTest {

    @Test
    public void fieldUpdateTest() {
        BasicEntity entity = new BasicEntity();
        Date originalDate = new Date();
        entity.setDateCreated(originalDate);
        entityRepository.saveAndFlush(entity);
        // Сохранение создало запись
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        entity = entityRepository.findAll().get(0);
        originalDate.setTime(originalDate.getTime()-10000);
        entity.setDateCreated(originalDate);
        entityRepository.saveAndFlush(entity);
        // Изменение значения поля породило еще запись
        Assert.assertEquals(2, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(1, entityRepository.findAll().size());
    }

    @Test
    public void fieldUpdateToSameTest() {
        BasicEntity entity = new BasicEntity();
        Date originalDate = new Date();
        entity.setDateCreated(originalDate);
        entityRepository.saveAndFlush(entity);
        // Сохранение создало запись
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        entity = entityRepository.findAll().get(0);
        Date anotherDateSameValue = new Date(originalDate.getTime());
        entity.setDateCreated(anotherDateSameValue);
        Assert.assertTrue(originalDate != entity.getDateCreated());
        entityRepository.saveAndFlush(entity);
        // Изменение значения поля на то же не породило запись
        Assert.assertEquals(1, auditLogEntityRepository.findAll().size());
        Assert.assertEquals(1, entityRepository.findAll().size());
    }
}
