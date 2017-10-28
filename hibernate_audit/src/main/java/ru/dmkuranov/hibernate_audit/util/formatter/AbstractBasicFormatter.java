package ru.dmkuranov.hibernate_audit.util.formatter;

public abstract class AbstractBasicFormatter implements Formatter {
    private Class clazz;

    public AbstractBasicFormatter() {

    }
    public AbstractBasicFormatter(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean supportClass(Class clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }
}
