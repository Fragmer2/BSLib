package io.github.fragmer2.bslib.api.di;

import java.lang.annotation.*;

/**
 * Marks a class as a service that should be auto-registered in the container.
 *
 * Usage:
 *   @Service
 *   public class EconomyImpl implements Economy { ... }
 *
 *   @Service("mysql")
 *   public class MySQLDatabase implements Database { ... }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
    String value() default "";
}
