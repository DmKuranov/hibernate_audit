package test.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Entity_Change_Log")
public class AuditLogEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "trx_name")
    private String trxName;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date tstamp;

    @Column
    private String subject;

    @Column(name="change_desc")
    private String changeDesc;

    public String getTrxName() {
        return trxName;
    }

    public void setTrxName(String trxName) {
        this.trxName = trxName;
    }

    public Date getTstamp() {
        return tstamp;
    }

    public void setTstamp(Date tstamp) {
        this.tstamp = tstamp;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChangeDesc() {
        return changeDesc;
    }

    public void setChangeDesc(String changeDesc) {
        this.changeDesc = changeDesc;
    }
}
