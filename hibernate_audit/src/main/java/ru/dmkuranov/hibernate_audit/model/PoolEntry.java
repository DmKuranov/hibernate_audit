package ru.dmkuranov.hibernate_audit.model;

import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;

public class PoolEntry {
    private final EntityState state;
    private Object entity;

    public PoolEntry(EntityState state, Object entity) {
        this.state = state;
        this.entity = entity;
    }

    public EntityState getEntityState() {
        return state;
    }

    public Object getEntity() {
        return entity;
    }

    public void pin() {
    }

}
