package io.github.fragmer2.bslib.api.lifecycle;

import java.lang.annotation.*;

/** Called when the owning plugin enables. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEnable {}
