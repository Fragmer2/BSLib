package io.github.fragmer2.bslib.api.di;

import java.lang.annotation.*;

/**
 * Marks a field for dependency injection.
 *
 * Usage:
 *   @Inject Economy economy;
 *   @Inject("mysql") Database db;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    String value() default "";
}
