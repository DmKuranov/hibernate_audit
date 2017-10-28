package ru.dmkuranov.hibernate_audit.inspector.model;

import ru.dmkuranov.hibernate_audit.util.MapFormatter;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityDelta {
    private final EntityId entityId;
    private final Map<Field, FieldValueDelta> fieldValueDeltaMap;

    public EntityDelta(EntityId entityId, Map<Field, FieldValueDelta> fieldValueDeltaMap) {
        this.entityId = entityId;
        this.fieldValueDeltaMap = fieldValueDeltaMap;
    }

    @Override
    public String toString() {
        String res = entityId.getClazz().getSimpleName()+"["+entityId.getId()+"]: (";
        res += MapFormatter.format(fieldValueDeltaMap, ", ", new MapFormatter.EntryFormatter<Field, FieldValueDelta>() {
            @Override
            public String toString(Field key, FieldValueDelta value) {
                return "["+key.getName()+"]"+value.toString();
            }
        });
        return res+")";
    }

}
