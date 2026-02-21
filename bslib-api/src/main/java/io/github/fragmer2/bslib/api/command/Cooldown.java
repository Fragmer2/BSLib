package io.github.fragmer2.bslib.api.command;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cooldown {
    int value();
    TimeUnit unit() default TimeUnit.SECONDS;
    String bypassPermission() default "";
}