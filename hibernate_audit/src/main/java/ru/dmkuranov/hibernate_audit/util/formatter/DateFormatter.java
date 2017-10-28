package ru.dmkuranov.hibernate_audit.util.formatter;

import java.util.Date;

public class DateFormatter extends AbstractBasicFormatter {

    public DateFormatter() {
        super(Date.class);
    }

    @Override
    public String format(Object object) {
        return String.format("%1$tY.%1$tm.%1$td %1$tT", (Date) object);
    }
}
