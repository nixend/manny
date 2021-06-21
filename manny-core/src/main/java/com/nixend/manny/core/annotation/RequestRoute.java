package com.nixend.manny.core.annotation;

import com.nixend.manny.common.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * @author panyox
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestRoute {

    String value() default "";

    String version() default "v1";

    boolean enable() default true;

    HttpMethod method() default HttpMethod.GET;
}
