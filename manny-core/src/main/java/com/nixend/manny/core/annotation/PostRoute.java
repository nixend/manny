package com.nixend.manny.core.annotation;

import com.nixend.manny.common.enums.HttpMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author panyox
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RequestRoute(method = HttpMethod.POST)
public @interface PostRoute {

    @AliasFor(annotation = RequestRoute.class)
    String value() default "";

    String name() default "";

    boolean enable() default true;
}
