package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {
    String value() default "";
    String defaultValue() default "";
}