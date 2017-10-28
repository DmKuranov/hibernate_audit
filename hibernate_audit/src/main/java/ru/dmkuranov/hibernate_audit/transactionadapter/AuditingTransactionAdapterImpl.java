package ru.dmkuranov.hibernate_audit.transactionadapter;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import ru.dmkuranov.aspects_util.spring.disableexecutioninsessiononexception.DisableExecutionInSessionOnException;
import ru.dmkuranov.aspects_util.spring.emailcomplaintonexception.EmailComplaintOnException;
import ru.dmkuranov.hibernate_audit.inspector.EntityInspector;
import ru.dmkuranov.hibernate_audit.inspector.model.EntityId;
import ru.dmkuranov.hibernate_audit.model.ChangeEntityActionLog;
import ru.dmkuranov.hibernate_audit.model.EntityPool;
import ru.dmkuranov.hibernate_audit.model.changeactions.ChangeEntityActionAbstract;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Service("transactionAdapter")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@DisableExecutionInSessionOnException("jpa-audit-logging")
@EmailComplaintOnException(value = "dmku@1c.ru", subjectPrefix = "Audit exception")
public class AuditingTransactionAdapterImpl extends TransactionSynchronizationAdapter implements AuditingTransactionAdapter {
    @Autowired
    private EntityInspector inspector;
    @Autowired
    private EntityPool entityPool;
    @Autowired
    private EntityManager entityManager;

    private String trxName;
    private ChangeEntityActionLog changeLog = new ChangeEntityActionLog();
    private static final Logger log = LoggerFactory.getLogger(AuditingTransactionAdapterImpl.class);
    public static final String tableName = "Entity_Change_Log";
    public static final String fieldNameDescription = "change_desc";
    public static final String fieldNameTstamp = "tstamp";
    public static final String fieldNameSubject = "subject";
    private static final String sqlText = "insert into " + tableName + " (trx_name, "+ fieldNameTstamp +", "+ fieldNameSubject +", " + fieldNameDescription + ") values (?, ?, ?, ?)";

    @Override
    public void beforeCommit(boolean readOnly) {
        final String subject;
        final String autorities;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            subject = auth.getPrincipal().toString();
            autorities = auth.getAuthorities().toString();
        } else {
            subject = "Anonymous";
            autorities = subject;
        }
        final Session session = entityManager.unwrap(Session.class);
        session.flush();
        if (!changeLog.getActions().isEmpty()) {
            final java.util.Date now = new java.util.Date();
            final String changeLogString = changeLog.toString();
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    PreparedStatement stmt = connection.prepareStatement(sqlText);
                    stmt.setString(1, trxName);
                    stmt.setTimestamp(2, new Timestamp(now.getTime()));
                    stmt.setString(3, subject + ", authorities=" + autorities);
                    stmt.setString(4, changeLogString);
                    stmt.execute();
                }
            });
            if (log.isDebugEnabled()) {
                log.debug("AUDITLOG: " + now + " " + trxName + " " + auth + " " + subject + " " + changeLogString);
            }
        }
    }

    @Override
    public void afterCommit() {
        if (log.isDebugEnabled() && !changeLog.getActions().isEmpty()) {
            log.debug("COMMITED changes by " + trxName + ": " + changeLog.toString());
        }
    }

    @Override
    public void processCreate(Object entity) {
        entityPool.addEntity(entity);
        EntityId entityId = pinEntity(entity);
        changeLog.processCreate(entityId, inspector.buildState(entity));
    }

    @Override
    public void processLoad(Object entity) {
        entityPool.addEntity(entity);
    }

    @Override
    public void processUpdate(Object entity) {
        EntityId entityId = pinEntity(entity);
        changeLog.processUpdate(entityId, entityPool.getState(entityId), inspector.buildState(entity));
    }

    @Override
    public void processDelete(Object entity) {
        EntityId entityId = pinEntity(entity);
        changeLog.processDelete(entityId);
    }

    @Override
    public String toString() {
        String result = "changes by "+getTrxName()+": ";
        for(ChangeEntityActionAbstract action : changeLog.getActions()) {
            result+=action+", ";
        }
        return result;
    }


    private EntityId pinEntity(Object entity) {
        EntityId entityId = inspector.getEntityId(entity);
        entityPool.pinEntity(entityId);
        return entityId;
    }

    @Override
    public String getTrxName() {
        return trxName;
    }

    @Override
    public void setTrxName(String trxName) {
        this.trxName = trxName;
    }
}