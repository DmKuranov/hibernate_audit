package ru.dmkuranov.hibernate_audit.model.changeactions;

import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;

public class ChangeEntityActionCreate extends ChangeEntityActionAbstract {
    protected EntityState stateAfter;

    public ChangeEntityActionCreate(EntityId entityId, EntityState stateAfter) {
        super(entityId);
        this.stateAfter = stateAfter;
    }

    @Override
    public ChangeActionType getType() {
        return ChangeActionType.CREATE;
    }

    public void updateState(EntityState stateAfter) {
        this.stateAfter = stateAfter;
    }

    @Override
    public String toString() {
        return super.toString()+": ("+stateAfter+")";
    }
}
