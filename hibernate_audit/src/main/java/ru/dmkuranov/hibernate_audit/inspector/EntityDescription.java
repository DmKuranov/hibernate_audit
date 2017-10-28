package ru.dmkuranov.hibernate_audit.inspector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityDescription {
    private EntityInspector entityInspector;
    private final Class clazz;
    private List<FieldDescription> fieldDescriptions;
    private List<FieldDescription> persistentFieldDescriptions;
    private FieldDescription idField;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger log = LoggerFactory.getLogger(EntityDescription.class);

    public EntityDescription(EntityInspector entityInspector, Class clazz) {
        this.entityInspector = entityInspector;
        this.clazz = clazz;
    }

    List<FieldDescription> getFieldDescriptions() {
        lock.readLock().lock();
        try {
            if (fieldDescriptions == null) {
                lock.readLock().unlock();
                initFieldDescriptions();
                lock.readLock().lock();
            }
            return fieldDescriptions;
        } finally {
            lock.readLock().unlock();
        }
    }

    List<FieldDescription> getPersistentFieldDescriptions() {
        lock.readLock().lock();
        try {
            if (fieldDescriptions == null) {
                lock.readLock().unlock();
                initFieldDescriptions();
                lock.readLock().lock();
            }
            return persistentFieldDescriptions;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Class getClazz() {
        return clazz;
    }

    private void initFieldDescriptions() {
        lock.writeLock().lock();
        try {
            if (fieldDescriptions == null) {
                if(log.isDebugEnabled()) {
                    log.debug("Initializing info for class "+clazz.getCanonicalName());
                }
                fieldDescriptions = new ArrayList<FieldDescription>();
                persistentFieldDescriptions = new ArrayList<FieldDescription>();
                collectFieldsFromClassHierarchy(clazz);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void collectFieldsFromClassHierarchy(Class clazz) {
        Class ancestor = (Class) clazz.getGenericSuperclass();
        if(ancestor!=null && !Object.class.equals(ancestor)) {
            collectFieldsFromClassHierarchy(ancestor);
        }
        for (Field field : clazz.getDeclaredFields()) {
            FieldDescription fieldDescription = new FieldDescription(this, field);
            fieldDescriptions.add(fieldDescription);
            if(fieldDescription.isPersistent()) {
                persistentFieldDescriptions.add(fieldDescription);
                if(fieldDescription.isId()) {
                    idField = fieldDescription;
                }
            }
        }
    }

    @Override
    public boolean equals(Object anotherObject) {
        if(anotherObject instanceof EntityDescription) {
            EntityDescription another = (EntityDescription) anotherObject;
            return clazz.equals(another.getClazz());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return clazz.getCanonicalName().hashCode();
    }

    @Override
    public String toString() {
        return "Description of "+clazz.getCanonicalName();
    }

    public FieldDescription getIdField() {
        lock.readLock().lock();
        try {
            if (fieldDescriptions == null) {
                lock.readLock().unlock();
                initFieldDescriptions();
                lock.readLock().lock();
            }
            return idField;
        } finally {
            lock.readLock().unlock();
        }
    }

    public EntityInspector getEntityInspector() {
        return entityInspector;
    }
}
