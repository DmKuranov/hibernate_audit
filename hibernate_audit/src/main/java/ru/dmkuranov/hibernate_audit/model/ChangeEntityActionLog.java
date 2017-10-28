package ru.dmkuranov.hibernate_audit.model;

import org.apache.commons.lang3.StringUtils;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;
import ru.dmkuranov.hibernate_audit.model.changeactions.*;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChangeEntityActionLog {
    private Map<Integer, ChangeEntityActionAbstract> actions = new TreeMap<Integer, ChangeEntityActionAbstract>();
    private Map<EntityId, Collection<ChangeEntityActionAbstract>> affectedActions = new HashMap<EntityId, Collection<ChangeEntityActionAbstract>>();
    AtomicInteger actionsCount = new AtomicInteger(0);

    public void processCreate(EntityId entityId, EntityState state) {
        doAddAction(new ChangeEntityActionCreate(entityId, state));
    }

    public void processUpdate(EntityId entityId, EntityState stateBefore, EntityState stateAfter) {
        ChangeEntityActionUpdate action = new ChangeEntityActionUpdate(entityId, stateBefore, stateAfter);
        Collection<ChangeEntityActionAbstract> affected = affectedActions.get(action.getEntityId());
        if (affected != null) {
            for (ChangeEntityActionAbstract affectedAction : affected) {
                if (affectedAction.getType() == ChangeActionType.DELETE) {
                    throw new UnsupportedOperationException("Cannot update entity after delete");
                }
                if (affectedAction.getType() == ChangeActionType.UPDATE
                        || affectedAction.getType() == ChangeActionType.CREATE) {
                    ((ChangeEntityActionCreate) affectedAction).updateState(stateAfter);
                    return;
                }
            }
        }
        doAddAction(action);
    }

    public void processDelete(EntityId entityId) {
        ChangeEntityActionDelete action = new ChangeEntityActionDelete(entityId);
        Collection<ChangeEntityActionAbstract> affected = affectedActions.get(action.getEntityId());
        if (affected != null) {
            for (ChangeEntityActionAbstract affectedAction : affected) {
                if (affectedAction.getType() == ChangeActionType.DELETE) {
                    throw new UnsupportedOperationException("Cannot delete entity after delete");
                }
            }
        }
        doAddAction(action);
    }

    private void doAddAction(ChangeEntityActionAbstract action) {
        actions.put(actionsCount.getAndIncrement(), action);
        affectedActions.put(action.getEntityId(), getSingleAction(action));
    }

    private static List<ChangeEntityActionAbstract> getSingleAction(ChangeEntityActionAbstract action) {
        List<ChangeEntityActionAbstract> changeEntityActions = new ArrayList<ChangeEntityActionAbstract>();
        changeEntityActions.add(action);
        return changeEntityActions;
    }

    public Collection<ChangeEntityActionAbstract> getActions() {
        return actions.values();
    }

    @Override
    public String toString() {
        return StringUtils.join(getActions(), "; ");
    }
}
