package ru.dmkuranov.hibernate_audit.model.changeactions;

import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;

public class ChangeEntityActionDelete extends ChangeEntityActionAbstract {

    public ChangeEntityActionDelete(EntityId entityId) {
        super(entityId);
    }

    @Override
    public ChangeActionType getType() {
        return ChangeActionType.DELETE;
    }
}
