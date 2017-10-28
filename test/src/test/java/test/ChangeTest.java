package test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import test.config.SpringTestParent;
import test.repository.AuditLogEntityRepository;
import test.repository.BasicEntityRepository;
import test.repository.ChildEntityRepository;
import test.repository.ComplexEntityRepository;
import test.service.EntityManipulationService;

public abstract class ChangeTest extends SpringTestParent {
    @Autowired
    protected BasicEntityRepository entityRepository;
    @Autowired
    protected AuditLogEntityRepository auditLogEntityRepository;
    @Autowired
    protected EntityManipulationService entityManipulationService;
    @Autowired
    protected ComplexEntityRepository complexEntityRepository;
    @Autowired
    protected ChildEntityRepository childEntityRepository;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Очищаем данные
     */
    @Before
    public void setUp() {
        jdbcTemplate.update("truncate table Entity_Change_Log");
        jdbcTemplate.update("truncate table BasicEntity");
        jdbcTemplate.update("truncate table ComplexToSetOfStrings");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("truncate table ComplexEntity");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
