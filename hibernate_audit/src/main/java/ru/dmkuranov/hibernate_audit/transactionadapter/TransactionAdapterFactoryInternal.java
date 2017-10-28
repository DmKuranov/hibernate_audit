package ru.dmkuranov.hibernate_audit.transactionadapter;

public interface TransactionAdapterFactoryInternal {
    AuditingTransactionAdapter getInstance();
}
