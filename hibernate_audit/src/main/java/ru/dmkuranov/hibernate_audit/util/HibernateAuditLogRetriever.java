package ru.dmkuranov.hibernate_audit.util;

import ru.dmkuranov.hibernate_audit.transactionadapter.AuditingTransactionAdapterImpl;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class HibernateAuditLogRetriever {
    private DataSource dataSource;

    public HibernateAuditLogRetriever(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public Map<String, List<String>> getHistory(String entityType, String entityId) {
        try {
            PreparedStatement preparedStatement = dataSource.getConnection()
                    .prepareStatement("select " + AuditingTransactionAdapterImpl.fieldNameTstamp + ", "
                            + AuditingTransactionAdapterImpl.fieldNameSubject + ", "
                            + AuditingTransactionAdapterImpl.fieldNameDescription
                            + " from " + AuditingTransactionAdapterImpl.tableName
                            + " where " + AuditingTransactionAdapterImpl.fieldNameDescription
                            + " like ? escape '\\' order by tstamp");
            preparedStatement.setString(1, "%"+entityType+"\\["+entityId+"\\]%");
            ResultSet resultSet = preparedStatement.executeQuery();
            Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
            while(resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp(AuditingTransactionAdapterImpl.fieldNameTstamp);
                Date timestampDt = new Date(timestamp.getTime());
                dateFormat.format(timestampDt);
                List<String> item = new ArrayList<String>();
                item.add(resultSet.getString(AuditingTransactionAdapterImpl.fieldNameDescription));
                item.add(resultSet.getString(AuditingTransactionAdapterImpl.fieldNameSubject));
                result.put(dateFormat.format(timestampDt), item);
            }
            return result;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
