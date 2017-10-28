package ru.dmkuranov.hibernate_audit.inspector;

import org.apache.commons.lang3.StringUtils;
import ru.dmkuranov.hibernate_audit.inspector.model.FieldValueDelta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CollectionFieldValue {
    public static FieldValue build(FieldDescription fieldDescription, Object ownerObject) {
        Object collection = fieldDescription.getValue(ownerObject);
        if(collection instanceof Collection) {
            return new BasicCollectionFieldValue(fieldDescription, (Collection) collection);
        }
        if(collection == null) {
            return new BasicCollectionFieldValue(fieldDescription, Collections.emptyList());
        }
        throw new UnsupportedOperationException();
    }

    public static class BasicCollectionFieldValue extends FieldValue {
        private final ArrayList storedCollection = new ArrayList();
        private BasicCollectionFieldValue(FieldDescription fieldDescription, Collection originalSet) {
            super(fieldDescription, null);
            storedCollection.addAll(originalSet);
            // TODO: save state to make embedded modifiable objects support
        }
        @Override
        public boolean equals(Object object) {
            if(object instanceof BasicCollectionFieldValue) {
                BasicCollectionFieldValue other = (BasicCollectionFieldValue)object;
                return storedCollection.equals(other);
            }
            return false;
        }
        @Override
        public String toString() {
            return "["+ StringUtils.join(storedCollection, ", ")+"]";
        }

        public FieldValueDelta getDelta(FieldValue anotherValue) {
            return new SetCollectionFieldValueDelta(this, (BasicCollectionFieldValue) anotherValue);
        }

        private static class SetCollectionFieldValueDelta extends  FieldValueDelta<BasicCollectionFieldValue> {
            public SetCollectionFieldValueDelta(BasicCollectionFieldValue from, BasicCollectionFieldValue to) {
                super(from, to);
            }
            @Override
            public String toString() {
                return getFrom().toString()+"->"+getTo().toString();
            }
        }
    }
}
