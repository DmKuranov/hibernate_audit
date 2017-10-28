package ru.dmkuranov.hibernate_audit.util.formatter;

public interface Formatter {
    String format(Object object);
    boolean supportClass(Class clazz);
}
