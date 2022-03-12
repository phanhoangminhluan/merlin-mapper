package com.merlin.mapper.handler;


import com.merlin.mapper.MerlinMapper;
import com.merlin.mapper.annotations.MappingField;
import com.merlin.mapper.annotations.SourceFieldConfig;
import com.merlin.mapper.annotations.TargetFieldConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

public class MiluMapperUtils {
    static Logger logger = Logger.getLogger(MerlinMapper.class.getName());

    public static ArrayList<Field> getAllAnnotatedFields(Object source, Class<? extends Annotation> annotation) {
        Field[] fields = source.getClass().getDeclaredFields();
        ArrayList<Field> annotatedFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                annotatedFields.add(field);
            }
        }
        fields = source.getClass().getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    public static String toUppercaseTheFirstLetter(String fieldName) {
        return (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1);
    }

    public static String getSourceFieldName(Field targetField, Class targetClass) {
        String targetFieldName = targetField.getName();

        if (targetField.isAnnotationPresent(MappingField.class)) {
//            String targetAnnotationFieldName = targetField.getAnnotation(MappingField.class).sourceFieldName();
//            targetFieldName = targetAnnotationFieldName.isEmpty() ? targetFieldName : targetAnnotationFieldName;
        }
        return targetFieldName;
    }


    public static String getSourceGetterMethodName(Field targetField, Class desiredSourceClass, Class targetClass) throws Exception {
        String targetFieldName = targetField.getName();
        MappingField mappingField = targetField.getDeclaredAnnotation(MappingField.class);
        String defaultGetterMethod = "get" + MiluMapperUtils.toUppercaseTheFirstLetter(targetFieldName);
        if (mappingField.sourceFieldConfigs().length > 0) {
            SourceFieldConfig selectedConfig = null;
            SourceFieldConfig[] sourceFieldConfigs = mappingField.sourceFieldConfigs();
            for (SourceFieldConfig sourceFieldConfig : sourceFieldConfigs) {
                boolean meetDesiredSoureClass = false;
                // if the number of source classes greater than 0 => check every source classes in there to ensure that some of them equal desired source class
                if (sourceFieldConfig.forSourceClasses().length > 0) {
                    Class[] sourceClasses = sourceFieldConfig.forSourceClasses();
                    for (Class source : sourceClasses) {
                        if (source.equals(desiredSourceClass)) {
                            selectedConfig = sourceFieldConfig;
                            meetDesiredSoureClass = true;
                            break;
                        } else if (source.equals(AnySourceClass.class)) {
                            // if source class is void, it will be applied if there are no source classes that match with desired source class
                            selectedConfig = sourceFieldConfig;
                        }
                    }
                    if (meetDesiredSoureClass) break;
                } else {
                    selectedConfig = sourceFieldConfig;
                }
            }
            String getterMethod = selectedConfig != null ? selectedConfig.sourceGetterMethod() : null;
            if (getterMethod == null) {
                logger.warning(String.format("[%s -> %s] [target field: %s]: Can not find custom getter method", desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName));
                logger.info(String.format("[%s -> %s] [target field: %s]: Apply default source getter method '%s %s()'",
                        desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName, targetField.getType().getSimpleName(), defaultGetterMethod));
                return defaultGetterMethod;
            }
            if (getterMethod.isEmpty()) {
                throw new Exception(String.format("[%s -> %s] [target field: %s]: Please specify source getterMethod", desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName));
            }
            return getterMethod;
        } else return defaultGetterMethod;
    }

    public static String getTargetSetterMethodName(Field targetField, Class desiredSourceClass, Class targetClass) throws Exception {
        String targetFieldName = targetField.getName();
        MappingField mappingField = targetField.getDeclaredAnnotation(MappingField.class);
        String defaultSetterMethod = "set" + MiluMapperUtils.toUppercaseTheFirstLetter(targetFieldName);
        if (mappingField.targetFieldConfigs().length > 0) {
            TargetFieldConfig selectedConfig = null;
            TargetFieldConfig[] sourceFieldConfigs = mappingField.targetFieldConfigs();
            for (TargetFieldConfig targetFieldConfig : sourceFieldConfigs) {
                boolean meetDesiredSoureClass = false;
                // if the number of source classes greater than 0 => check every source classes in there to ensure that some of them equal desired source class
                if (targetFieldConfig.forSourceClasses().length > 0) {
                    Class[] sourceClasses = targetFieldConfig.forSourceClasses();
                    for (Class source : sourceClasses) {
                        if (source.equals(desiredSourceClass)) {
                            selectedConfig = targetFieldConfig;
                            meetDesiredSoureClass = true;
                            break;
                        } else if (source.equals(AnySourceClass.class)) {
                            // if source class is void, it will be applied if there are no source classes that match with desired source class
                            selectedConfig = targetFieldConfig;
                        }
                    }
                    if (meetDesiredSoureClass) break;
                } else {
                    selectedConfig = targetFieldConfig;
                }
            }
            String setterMethod = selectedConfig != null ? selectedConfig.targetSetterMethod() : null;
            if (setterMethod == null) {
                logger.warning(String.format("[%s -> %s] [target field: %s]: Can not find custom setter method", desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName));
                logger.info(String.format("[%s -> %s] [target field: %s]:  Apply default setter method 'void %s(%s)'",
                        desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName, defaultSetterMethod, targetField.getType().getSimpleName()));
                return defaultSetterMethod;
            }
            if (setterMethod.isEmpty()) {
                throw new Exception(String.format("[%s -> %s] [target field: %s]: Please specify setterMethod", desiredSourceClass.getSimpleName(), targetClass.getSimpleName(), targetFieldName));
            }
            return setterMethod;
        } else return "set" + MiluMapperUtils.toUppercaseTheFirstLetter(targetFieldName);
    }

    public static boolean isNestedField(Field targetField) {
        if (targetField.isAnnotationPresent(MappingField.class)) {
            return targetField.getAnnotation(MappingField.class).allowNestedMapping();
        }
        return false;
    }

    public static List<String> getTypeArguments(Field field) {
        Type type = field.getGenericType();
        List<String> typeArguments = new ArrayList<>();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type argumentType : parameterizedType.getActualTypeArguments()) {
                typeArguments.add(argumentType.getTypeName());
            }
        }
        return typeArguments;
    }

    public static <T> Collection<T> initCollectionObject(Class<? extends Collection> clazz) throws Exception {
        try {
            if (clazz.equals(List.class)) clazz = ArrayList.class;
            else if (clazz.equals(Set.class)) clazz = HashSet.class;
                // TODO: need comparable to handle queue
            else if (clazz.equals(Queue.class)) clazz = PriorityQueue.class;

            Constructor constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return (Collection<T>) constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
