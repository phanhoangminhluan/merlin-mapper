package com.merlin.mapper.handler;


import com.merlin.mapper.utils.MerlinMapperUtils;
import com.merlin.mapper.annotations.MappingField;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Logger;

public class MapperHandler<S, T> {

    Logger logger = Logger.getLogger(MerlinMapperUtils.class.getName());

    private S sourceObject;
    private Class sourceClass;
    private T targetObject;
    private Class<T> targetClass;
    private ArrayList<Field> targetFields;
    private Field activeTargetField;
    private Method sourceGetterMethod;
    private Method targetSetterMethod;


    public MapperHandler(S sourceObject, Class<T> targetClass) throws Exception {
        this.sourceObject = sourceObject;
        this.targetClass = targetClass;
        this.sourceClass = sourceObject.getClass();
        this.targetObject = this.initTargetObject();
        this.targetFields = MiluMapperUtils.getAllAnnotatedFields(this.targetObject, MappingField.class);
    }

    public Method getSourceGetterMethod() {
        return sourceGetterMethod;
    }

    public boolean setActiveSourceGetterMethod(String methodName) {
        if (methodName == null) return false;
        boolean hasSet = false;
        try {
            Method method = this.sourceClass.getMethod(methodName);
            this.sourceGetterMethod = method;
            hasSet = true;
        } catch (NoSuchMethodException e) {
            String msg = String.format("[%s -> %s] [target field: %s]: There is no method named %s in class %s", sourceClass.getSimpleName(), targetClass.getSimpleName(), activeTargetField.getName(), methodName, this.sourceClass.getSimpleName());
            logger.warning(msg);
        }
        return hasSet;
    }

    public boolean setActiveTargetSetterMethod(String methodName, Class paramType) {
        if (methodName == null || paramType == null) return false;
        boolean hasSet = false;
        try {
            Method method = this.targetClass.getMethod(methodName, paramType);
            this.targetSetterMethod = method;
            hasSet = true;
        } catch (NoSuchMethodException e) {
            String msg = String.format("[%s -> %s] [target field: %s]: There is no method 'void %s(%s)' in class %s", sourceClass.getSimpleName(), targetClass.getSimpleName(), activeTargetField.getName(), methodName, paramType.getName(), this.targetClass.getSimpleName());
            logger.warning(msg);
        }
        return hasSet;
    }

    public ArrayList<Field> getTargetFields() {
        return targetFields;
    }

    public T getTargetObject() {
        return targetObject;
    }

    public boolean setActiveTargetField(String targetFieldName) {
        boolean hasSet = false;
        try {
            this.activeTargetField = this.targetClass.getDeclaredField(targetFieldName);
            hasSet = true;
        } catch (NoSuchFieldException e) {
            try {
                this.activeTargetField = this.targetClass.getSuperclass().getDeclaredField(targetFieldName);
                hasSet = true;
            } catch (NoSuchFieldException ex) {
                String msg = String.format("[%s -> %s] [target field: %s]: There is no field named '%s' in target class '%s'.",
                        sourceClass.getSimpleName(),
                        targetClass.getSimpleName(),
                        activeTargetField.getName(),
                        targetFieldName,
                        this.targetClass.getSimpleName());
                logger.warning(msg);
            }
        }
        return hasSet;
    }

    public Field getActiveTargetField() {
        return activeTargetField;
    }

    public boolean isActiveTargetFieldAList() {
        Type activeTargetFieldClass = this.activeTargetField.getType();
        return activeTargetFieldClass.equals(List.class)
                || activeTargetFieldClass.equals(ArrayList.class)
                || activeTargetFieldClass.equals(LinkedList.class)
                || activeTargetFieldClass.equals(Vector.class)
                || activeTargetFieldClass.equals(Collection.class)
                || activeTargetFieldClass.equals(Queue.class)
                || activeTargetFieldClass.equals(Set.class)
                || activeTargetFieldClass.equals(TreeSet.class)
                || activeTargetFieldClass.equals(LinkedHashSet.class)
                || activeTargetFieldClass.equals(Deque.class)
                || activeTargetFieldClass.equals(ArrayDeque.class)
                || activeTargetFieldClass.equals(PriorityQueue.class);
    }

    public Class getArgumentTypeOfTargetField() {
        try {
            return Class.forName(MiluMapperUtils.getTypeArguments(this.activeTargetField).get(0));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private T initTargetObject() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor = this.targetClass.getConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    public Object invokeSourceGetterMethod() throws InvocationTargetException, IllegalAccessException {
        return sourceGetterMethod.invoke(this.sourceObject);
    }

    public void invokeTargetSetterMethod(Object fieldValue) throws InvocationTargetException, IllegalAccessException {
        this.targetSetterMethod.invoke(targetObject, fieldValue);
    }

}
