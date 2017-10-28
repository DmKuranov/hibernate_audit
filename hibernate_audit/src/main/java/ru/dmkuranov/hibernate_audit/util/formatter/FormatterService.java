package ru.dmkuranov.hibernate_audit.util.formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class FormatterService {
    List<Formatter> formatterList;

    @Autowired(required = false)
    @Qualifier("customFormatterList")
    List<Formatter> customFormatterList;

    public FormatterService() {

    }
    public FormatterService(List<Formatter> formatterList) {
        this.formatterList = formatterList;
    }

    public String format(Object object) {
        if(object==null) {
            return "null";
        }
        Class clazz = object.getClass();
        if(customFormatterList!=null) {
            for(Formatter formatter: customFormatterList) {
                if(formatter.supportClass(clazz)) {
                    return formatter.format(object);
                }
            }
        }
        for(Formatter formatter: formatterList) {
            if(formatter.supportClass(clazz)) {
                return formatter.format(object);
            }
        }
        return object.toString();
    }
}
