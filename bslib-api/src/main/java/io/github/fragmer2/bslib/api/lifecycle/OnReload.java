package io.github.fragmer2.bslib.api.lifecycle;

import java.lang.annotation.*;

/** Called when configs are reloaded via FrameworkPlugin.reload(). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnReload {}
