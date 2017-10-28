package ru.dmkuranov.hibernate_audit.inspector.model;

import ru.dmkuranov.hibernate_audit.inspector.FieldDescription;
import ru.dmkuranov.hibernate_audit.inspector.FieldValue;
import ru.dmkuranov.hibernate_audit.util.MapFormatter;

import java.util.Map;

public class EntityState {
    private final EntityId entityId;
    private final Map<FieldDescription, FieldValue> propertyValues;

    public EntityState(EntityId entityId, Map<FieldDescription, FieldValue> propertyValues) {
        this.entityId = entityId;
        this.propertyValues = propertyValues;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public Map<FieldDescription, FieldValue> getPropertyValues() {
        return propertyValues;
    }

    @Override
    public String toString() {
        return MapFormatter.format(propertyValues, ", ", new MapFormatter.EntryFormatter<FieldDescription, FieldValue>() {
            @Override
            public String toString(FieldDescription key, FieldValue value) {
                return "[" + key.getFieldName() + "]=" + value.toString();
            }
        });
    }
}
