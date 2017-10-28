package ru.dmkuranov.hibernate_audit.inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;
import ru.dmkuranov.hibernate_audit.util.comparator.ComparatorService;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityDelta;
import ru.dmkuranov.hibernate_audit.inspector.model.FieldValueDelta;
import ru.dmkuranov.hibernate_audit.util.formatter.FormatterService;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class EntityInspector {
    private final Map<Class, EntityDescription> entityDescriptions = new WeakHashMap<Class, EntityDescription>();
    @Autowired
    private EntityManager entityManager;
    @Autowired
    protected FormatterService formatter;
    @Autowired
    protected ComparatorService comparatorService;


    public EntityInspector() {
    }

    public EntityState buildState(Object entity) {
        Class clazz = entity.getClass();
        EntityDescription description = getDescription(clazz);
        Map<FieldDescription, FieldValue> values = new LinkedHashMap<FieldDescription, FieldValue>();
        for(FieldDescription fd :description.getPersistentFieldDescriptions()) {
            values.put(fd, FieldValue.build(fd, entity));
        }
        return new EntityState(new EntityId(clazz, (Serializable) description.getIdField().getValue(entity)), values);
    }

    public EntityId getEntityId(Object entity) {
        Class clazz = entity.getClass();
        return new EntityId(clazz, (Serializable) getDescription(clazz).getIdField().getValue(entity));
    }

    public Object retrieveEntity(Object entity) {
        Class clazz = entity.getClass();
        EntityDescription entityDesc = getDescription(clazz);
        Serializable idFieldValue = (Serializable) entityDesc.getIdField().getValue(entity);
        return entityManager.find(clazz, idFieldValue);
    }

    public static EntityDelta getDelta(EntityState from, EntityState to) {
        EntityId fromId = from.getEntityId();
        EntityId toId = to.getEntityId();
        if(!fromId.getClazz().equals(toId.getClazz())) {
            throw new UnsupportedOperationException("Change delta for different types can't be calculated");
        }
        if(!fromId.getId().equals(toId.getId())) {
            throw new UnsupportedOperationException("Change delta for different entities can't be calculated");
        }
        Map<Field, FieldValueDelta> changedValueFields = getChangedFields(from.getPropertyValues(), to.getPropertyValues());
        if(!changedValueFields.isEmpty()) {
            return new EntityDelta(fromId, changedValueFields);
        } else {
            return null;
        }
    }

    public static Map<Field, FieldValueDelta> getChangedFields(Map<FieldDescription, FieldValue> propertiesFrom, Map<FieldDescription, FieldValue> propertiesTo) {
        Map<Field, FieldValueDelta> changedValueFields = new HashMap<Field, FieldValueDelta>();
        Set<Map.Entry<FieldDescription, FieldValue>> entrySet = propertiesFrom.entrySet();
        for (Map.Entry<FieldDescription, FieldValue> entry : entrySet) {
            FieldDescription fd = entry.getKey();
            FieldValue fieldValueFrom = entry.getValue();
            FieldValue fieldValueTo = propertiesTo.get(fd);
            if(!fieldValueFrom.equals(fieldValueTo)) {
                FieldValueDelta delta = fieldValueFrom.getDelta(fieldValueTo);
                changedValueFields.put(fd.getField(), delta);
            }
        }
        return changedValueFields;
    }

    public EntityDescription getDescription(Class clazz) {
        EntityDescription description = entityDescriptions.get(clazz);
        if(description==null) {
            description = new EntityDescription(this, clazz);
            entityDescriptions.put(clazz, description);
        }
        return description;
    }

    public FormatterService getFormatter() {
        return formatter;
    }

    public ComparatorService getComparator() {
        return comparatorService;
    }
}
