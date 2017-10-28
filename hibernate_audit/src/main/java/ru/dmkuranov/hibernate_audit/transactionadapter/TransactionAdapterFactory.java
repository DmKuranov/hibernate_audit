package ru.dmkuranov.hibernate_audit.transactionadapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;

@Service
public class TransactionAdapterFactory {
    @Autowired
    private ApplicationContext applicationContext;

    private static final String trxNamePrefix="audited-trx-";

    public AuditingTransactionAdapter findOrCreateTransactionAdapter() {
        for (TransactionSynchronization synchronization : TransactionSynchronizationManager.getSynchronizations()) {
            if (synchronization instanceof AuditingTransactionAdapter) {
                return (AuditingTransactionAdapter) synchronization;
            }
        }
        AuditingTransactionAdapter instance = (AuditingTransactionAdapter) applicationContext
                .getBean("transactionAdapter", AuditingTransactionAdapter.class);
        String trxName = String.format("%1$s%2$tY.%2$tm.%2$td %2$tT.%2$tL", trxNamePrefix, new Date());
        instance.setTrxName(trxName);
        TransactionSynchronizationManager.registerSynchronization(instance);
        TransactionSynchronizationManager.setCurrentTransactionName(trxName);
        return instance;
    }
}
