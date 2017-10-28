package ru.dmkuranov.hibernate_audit.inspector.model;

import ru.dmkuranov.hibernate_audit.inspector.FieldValue;

public class FieldValueDelta <T extends FieldValue> {
    private final T from;
    private final T to;

    public FieldValueDelta(T from, T to) {
        this.from = from;
        this.to = to;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    @Override
    public String toString() {
        return getFrom().toString()+"->"+getTo().toString();
    }
}
