package ru.dmkuranov.hibernate_audit.transactionadapter;

import org.springframework.transaction.support.TransactionSynchronization;

public interface AuditingTransactionAdapter extends TransactionSynchronization {
    void processCreate(Object entity);
    void processLoad(Object entity);
    void processUpdate(Object entity);
    void processDelete(Object entity);
    String getTrxName();
    void setTrxName(String trxName);
}
