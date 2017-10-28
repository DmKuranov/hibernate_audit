package ru.dmkuranov.hibernate_audit;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmkuranov.hibernate_audit.service.EntityEventListener;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Service
public class HibernateEntityEventListener implements PostLoadEventListener, PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener,
        PostCollectionRecreateEventListener, PostCollectionRemoveEventListener, PostCollectionUpdateEventListener {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private EntityEventListener entityEventListener;


    @PostConstruct
    public void postConstruct() {
        EventListenerRegistry eventListenerRegistry = ((SessionFactoryImpl)((HibernateEntityManagerFactory)entityManagerFactory).getSessionFactory())
                .getServiceRegistry().getService(EventListenerRegistry.class);
        eventListenerRegistry.appendListeners(EventType.POST_LOAD, this);
        eventListenerRegistry.appendListeners(EventType.POST_INSERT, this);
        eventListenerRegistry.appendListeners(EventType.POST_UPDATE, this);
        eventListenerRegistry.appendListeners(EventType.POST_DELETE, this);
        eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_RECREATE, this);
        eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_UPDATE, this);
        eventListenerRegistry.appendListeners(EventType.POST_COLLECTION_REMOVE, this);
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        entityEventListener.onEntityLoad(event.getEntity());
    }

    @Override
    public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        entityEventListener.onEntityUpdate(event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        entityEventListener.onEntityUpdate(event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        entityEventListener.onEntityUpdate(event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        entityEventListener.onEntityDelete(event.getEntity());
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        entityEventListener.onEntityCreate(event.getEntity());
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        entityEventListener.onEntityUpdate(event.getEntity());
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }
}
