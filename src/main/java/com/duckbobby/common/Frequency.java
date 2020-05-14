package com.duckbobby.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by witsir on 2020/04/04.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Frequency {

    String name() default "all";

    int time() default 0;

    int limit() default 0;
}