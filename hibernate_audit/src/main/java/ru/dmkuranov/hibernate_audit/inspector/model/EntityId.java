package ru.dmkuranov.hibernate_audit.inspector.model;

import java.io.Serializable;

public class EntityId implements Serializable {
    private Class clazz;
    private Serializable id;

    public EntityId(Class clazz, Serializable id) {
        this.clazz = clazz;
        this.id = id;
    }

    public Class getClazz() {
        return clazz;
    }

    public Serializable getId() {
        return id;
    }

    @Override
    public String toString() {
        return clazz.getSimpleName()+"["+id+"]";
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof EntityId) {
            EntityId other = (EntityId) object;
            return clazz.equals(other.clazz) && id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return clazz.hashCode()*13+id.hashCode();
    }
}
