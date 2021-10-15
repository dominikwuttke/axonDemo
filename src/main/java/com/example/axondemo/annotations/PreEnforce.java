package com.example.axondemo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface PreEnforce {
    String subject() default "";

    String action() default "";

    String resource() default "";

    String environment() default "";
}
