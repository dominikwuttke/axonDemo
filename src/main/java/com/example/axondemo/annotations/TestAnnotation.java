package com.example.axondemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface TestAnnotation {
    String value();
}
