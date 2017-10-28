package ru.dmkuranov.hibernate_audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import ru.dmkuranov.hibernate_audit.util.comparator.ComparableComparator;
import ru.dmkuranov.hibernate_audit.util.comparator.ComparatorService;
import ru.dmkuranov.hibernate_audit.util.formatter.DateFormatter;
import ru.dmkuranov.hibernate_audit.util.formatter.Formatter;
import ru.dmkuranov.hibernate_audit.util.formatter.FormatterService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;

@Configuration
@ComponentScan(basePackages="ru.dmkuranov.hibernate_audit")
@ImportResource({"classpath:spring/aspects_util.xml"})
public class HibernateAuditConfig {

    @Bean(name="formatterService")
    public FormatterService formatterService() {
        return new FormatterService(Collections.<Formatter>singletonList(new DateFormatter()));
    }

    @Bean(name="comparatorService")
    public ComparatorService comparatorService() {
        return new ComparatorService(Collections.<Class, Comparator>singletonMap(BigDecimal.class, new ComparableComparator()));
    }
}
