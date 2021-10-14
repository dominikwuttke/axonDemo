package com.example.axondemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
public @interface PreEnforce {
    String subject() default "";

    String action() default "";

    String resource() default "";

    String environment() default "";
}
