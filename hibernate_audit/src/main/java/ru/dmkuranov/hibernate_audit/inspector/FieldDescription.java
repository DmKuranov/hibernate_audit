package ru.dmkuranov.hibernate_audit.inspector;

import javax.persistence.*;
import java.lang.reflect.Field;

public class FieldDescription {
    private final EntityDescription entityDescription;
    private final String fieldName;
    private Field field;
    private final Class clazz;

    public FieldDescription(EntityDescription entityDescription, Field field) {
        this.entityDescription = entityDescription;
        this.field = field;
        fieldName = field.getName();
        clazz = field.getType();
    }

    public boolean isPersistent() {
        return isColumn() || isLink() || isEmbedded() || isElementCollection();
    }
    public boolean isColumn() {
        return field.isAnnotationPresent(Column.class) && !isElementCollection();
    }

    public boolean isLink() {
        return field.isAnnotationPresent(ManyToOne.class)
                || (field.isAnnotationPresent(OneToOne.class) && !isInverseSide(OneToOne.class))
                || (field.isAnnotationPresent(OneToMany.class) && !isInverseSide(OneToMany.class));
    }
    public boolean isId() {
        return field.isAnnotationPresent(Id.class);
    }
    public boolean isEmbedded() {
        return field.isAnnotationPresent(Embedded.class);
    }
    public boolean isElementCollection() {
        return field.isAnnotationPresent(ElementCollection.class) && field.isAnnotationPresent(CollectionTable.class);
    }
    public Object getValue(Object entity) {
        try {
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }
            if(entity!=null) {
                return field.get(entity);
            } else {
                return null;
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object anotherObject) {
        if(anotherObject instanceof FieldDescription) {
            FieldDescription another = (FieldDescription) anotherObject;
            return entityDescription.getClazz().equals(another.getEntityDescription().getClazz())
                    && fieldName.equals(another.getFieldName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (getEntityDescription().getClazz().hashCode()*13) + fieldName.hashCode();
    }

    @Override
    public String toString() {
        return "Field "+fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public EntityDescription getEntityDescription() {
        return entityDescription;
    }

    public Field getField() {
        return field;
    }

    private boolean isInverseSide(Class relationAnnotationClass) {
        if(OneToMany.class.equals(relationAnnotationClass)) {
            OneToMany annotation = field.getAnnotation(OneToMany.class);
            if(annotation!=null) {
                return !"".equals(annotation.mappedBy());
            }
        }
        if(OneToOne.class.equals(relationAnnotationClass)) {
            OneToOne annotation = field.getAnnotation(OneToOne.class);
            if(annotation!=null) {
                return !"".equals(annotation.mappedBy());
            }
        }
        return false;
    }

    public Class getClazz() {
        return clazz;
    }
}
