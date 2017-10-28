package test.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.dmkuranov.hibernate_audit.HibernateAuditConfig;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = {"test.repository"}, entityManagerFactoryRef = "entityManagerFactoryTest", transactionManagerRef = "transactionManagerTest")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ComponentScan(basePackages = "test", excludeFilters = {@ComponentScan.Filter(Controller.class)})
@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
@EnableAspectJAutoProxy
@Import(HibernateAuditConfig.class)
public class SpringTestContext {
    @Bean(name = "loadTimeWeaver")
    public LoadTimeWeaver customLoadTimeWeaver() {
        return new InstrumentationLoadTimeWeaver();
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean(name = "entityManagerFactoryTest")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform(dialect().getClass().getName());

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("test.domain");
        factory.setDataSource(dataSource());
        factory.setLoadTimeWeaver(customLoadTimeWeaver());

        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(AvailableSettings.DIALECT, dialect().getClass().getName());
        //propertyMap.put("hibernate.connection.characterEncoding", "UTF-8");
        //propertyMap.put("hibernate.connection.CharSet", "UTF-8");
        //propertyMap.put("hibernate.connection.useUnicode", true);
        propertyMap.put(AvailableSettings.GENERATE_STATISTICS, false);
        propertyMap.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, false);
        propertyMap.put(AvailableSettings.USE_QUERY_CACHE, false);
        propertyMap.put(AvailableSettings.SHOW_SQL, true);
        propertyMap.put(AvailableSettings.FORMAT_SQL, true);
        propertyMap.put(AvailableSettings.HBM2DDL_AUTO, "create");
        factory.setJpaPropertyMap(propertyMap);

        return factory;
    }

    @Bean
    public Dialect dialect() {
        return new H2Dialect();
    }

    @Bean(name = "transactionManagerTest")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getNativeEntityManagerFactory());
        return txManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
