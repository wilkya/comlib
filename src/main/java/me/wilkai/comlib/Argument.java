package me.wilkai.comlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a Command Argument which should have its value assigned by the Command Dispatcher.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {

    /**
     * The name of this Argument.
     */
    String name();

    /**
     * A short description of this Argument.
     */
    String summary() default "No Description Provided.";

    /**
     * Is this Argument required?
     */
    boolean required() default false;
}