package ru.dmkuranov.hibernate_audit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmkuranov.hibernate_audit.transactionadapter.AuditingTransactionAdapter;
import ru.dmkuranov.hibernate_audit.transactionadapter.TransactionAdapterFactory;

@Service
public class EntityEventListener {
    @Autowired
    private TransactionAdapterFactory transactionAdapterFactory;

    public void onEntityLoad(Object entity) {
        getAdapter().processLoad(entity);
    }

    public void onEntityUpdate(Object entity) {
        getAdapter().processUpdate(entity);
    }

    public void onEntityCreate(Object entity) {
        getAdapter().processCreate(entity);
    }

    public void onEntityDelete(Object entity) {
        getAdapter().processDelete(entity);
    }

    private AuditingTransactionAdapter getAdapter() {
        return transactionAdapterFactory.findOrCreateTransactionAdapter();
    }
}
