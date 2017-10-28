package ru.dmkuranov.hibernate_audit.model.changeactions;

import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;

public abstract class ChangeEntityActionAbstract {
    protected final EntityId entityId;

    protected ChangeEntityActionAbstract(EntityId entityId) {
        this.entityId = entityId;
    }

    public abstract ChangeActionType getType();

    public EntityId getEntityId() {
        return entityId;
    }

    @Override
    public String toString() {
        return getType()+" "+getEntityId().toString();
    }
}
