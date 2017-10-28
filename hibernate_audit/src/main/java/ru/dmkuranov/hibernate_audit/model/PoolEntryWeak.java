package ru.dmkuranov.hibernate_audit.model;

import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.util.Cleanupable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class PoolEntryWeak extends PoolEntry {
    private final WeakReference reference;
    private Object object = null;

    public PoolEntryWeak(EntityState state, Object entity, EntityPool entityPool, ReferenceQueue referenceQueue) {
        super(state, null);
        reference = new WeakReferenceCleanupable(entity, referenceQueue, entityPool, state.getEntityId());
    }

    @Override
    public void pin() {
        object = reference.get();
    }

    @Override
    public Object getEntity() {
        return object != null ? object : reference.get();
    }

    private static class WeakReferenceCleanupable extends WeakReference implements Cleanupable {
        private final EntityPool entityPool;
        private final EntityId entityId;
        public WeakReferenceCleanupable(Object referent, ReferenceQueue q, EntityPool entityPool, EntityId entityId) {
            super(referent, q);
            this.entityPool = entityPool;
            this.entityId = entityId;
        }

        @Override
        public void cleanup() {
            entityPool.removeEntity(entityId);
        }
    }
}
