package io.github.fragmer2.bslib.api.lifecycle;

import java.lang.annotation.*;

/**
 * Enables automatic classpath scanning on a FrameworkPlugin.
 *
 * When present, BSLib will automatically:
 * ✅ Find and register all @Command classes
 * ✅ Find and register all Listener implementations
 * ✅ Find and inject all @Service classes
 * ✅ Load all .yml config files from plugin folder
 *
 * Usage:
 *   @AutoScan
 *   public final class ShopPlugin extends FrameworkPlugin {}
 *   // That's it. Zero boilerplate.
 *
 * Customization:
 *   @AutoScan(packages = "com.myserver.shop")  // only scan this package
 *   @AutoScan(excludePackages = "com.myserver.shop.internal")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoScan {
    /** Base packages to scan. Empty = scan all packages in the plugin JAR. */
    String[] packages() default {};

    /** Packages to exclude from scanning. */
    String[] excludePackages() default {};

    /** Auto-load all .yml configs from plugin folder. Default: true. */
    boolean configs() default true;
}
