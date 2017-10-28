package ru.dmkuranov.hibernate_audit.util.comparator;

import java.util.Comparator;
import java.util.Map;

public class ComparatorService {
    private Map<Class, Comparator> comparatorMap;

    public ComparatorService() {
    }

    public ComparatorService(Map<Class, Comparator> comparatorMap) {
        this.comparatorMap = comparatorMap;
    }

    public boolean compare(Object object1, Object object2) {
        Comparator comparator = comparatorMap.get(object1.getClass());
        if(comparator!=null) {
            return comparator.compare(object1, object2)==0;
        } else {
            return object1.equals(object2);
        }
    }
}
