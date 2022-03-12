package com.merlin.mapper.annotations;


import com.merlin.mapper.handler.AnySourceClass;

public @interface SourceFieldConfig {
    Class[] forSourceClasses() default {AnySourceClass.class};

    String sourceGetterMethod() default "";
}
