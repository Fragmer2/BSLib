package io.github.fragmer2.bslib.paper.command;

import io.github.fragmer2.bslib.api.command.*;
import io.github.fragmer2.bslib.api.command.Optional;
import io.github.fragmer2.bslib.paper.command.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaperCommandRegistry implements CommandRegistry {
    private final Plugin plugin;
    private final CooldownManager cooldownManager = new CooldownManager();
    private final Map<String, CmdNode> rootCommands = new LinkedHashMap<>();

    // Extensible converters
    private final Map<Class<?>, Function<String, ?>> converters = new HashMap<>();
    private final Map<Class<?>, Function<String, List<String>>> tabProviders = new HashMap<>();

    public PaperCommandRegistry(Plugin plugin) {
        this.plugin = plugin;
        registerDefaultConverters();
        registerDefaultTabProviders();
    }

    // ========== Public API ==========

    @Override
    public void register(Object commandsInstance) {
        Class<?> clazz = commandsInstance.getClass();
        processClass(clazz, commandsInstance, null);
    }

    @Override
    public <T> void registerConverter(Class<T> type, Function<String, T> converter) {
        converters.put(type, converter);
    }

    @Override
    public <T> void registerConverter(Class<T> type, Function<String, T> converter, Function<String, List<String>> tabCompleter) {
        converters.put(type, converter);
        tabProviders.put(type, tabCompleter);
    }

    // ========== Default converters ==========

    private void registerDefaultConverters() {
        converters.put(String.class, s -> s);
        converters.put(int.class, Integer::parseInt);
        converters.put(Integer.class, Integer::parseInt);
        converters.put(double.class, Double::parseDouble);
        converters.put(Double.class, Double::parseDouble);
        converters.put(float.class, Float::parseFloat);
        converters.put(Float.class, Float::parseFloat);
        converters.put(long.class, Long::parseLong);
        converters.put(Long.class, Long::parseLong);
        converters.put(boolean.class, Boolean::parseBoolean);
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(Player.class, s -> {
            Player p = Bukkit.getPlayer(s);
            if (p == null) throw new IllegalArgumentException("Player not found: " + s);
            return p;
        });
        converters.put(Material.class, s -> {
            Material m = Material.getMaterial(s.toUpperCase());
            if (m == null) throw new IllegalArgumentException("Invalid material: " + s);
            return m;
        });
        converters.put(World.class, s -> {
            World w = Bukkit.getWorld(s);
            if (w == null) throw new IllegalArgumentException("World not found: " + s);
            return w;
        });
        converters.put(UUID.class, UUID::fromString);
    }

    private void registerDefaultTabProviders() {
        tabProviders.put(Player.class, input ->
                Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> n.toLowerCase().startsWith(input.toLowerCase()))
                        .collect(Collectors.toList()));

        tabProviders.put(Material.class, input ->
                Arrays.stream(Material.values())
                        .filter(m -> !m.isLegacy())
                        .map(m -> m.name().toLowerCase())
                        .filter(n -> n.startsWith(input.toLowerCase()))
                        .limit(30) // don't flood
                        .collect(Collectors.toList()));

        tabProviders.put(World.class, input ->
                Bukkit.getWorlds().stream()
                        .map(World::getName)
                        .filter(n -> n.toLowerCase().startsWith(input.toLowerCase()))
                        .collect(Collectors.toList()));

        tabProviders.put(boolean.class, input ->
                List.of("true", "false").stream()
                        .filter(s -> s.startsWith(input.toLowerCase()))
                        .collect(Collectors.toList()));
        tabProviders.put(Boolean.class, tabProviders.get(boolean.class));
    }

    // ========== Class processing ==========

    private void processClass(Class<?> clazz, Object instance, CmdNode parent) {
        io.github.fragmer2.bslib.api.command.Command classCmd =
                clazz.getAnnotation(io.github.fragmer2.bslib.api.command.Command.class);
        Permission classPerm = clazz.getAnnotation(Permission.class);

        // If class has @Command, it defines a command group
        CmdNode groupNode = parent;
        if (classCmd != null && parent == null) {
            groupNode = getOrCreateRoot(classCmd.value());
            groupNode.description = classCmd.description();
            if (classPerm != null) groupNode.permission = classPerm.value();
            for (String alias : classCmd.aliases()) {
                groupNode.aliases.add(alias);
            }
        }

        for (Method method : clazz.getDeclaredMethods()) {
            io.github.fragmer2.bslib.api.command.Command cmdAnn =
                    method.getAnnotation(io.github.fragmer2.bslib.api.command.Command.class);
            Subcommand subAnn = method.getAnnotation(Subcommand.class);

            if (cmdAnn != null) {
                CmdNode node;
                if (groupNode != null && groupNode != parent) {
                    // Method is inside a class-level command group → treat as executor of the group
                    // or as subcommand if it has a different name
                    if (cmdAnn.value().equals(groupNode.name)) {
                        node = groupNode;
                    } else {
                        node = groupNode.getOrCreateSub(cmdAnn.value());
                    }
                } else {
                    node = getOrCreateRoot(cmdAnn.value());
                }
                populateNode(node, method, instance, cmdAnn.description(),
                        cmdAnn.permission(), cmdAnn.aliases());
            } else if (subAnn != null && groupNode != null) {
                CmdNode node = groupNode.getOrCreateSub(subAnn.value());
                populateNode(node, method, instance, subAnn.description(),
                        subAnn.permission(), subAnn.aliases());
            }
        }

        // Inner classes as subcommand groups
        for (Class<?> inner : clazz.getDeclaredClasses()) {
            if (inner.isAnnotationPresent(io.github.fragmer2.bslib.api.command.Command.class)) {
                Object innerInstance = createInnerInstance(inner, clazz, instance);
                if (innerInstance == null) continue;

                io.github.fragmer2.bslib.api.command.Command innerCmd =
                        inner.getAnnotation(io.github.fragmer2.bslib.api.command.Command.class);
                CmdNode parentForInner = groupNode != null ? groupNode : getOrCreateRoot(innerCmd.value());
                CmdNode subNode = groupNode != null
                        ? groupNode.getOrCreateSub(innerCmd.value())
                        : parentForInner;

                Permission innerPerm = inner.getAnnotation(Permission.class);
                if (innerPerm != null) subNode.permission = innerPerm.value();
                subNode.description = innerCmd.description();

                processClass(inner, innerInstance, subNode);
            }
        }

        // Register all root commands with Bukkit
        if (parent == null) {
            for (CmdNode node : rootCommands.values()) {
                if (!node.registered) {
                    registerWithBukkit(node);
                    node.registered = true;
                }
            }
        }
    }

    private void populateNode(CmdNode node, Method method, Object instance,
                              String description, String permission, String[] aliases) {
        node.method = method;
        node.instance = instance;
        node.description = description;
        node.parameters = analyzeParameters(method);

        // Annotations on method override class-level
        if (method.isAnnotationPresent(Permission.class)) {
            node.permission = method.getAnnotation(Permission.class).value();
        } else if (!permission.isEmpty()) {
            node.permission = permission;
        }
        node.playerOnly = method.isAnnotationPresent(PlayerOnly.class);
        node.consoleOnly = method.isAnnotationPresent(ConsoleOnly.class);

        if (method.isAnnotationPresent(Cooldown.class)) {
            Cooldown cd = method.getAnnotation(Cooldown.class);
            node.cooldownSeconds = (int) cd.unit().toSeconds(cd.value());
            node.cooldownBypass = cd.bypassPermission();
        }

        for (String alias : aliases) {
            node.aliases.add(alias);
        }
    }

    // ========== Parameter analysis ==========

    private List<ParamInfo> analyzeParameters(Method method) {
        List<ParamInfo> params = new ArrayList<>();
        Parameter[] javaParams = method.getParameters();
        Annotation[][] allAnns = method.getParameterAnnotations();

        for (int i = 0; i < javaParams.length; i++) {
            ParamInfo info = new ParamInfo();
            info.type = javaParams[i].getType();
            info.name = "arg" + i;
            info.isSender = (info.type == CommandSender.class || info.type == Player.class);

            for (Annotation ann : allAnns[i]) {
                if (ann instanceof Arg a) {
                    if (!a.value().isEmpty()) info.name = a.value();
                    if (!a.defaultValue().isEmpty()) {
                        info.defaultValue = a.defaultValue();
                        info.optional = true;
                    }
                } else if (ann instanceof Optional) {
                    info.optional = true;
                } else if (ann instanceof JoinArgs) {
                    info.joinArgs = true;
                } else if (ann instanceof TabComplete tc) {
                    info.tabType = tc.value();
                    info.staticSuggestions = tc.suggest();
                }
            }
            params.add(info);
        }
        return params;
    }

    // ========== Bukkit registration ==========

    private void registerWithBukkit(CmdNode node) {
        PluginCommand existing = plugin.getServer().getPluginCommand(node.name);
        if (existing != null && existing.getPlugin() == plugin) {
            setupCommand(existing, node);
            return;
        }

        try {
            Constructor<PluginCommand> ctor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            ctor.setAccessible(true);
            PluginCommand cmd = ctor.newInstance(node.name, plugin);
            setupCommand(cmd, node);

            Field mapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            mapField.setAccessible(true);
            CommandMap map = (CommandMap) mapField.get(Bukkit.getServer());
            map.register(plugin.getName(), cmd);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to register command: " + node.name);
            e.printStackTrace();
        }
    }

    private void setupCommand(PluginCommand cmd, CmdNode node) {
        cmd.setExecutor((sender, command, label, args) -> execute(sender, args, node));
        cmd.setTabCompleter((sender, command, alias, args) -> tabComplete(sender, args, node));
        if (!node.description.isEmpty()) cmd.setDescription(node.description);
        if (!node.aliases.isEmpty()) cmd.setAliases(new ArrayList<>(node.aliases));
        if (node.permission != null) cmd.setPermission(node.permission);
    }

    // ========== Execution ==========

    private boolean execute(CommandSender sender, String[] args, CmdNode node) {
        // Resolve subcommand
        if (args.length > 0) {
            CmdNode sub = node.findSub(args[0]);
            if (sub != null) {
                return execute(sender, Arrays.copyOfRange(args, 1, args.length), sub);
            }
        }

        // No method → show help
        if (node.method == null) {
            sendHelp(sender, node);
            return true;
        }

        // Permission check
        if (node.permission != null && !sender.hasPermission(node.permission)) {
            sender.sendMessage("§cYou don't have permission for this command.");
            return true;
        }

        // Sender type check
        if (node.playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cThis command is for players only.");
            return true;
        }
        if (node.consoleOnly && sender instanceof Player) {
            sender.sendMessage("§cThis command is for console only.");
            return true;
        }

        // Cooldown check
        if (sender instanceof Player player && node.cooldownSeconds > 0) {
            if (node.cooldownBypass.isEmpty() || !player.hasPermission(node.cooldownBypass)) {
                if (cooldownManager.hasCooldown(player, node.getFullKey())) {
                    long rem = cooldownManager.getRemaining(player, node.getFullKey(), TimeUnit.SECONDS);
                    player.sendMessage("§cPlease wait " + rem + "s before using this again.");
                    return true;
                }
            }
        }

        // Parse arguments
        Object[] methodArgs;
        try {
            methodArgs = parseArgs(sender, args, node.parameters);
        } catch (ArgParseException e) {
            sender.sendMessage("§c" + e.getMessage());
            return true;
        }

        // Invoke
        try {
            node.method.setAccessible(true);
            node.method.invoke(node.instance, methodArgs);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IllegalArgumentException) {
                sender.sendMessage("§c" + cause.getMessage());
            } else {
                plugin.getLogger().severe("Error executing /" + node.getFullKey());
                cause.printStackTrace();
                sender.sendMessage("§cAn error occurred while executing this command.");
            }
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Error executing /" + node.getFullKey());
            e.printStackTrace();
            sender.sendMessage("§cAn error occurred while executing this command.");
            return true;
        }

        // Apply cooldown after success
        if (sender instanceof Player player && node.cooldownSeconds > 0) {
            if (node.cooldownBypass.isEmpty() || !player.hasPermission(node.cooldownBypass)) {
                cooldownManager.setCooldown(player, node.getFullKey(), node.cooldownSeconds, TimeUnit.SECONDS);
            }
        }

        return true;
    }

    private Object[] parseArgs(CommandSender sender, String[] args, List<ParamInfo> params) {
        Object[] result = new Object[params.size()];
        int argIdx = 0;

        for (int i = 0; i < params.size(); i++) {
            ParamInfo p = params.get(i);

            // Sender injection
            if (p.isSender) {
                result[i] = sender;
                continue;
            }

            // JoinArgs — consume all remaining
            if (p.joinArgs) {
                if (argIdx < args.length) {
                    result[i] = String.join(" ", Arrays.copyOfRange(args, argIdx, args.length));
                    argIdx = args.length;
                } else if (p.optional) {
                    result[i] = p.defaultValue != null ? p.defaultValue : "";
                } else {
                    throw new ArgParseException("Missing argument: " + p.name);
                }
                continue;
            }

            // Normal arg
            if (argIdx >= args.length) {
                if (p.optional) {
                    result[i] = p.defaultValue != null ? convertArg(p.defaultValue, p.type) : getDefaultForType(p.type);
                    continue;
                }
                throw new ArgParseException("Missing argument: " + p.name + ". Usage: " + buildUsage(params));
            }

            try {
                result[i] = convertArg(args[argIdx++], p.type);
            } catch (Exception e) {
                throw new ArgParseException("Invalid " + p.name + ": " + e.getMessage());
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Object convertArg(String input, Class<?> type) {
        Function<String, ?> converter = converters.get(type);
        if (converter != null) return converter.apply(input);

        // Try enum
        if (type.isEnum()) {
            try {
                return Enum.valueOf((Class<Enum>) type, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid value: " + input + ". Expected: " +
                        Arrays.stream(type.getEnumConstants()).map(Object::toString).collect(Collectors.joining(", ")));
            }
        }

        throw new IllegalArgumentException("Unsupported type: " + type.getSimpleName());
    }

    private Object getDefaultForType(Class<?> type) {
        if (type == int.class) return 0;
        if (type == double.class) return 0.0;
        if (type == float.class) return 0.0f;
        if (type == long.class) return 0L;
        if (type == boolean.class) return false;
        return null;
    }

    private String buildUsage(List<ParamInfo> params) {
        StringBuilder sb = new StringBuilder();
        for (ParamInfo p : params) {
            if (p.isSender) continue;
            sb.append(p.optional ? "[" : "<").append(p.name).append(p.optional ? "] " : "> ");
        }
        return sb.toString().trim();
    }

    // ========== Tab Completion ==========

    private List<String> tabComplete(CommandSender sender, String[] args, CmdNode node) {
        // Subcommand completion
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();

            // Add subcommand names and aliases
            for (CmdNode sub : node.subcommands.values()) {
                if (sub.permission == null || sender.hasPermission(sub.permission)) {
                    suggestions.add(sub.name);
                    suggestions.addAll(sub.aliases);
                }
            }

            // Also add argument completions for the current node's method
            if (node.method != null) {
                suggestions.addAll(getArgCompletions(sender, args, 0, node.parameters));
            }

            return filterStartsWith(suggestions, args[0]);
        }

        // Check if first arg is a subcommand
        if (args.length > 1) {
            CmdNode sub = node.findSub(args[0]);
            if (sub != null) {
                return tabComplete(sender, Arrays.copyOfRange(args, 1, args.length), sub);
            }
        }

        // Arg completion for current node
        if (node.method != null) {
            int argIndex = args.length - 1;
            List<String> completions = getArgCompletions(sender, args, argIndex, node.parameters);
            return filterStartsWith(completions, args[args.length - 1]);
        }

        return Collections.emptyList();
    }

    private List<String> getArgCompletions(CommandSender sender, String[] args, int argIndex, List<ParamInfo> params) {
        // Find which parameter corresponds to this arg index
        int paramIdx = 0;
        int consumedArgs = 0;
        for (ParamInfo p : params) {
            if (p.isSender) {
                paramIdx++;
                continue;
            }
            if (consumedArgs == argIndex) break;
            consumedArgs++;
            paramIdx++;
        }

        if (paramIdx >= params.size()) return Collections.emptyList();
        ParamInfo param = params.get(paramIdx);
        String input = args.length > argIndex ? args[argIndex] : "";

        // Static suggestions from @TabComplete(suggest = {...})
        if (param.staticSuggestions != null && param.staticSuggestions.length > 0) {
            return Arrays.asList(param.staticSuggestions);
        }

        // @TabComplete(TabType.X)
        if (param.tabType != null && param.tabType != TabType.NONE) {
            return getTabTypeSuggestions(param.tabType, input);
        }

        // Auto-detect from type
        Function<String, List<String>> provider = tabProviders.get(param.type);
        if (provider != null) return provider.apply(input);

        // Enum auto-complete
        if (param.type.isEnum()) {
            return Arrays.stream(param.type.getEnumConstants())
                    .map(e -> e.toString().toLowerCase())
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<String> getTabTypeSuggestions(TabType type, String input) {
        return switch (type) {
            case ONLINE_PLAYERS -> Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName).collect(Collectors.toList());
            case MATERIALS -> Arrays.stream(Material.values())
                    .filter(m -> !m.isLegacy())
                    .map(m -> m.name().toLowerCase())
                    .filter(n -> n.startsWith(input.toLowerCase()))
                    .limit(30).collect(Collectors.toList());
            case WORLDS -> Bukkit.getWorlds().stream()
                    .map(World::getName).collect(Collectors.toList());
            case NUMBERS -> IntStream.rangeClosed(1, 64)
                    .mapToObj(String::valueOf).collect(Collectors.toList());
            case BOOLEAN -> List.of("true", "false");
            default -> Collections.emptyList();
        };
    }

    private List<String> filterStartsWith(List<String> list, String prefix) {
        if (prefix.isEmpty()) return list;
        String lower = prefix.toLowerCase();
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }

    // ========== Help ==========

    private void sendHelp(CommandSender sender, CmdNode node) {
        sender.sendMessage("§6/" + node.getFullKey() + " §7- " +
                (node.description.isEmpty() ? "Available subcommands:" : node.description));
        for (CmdNode sub : node.subcommands.values()) {
            if (sub.permission == null || sender.hasPermission(sub.permission)) {
                String desc = sub.description.isEmpty() ? "" : " §7- " + sub.description;
                String usage = sub.method != null ? " " + buildUsage(sub.parameters) : "";
                sender.sendMessage("  §e/" + node.getFullKey() + " " + sub.name + "§f" + usage + desc);
            }
        }
    }

    // ========== Inner classes ==========

    private Object createInnerInstance(Class<?> inner, Class<?> outer, Object outerInstance) {
        try {
            Constructor<?> ctor = inner.getDeclaredConstructor(outer);
            ctor.setAccessible(true);
            return ctor.newInstance(outerInstance);
        } catch (NoSuchMethodException e) {
            try {
                Constructor<?> ctor = inner.getDeclaredConstructor();
                ctor.setAccessible(true);
                return ctor.newInstance();
            } catch (Exception ex) {
                plugin.getLogger().warning("Cannot instantiate: " + inner.getName());
                return null;
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Cannot instantiate: " + inner.getName());
            return null;
        }
    }

    private CmdNode getOrCreateRoot(String name) {
        return rootCommands.computeIfAbsent(name, CmdNode::new);
    }

    // ========== Command node ==========

    static class CmdNode {
        final String name;
        CmdNode parent;
        Method method;
        Object instance;
        String description = "";
        String permission = null;
        final List<String> aliases = new ArrayList<>();
        boolean playerOnly = false;
        boolean consoleOnly = false;
        int cooldownSeconds = 0;
        String cooldownBypass = "";
        List<ParamInfo> parameters = new ArrayList<>();
        final Map<String, CmdNode> subcommands = new LinkedHashMap<>();
        boolean registered = false;

        CmdNode(String name) {
            this.name = name;
        }

        CmdNode getOrCreateSub(String subName) {
            return subcommands.computeIfAbsent(subName, n -> {
                CmdNode node = new CmdNode(n);
                node.parent = this;
                return node;
            });
        }

        /**
         * Find subcommand by name OR alias.
         */
        CmdNode findSub(String nameOrAlias) {
            String lower = nameOrAlias.toLowerCase();
            CmdNode direct = subcommands.get(lower);
            if (direct != null) return direct;
            for (CmdNode sub : subcommands.values()) {
                if (sub.aliases.stream().anyMatch(a -> a.equalsIgnoreCase(lower))) {
                    return sub;
                }
            }
            return null;
        }

        String getFullKey() {
            if (parent == null) return name;
            return parent.getFullKey() + " " + name;
        }
    }

    static class ParamInfo {
        Class<?> type;
        String name;
        boolean optional = false;
        String defaultValue = null;
        boolean isSender = false;
        boolean joinArgs = false;
        TabType tabType = null;
        String[] staticSuggestions = null;
    }

    private static class ArgParseException extends RuntimeException {
        ArgParseException(String message) {
            super(message);
        }
    }
}
