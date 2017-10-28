package ru.dmkuranov.hibernate_audit.util.comparator;

import java.util.Comparator;

/**
 * Сравнивает при помощи Comparable#compareTo
 */
public class ComparableComparator implements Comparator<Comparable> {
    @Override
    public int compare(Comparable o1, Comparable o2) {
        if(o1==null) {
            return -1;
        }
        if(o2==null) {
            return 1;
        }
        return o1.compareTo(o2);
    }
}
