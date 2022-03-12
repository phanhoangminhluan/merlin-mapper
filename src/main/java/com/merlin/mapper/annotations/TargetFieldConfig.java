package com.merlin.mapper.annotations;


import com.merlin.mapper.handler.AnySourceClass;

public @interface TargetFieldConfig {
    Class[] forSourceClasses() default {AnySourceClass.class};

    String targetSetterMethod() default "";
}
