package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FieldExtractor {
    public List<String> getFieldNames(Class<?> clas) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> currentLayer = clas;
        while (currentLayer!= null){
            Field[] declaredFields = currentLayer.getDeclaredFields();
            for (Field field : declaredFields) {
                fieldNames.add(field.getName());
            }
            currentLayer = currentLayer.getSuperclass();
        }
        return fieldNames;
    }
}
