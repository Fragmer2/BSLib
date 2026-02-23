package io.github.fragmer2.bslib.api.lifecycle;

import java.lang.annotation.*;

/**
 * Marks a class for automatic registration as a Bukkit event listener.
 *
 * When used with @AutoScan, BSLib will automatically find and register
 * all classes annotated with @EventListener, even if they don't
 * explicitly implement the Listener interface.
 *
 * Usage:
 *   @EventListener
 *   public class PlayerListener {
 *       @EventHandler
 *       public void onJoin(PlayerJoinEvent event) { ... }
 *   }
 *
 * Note: The class must still have @EventHandler methods to receive events.
 * BSLib will automatically implement Listener via a dynamic proxy if needed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventListener {}
