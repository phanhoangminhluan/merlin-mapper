package com.merlin.mapper;


import com.merlin.mapper.handler.MapperHandler;
import com.merlin.mapper.handler.MiluMapperUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MerlinMapper {

    public <S, T> T mapSingleObject(S sourceObject, Class<T> targetClass) throws Exception {
        MapperHandler<S, T> mapperHandler = new MapperHandler(sourceObject, targetClass);

        ArrayList<Field> targetAnnotatedFields = mapperHandler.getTargetFields();

        for (Field targetAnnotatedField : targetAnnotatedFields) {
            // get source and target field name based on target field
            String sourceGetterMethod = MiluMapperUtils.getSourceGetterMethodName(targetAnnotatedField, sourceObject.getClass(), targetClass);
            String targetSetterMethod = MiluMapperUtils.getTargetSetterMethodName(targetAnnotatedField, sourceObject.getClass(), targetClass);
            boolean isCustomSetterMethod = true;

            if (targetSetterMethod != null && targetSetterMethod.equals(String.format("set%s", MiluMapperUtils.toUppercaseTheFirstLetter(targetAnnotatedField.getName())))) {
                isCustomSetterMethod = false;
            }

            if (
                    mapperHandler.setActiveTargetField(targetAnnotatedField.getName()) &&
                            mapperHandler.setActiveSourceGetterMethod(sourceGetterMethod) &&
                            mapperHandler.setActiveTargetSetterMethod(targetSetterMethod, mapperHandler.getSourceGetterMethod().getReturnType())
            ) {
                // get source value
                Object sourceFieldValue = mapperHandler.invokeSourceGetterMethod();

                // set value for list object
                //if setter is customize, MiluMapper will treat that list as a single object
                if (mapperHandler.isActiveTargetFieldAList() && !isCustomSetterMethod) {
                    Collection sourceListValue = (Collection) sourceFieldValue;
                    Class targetItemClass = mapperHandler.getArgumentTypeOfTargetField();
                    Class<? extends Collection> targetCollection = (Class<? extends Collection>) targetAnnotatedField.getType();

                    sourceFieldValue = this.mapList(sourceListValue, targetItemClass, targetCollection);
                    mapperHandler.invokeTargetSetterMethod(sourceFieldValue);
                }

                // set value for single object
                else {
                    if (MiluMapperUtils.isNestedField(targetAnnotatedField)) {
                        Class setterParameterType = mapperHandler.getActiveTargetField().getType();
                        MerlinMapper nestedMerlinMapper = new MerlinMapper();
                        Object targetNestedObject = nestedMerlinMapper.mapSingleObject(sourceFieldValue, setterParameterType);

                        mapperHandler.invokeTargetSetterMethod(targetNestedObject);
                    } else {
                        mapperHandler.invokeTargetSetterMethod(sourceFieldValue);
                    }
                }
            }
        }
        return mapperHandler.getTargetObject();
    }

    public <S, T> Collection<T> mapList(Collection<S> sourceList, Class<T> targetItemClass, Class<? extends Collection> targetCollectionClassName) throws Exception {

        Collection<T> targetList = MiluMapperUtils.initCollectionObject(targetCollectionClassName);
        for (S sourceObject : sourceList) {
            T targetObject = this.mapSingleObject(sourceObject, targetItemClass);
            targetList.add(targetObject);
        }
        return targetList;
    }
}
