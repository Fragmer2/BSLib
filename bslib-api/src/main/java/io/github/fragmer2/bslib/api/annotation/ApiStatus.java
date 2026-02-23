package io.github.fragmer2.bslib.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotations describing public API stability contracts.
 */
public final class ApiStatus {
    private ApiStatus() {}

    /**
     * Internal API — not covered by compatibility guarantees and may change without notice.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
    public @interface Internal {}

    /**
     * Experimental API — public preview, subject to incompatible changes.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
    public @interface Experimental {
        String since() default "";
        String notes() default "";
    }
}
