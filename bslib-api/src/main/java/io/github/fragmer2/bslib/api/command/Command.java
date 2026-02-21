package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Command {
    String value();
    String[] aliases() default {};
    String description() default "";
    String permission() default "";
}