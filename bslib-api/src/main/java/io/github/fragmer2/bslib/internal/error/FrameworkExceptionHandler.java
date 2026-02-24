package io.github.fragmer2.bslib.internal.error;

import io.github.fragmer2.bslib.api.annotation.ApiStatus;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.Map;
import java.util.logging.Level;

/**
 * Centralized framework exception sink to keep plugin failures isolated and observable.
 */
@ApiStatus.Internal
public final class FrameworkExceptionHandler {
    private FrameworkExceptionHandler() {}

    public enum Source {
        COMMAND,
        LISTENER,
        TASK,
        MENU,
        MODULE,
        SERVICE,
        UNKNOWN
    }

    public static void handle(Plugin plugin, Source source, Throwable error, Map<String, Object> context) {
        if (plugin == null || error == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[BSLib] Framework exception captured").append(System.lineSeparator())
          .append("  timestamp: ").append(Instant.now()).append(System.lineSeparator())
          .append("  plugin: ").append(plugin.getName()).append(System.lineSeparator())
          .append("  source: ").append(source).append(System.lineSeparator());

        if (context != null && !context.isEmpty()) {
            sb.append("  context:").append(System.lineSeparator());
            context.forEach((k, v) -> sb.append("    - ").append(k).append(" = ").append(v).append(System.lineSeparator()));
        }

        plugin.getLogger().log(Level.SEVERE, sb.toString(), error);
    }
}
