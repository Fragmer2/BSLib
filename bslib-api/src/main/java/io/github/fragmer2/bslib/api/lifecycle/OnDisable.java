package io.github.fragmer2.bslib.api.lifecycle;

import java.lang.annotation.*;

/** Called when the owning plugin disables. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnDisable {}
