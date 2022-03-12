package com.merlin.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappingField {
    //    String sourceFieldName() default "";
    boolean allowNestedMapping() default false;

    SourceFieldConfig[] sourceFieldConfigs() default {};

    TargetFieldConfig[] targetFieldConfigs() default {};

}
