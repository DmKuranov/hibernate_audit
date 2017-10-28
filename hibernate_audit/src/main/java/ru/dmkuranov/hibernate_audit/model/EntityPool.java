package ru.dmkuranov.hibernate_audit.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityState;
import ru.dmkuranov.hibernate_audit.inspector.EntityInspector;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.util.Cleanupable;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityPool {
    private static final Logger log = LoggerFactory.getLogger(EntityPool.class);

    @Autowired
    private EntityInspector entityInspector;

    private Map<EntityId, PoolEntry> poolMap = new HashMap<EntityId, PoolEntry>();
    private static final ReferenceQueue referenceQueue = new ReferenceQueue();

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Reference reference = referenceQueue.remove();
                        if (reference instanceof Cleanupable) {
                            ((Cleanupable) reference).cleanup();
                        }

                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "EntityPool-ReferenceQueue-Remover").start();
    }

    public EntityId addEntity(Object entity) {
        EntityState state = entityInspector.buildState(entity);
        EntityId entityId = state.getEntityId();
        PoolEntry poolEntry = poolMap.get(entityId);
        if(poolEntry==null || poolEntry.getEntity()==null) {
            poolMap.put(entityId, new PoolEntryWeak(state, entity, this, referenceQueue));
        }
        return entityId;
    }

    public void pinEntity(EntityId entityId) {
        poolMap.get(entityId).pin();
    }

    public void removeEntity(EntityId entityId) {
        poolMap.remove(entityId);
    }

    public EntityState getState(EntityId entityId) {
        return poolMap.get(entityId).getEntityState();
    }

}
