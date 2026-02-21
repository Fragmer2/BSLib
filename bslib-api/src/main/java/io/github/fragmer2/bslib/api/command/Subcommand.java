package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subcommand {
    String value();
    String[] aliases() default {};
    String description() default "";
    String permission() default "";
}