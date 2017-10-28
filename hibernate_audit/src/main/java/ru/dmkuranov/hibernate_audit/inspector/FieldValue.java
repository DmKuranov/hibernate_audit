package ru.dmkuranov.hibernate_audit.inspector;

import ru.dmkuranov.hibernate_audit.inspector.model.FieldValueDelta;
import ru.dmkuranov.hibernate_audit.util.MapFormatter;
import ru.dmkuranov.hibernate_audit.util.comparator.ComparatorService;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public abstract class FieldValue {
    protected FieldDescription fieldDescription;
    protected FieldValue(FieldDescription fieldDescription, Object ownerObject) {
        this.fieldDescription = fieldDescription;
    }
    public FieldValueDelta getDelta(FieldValue anotherValue) {
        return new FieldValueDelta(this, anotherValue);
    }

    public static FieldValue build(FieldDescription fieldDescription, Object ownerObject) {
        if(fieldDescription.isLink()) {
            return new LinkFieldValue(fieldDescription, ownerObject);
        }
        if(fieldDescription.isEmbedded()) {
            return new EmbeddedFieldValue(fieldDescription, ownerObject);
        }
        if(fieldDescription.isElementCollection()) {
            return CollectionFieldValue.build(fieldDescription, ownerObject);
        }
        return new BasicFieldValue(fieldDescription, ownerObject);
    }
    private static class LinkFieldValue extends FieldValue {
        private Serializable id;
        private EntityDescription linkedEntityDescription;
        private LinkFieldValue(FieldDescription fieldDescription, Object ownerObject) {
            super(fieldDescription, ownerObject);
            Object entity = fieldDescription.getValue(ownerObject);
            EntityInspector entityInspector = fieldDescription.getEntityDescription().getEntityInspector();
            linkedEntityDescription = entityInspector.getDescription(fieldDescription.getClazz());
            id = (Serializable) linkedEntityDescription.getIdField().getValue(entity);
        }
        @Override
        public String toString() {
            return "Entity("+linkedEntityDescription.getClazz().getSimpleName()+"["+id+"]";
        }
        @Override
        public boolean equals(Object object) {
            if(object instanceof LinkFieldValue) {
                LinkFieldValue another = (LinkFieldValue) object;
                if(id==null) {
                    if(another.id==null) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return id.equals(another.id);
            }
            return false;
        }
    }
    public static class EmbeddedFieldValue extends FieldValue {
        private EntityDescription embeddedEntityDescription;
        private Map<FieldDescription, FieldValue> embeddedEntityProperties;
        public EmbeddedFieldValue(FieldDescription fieldDescription, Object ownerObject) {
            super(fieldDescription, ownerObject);
            embeddedEntityDescription = fieldDescription.getEntityDescription().getEntityInspector().getDescription(fieldDescription.getField().getType());
            embeddedEntityProperties = new HashMap<FieldDescription, FieldValue>();
            for(FieldDescription embeddedFieldDescription : embeddedEntityDescription.getPersistentFieldDescriptions()) {
                embeddedEntityProperties.put(embeddedFieldDescription, FieldValue.build(embeddedFieldDescription, fieldDescription.getValue(ownerObject)));
            }
        }
        @Override
        public boolean equals(Object object) {
            if(object instanceof EmbeddedFieldValue) {
                EmbeddedFieldValue another = (EmbeddedFieldValue) object;
                for(Map.Entry<FieldDescription, FieldValue> entry: embeddedEntityProperties.entrySet()) {
                    FieldDescription fd = entry.getKey();
                    if(!embeddedEntityProperties.get(fd).equals(another.embeddedEntityProperties.get(fd))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        @Override
        public String toString() {
            String out = "Embedded "+embeddedEntityDescription.getClazz().getSimpleName()+" (";
            for(Map.Entry<FieldDescription, FieldValue> entry: embeddedEntityProperties.entrySet()) {
                FieldDescription fieldDescription = entry.getKey();
                out+=fieldDescription+"="+entry.getValue().toString()+", ";
            }
            return out+")";
        }
        @Override
        public FieldValueDelta getDelta(FieldValue anotherValue) {
            return new EmbeddedFieldValueDelta(this, (EmbeddedFieldValue) anotherValue);
        }
        private static class EmbeddedFieldValueDelta extends FieldValueDelta<EmbeddedFieldValue> {
            public EmbeddedFieldValueDelta(EmbeddedFieldValue from, EmbeddedFieldValue to) {
                super(from, to);
            }
            @Override
            public String toString() {
                String delta = "(";
                Map<Field, FieldValueDelta> propsDelta = EntityInspector.getChangedFields(getFrom().embeddedEntityProperties, getTo().embeddedEntityProperties);
                delta += MapFormatter.format(propsDelta, ", ", new MapFormatter.EntryFormatter<Field, FieldValueDelta>() {
                    @Override
                    public String toString(Field key, FieldValueDelta value) {
                        return "["+key.getName()+"]"+value.toString();
                    }
                });
                return delta+")";
            }
        }
    }
    public static class BasicFieldValue extends FieldValue {
        private Object value;
        public BasicFieldValue(FieldDescription fieldDescription, Object ownerObject) {
            super(fieldDescription, ownerObject);
            value = fieldDescription.getValue(ownerObject);
        }
        @Override
        public String toString() {
            return this.fieldDescription.getEntityDescription().getEntityInspector().getFormatter().format(value);
        }
        @Override
        public boolean equals(Object object) {
            if(object instanceof BasicFieldValue) {
                BasicFieldValue another = (BasicFieldValue) object;
                if(value==null) {
                    if(another.value==null) {
                        return true;
                    } else {
                        return false;
                    }
                }
                ComparatorService comparator = fieldDescription.getEntityDescription().getEntityInspector().getComparator();
                return comparator.compare(value, another.value);
            }
            return false;
        }
    }
}
