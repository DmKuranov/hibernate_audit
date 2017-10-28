package ru.dmkuranov.hibernate_audit.model.changeactions;

import ru.dmkuranov.hibernate_audit.inspector.EntityInspector;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;

public class ChangeEntityActionUpdate extends ChangeEntityActionCreate {
    protected EntityState stateBefore;

    public ChangeEntityActionUpdate(EntityId entityId, EntityState stateBefore, EntityState stateAfter) {
        super(entityId, stateAfter);
        this.stateBefore = stateBefore;
    }

    @Override
    public ChangeActionType getType() {
        return ChangeActionType.UPDATE;
    }

    @Override
    public String toString() {
        return getType()+" "+EntityInspector.getDelta(stateBefore, stateAfter).toString();
    }
}
