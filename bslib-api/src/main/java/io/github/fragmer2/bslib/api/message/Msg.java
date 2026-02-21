package io.github.fragmer2.bslib.api.message;

import io.github.fragmer2.bslib.api.config.Config;
import io.github.fragmer2.bslib.api.placeholder.Placeholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Message / localization engine.
 *
 * Setup:
 *   Msg.init(plugin, "messages.yml");
 *   // messages.yml:
 *   // shop.buy.success: "<green>You bought {item} for {price}!"
 *   // shop.buy.fail: "<red>Not enough money!"
 *
 * Send:
 *   Msg.key("shop.buy.success")
 *      .replace("item", "Diamond Sword")
 *      .replace("price", "$500")
 *      .send(player);
 *
 * With prefix:
 *   Msg.key("shop.buy.success").prefixed().send(player);
 *
 * Title:
 *   Msg.key("welcome.title").sendTitle(player);
 *
 * Action bar:
 *   Msg.key("balance.display").sendActionBar(player);
 *
 * Raw (no config):
 *   Msg.of("<red>Error!").send(player);
 *
 * Multi-language:
 *   Msg.init(plugin, "messages_en.yml");
 *   Msg.addLanguage("ru", Config.of(plugin, "messages_ru.yml"));
 */
public final class Msg {
    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static Config defaultMessages;
    private static final Map<String, Config> languages = new HashMap<>();
    private static String prefix = "";
    private static String defaultLang = "en";

    private Msg() {}

    // ========== Init ==========

    public static void init(Plugin plugin, String messagesFile) {
        defaultMessages = Config.of(plugin, messagesFile);
        prefix = defaultMessages.node("prefix").asString("");
    }

    public static void addLanguage(String lang, Config config) {
        languages.put(lang.toLowerCase(), config);
    }

    public static void setPrefix(String p) {
        prefix = p;
    }

    /** Set default language code (default: "en"). */
    public static void setDefaultLanguage(String lang) {
        defaultLang = lang.toLowerCase();
    }

    /** Clear all state (called on disable). */
    public static void clear() {
        defaultMessages = null;
        languages.clear();
        prefix = "";
        defaultLang = "en";
    }

    // ========== Create message ==========

    /**
     * Message from config key.
     */
    public static MessageBuilder key(String configKey) {
        return new MessageBuilder(configKey, true);
    }

    /**
     * Raw message (not from config).
     */
    public static MessageBuilder of(String text) {
        return new MessageBuilder(text, false);
    }

    // ========== Language resolution ==========

    /**
     * Get the best Config for a player's locale.
     * Falls back: player locale → default lang → defaultMessages.
     */
    private static Config resolveConfig(Player player) {
        if (player != null && !languages.isEmpty()) {
            // Try player's locale (e.g., "ru_ru" → try "ru_ru", then "ru")
            String locale = player.locale().getLanguage().toLowerCase();
            Config langConfig = languages.get(locale);
            if (langConfig != null) return langConfig;

            // Try full locale (e.g. "ru_ru")
            String fullLocale = locale + "_" + player.locale().getCountry().toLowerCase();
            langConfig = languages.get(fullLocale);
            if (langConfig != null) return langConfig;
        }
        return defaultMessages;
    }

    // ========== Builder ==========

    public static class MessageBuilder {
        private String text;
        private final boolean fromConfig;
        private final Map<String, String> replacements = new HashMap<>();
        private boolean usePrefix = false;

        MessageBuilder(String text, boolean fromConfig) {
            this.text = text;
            this.fromConfig = fromConfig;
        }

        public MessageBuilder replace(String key, String value) {
            replacements.put(key, value);
            return this;
        }

        public MessageBuilder replace(String key, Object value) {
            replacements.put(key, String.valueOf(value));
            return this;
        }

        public MessageBuilder prefixed() {
            this.usePrefix = true;
            return this;
        }

        // ========== Resolve text ==========

        private String resolve(Player player) {
            String resolved = text;

            // Resolve from config (with language support)
            if (fromConfig) {
                Config msgConfig = resolveConfig(player);
                if (msgConfig != null) {
                    resolved = msgConfig.node(text).asString(null);
                }
                // Fallback to default messages if language-specific config didn't have the key
                if (resolved == null && defaultMessages != null) {
                    resolved = defaultMessages.node(text).asString(text);
                }
                if (resolved == null) resolved = text; // ultimate fallback: raw key
            }

            // Apply custom replacements
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                resolved = resolved.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            // Apply placeholders
            if (player != null) {
                resolved = Placeholders.apply(player, resolved);
            }

            // Apply prefix
            if (usePrefix && !prefix.isEmpty()) {
                resolved = prefix + resolved;
            }

            return resolved;
        }

        private String resolve() {
            return resolve(null);
        }

        // ========== Send ==========

        public void send(CommandSender sender) {
            String resolved = (sender instanceof Player p) ? resolve(p) : resolve();
            sender.sendMessage(MM.deserialize(resolved));
        }

        public void send(Player player) {
            player.sendMessage(MM.deserialize(resolve(player)));
        }

        public void sendTitle(Player player) {
            sendTitle(player, Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(1000));
        }

        public void sendTitle(Player player, Duration fadeIn, Duration stay, Duration fadeOut) {
            Component title = MM.deserialize(resolve(player));
            player.showTitle(Title.title(title, Component.empty(),
                    Title.Times.times(fadeIn, stay, fadeOut)));
        }

        public void sendSubtitle(Player player, String titleKey) {
            Component titleComp = MM.deserialize(Msg.key(titleKey).resolve(player));
            Component subComp = MM.deserialize(resolve(player));
            player.showTitle(Title.title(titleComp, subComp));
        }

        public void sendActionBar(Player player) {
            player.sendActionBar(MM.deserialize(resolve(player)));
        }

        public void broadcast() {
            Component comp = MM.deserialize(resolve());
            org.bukkit.Bukkit.broadcast(comp);
        }

        /**
         * Get resolved text (for embedding in other systems).
         */
        public String text(Player player) {
            return resolve(player);
        }

        public String text() {
            return resolve();
        }

        /**
         * Get as Adventure Component.
         */
        public Component component(Player player) {
            return MM.deserialize(resolve(player));
        }

        public Component component() {
            return MM.deserialize(resolve());
        }
    }
}
