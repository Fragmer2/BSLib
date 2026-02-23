package io.github.fragmer2.bslib.paper;

import io.github.fragmer2.bslib.api.BSLib;
import io.github.fragmer2.bslib.api.FrameworkPlugin;
import io.github.fragmer2.bslib.api.GuiManagerProvider;
import io.github.fragmer2.bslib.api.cooldown.Cooldowns;
import io.github.fragmer2.bslib.api.debug.Debug;
import io.github.fragmer2.bslib.api.debug.GuiInspector;
import io.github.fragmer2.bslib.api.di.ServiceContainer;
import io.github.fragmer2.bslib.api.event.Bus;
import io.github.fragmer2.bslib.api.event.Events;
import io.github.fragmer2.bslib.api.feature.Features;
import io.github.fragmer2.bslib.api.interaction.Interact;
import io.github.fragmer2.bslib.api.module.ModuleManager;
import io.github.fragmer2.bslib.api.placeholder.Placeholders;
import io.github.fragmer2.bslib.api.reactive.ReactiveBinding;
import io.github.fragmer2.bslib.api.service.Services;
import io.github.fragmer2.bslib.api.session.Sessions;
import io.github.fragmer2.bslib.api.task.Tasks;
import io.github.fragmer2.bslib.api.thread.Async;
import io.github.fragmer2.bslib.paper.command.PaperCommandRegistryFactory;
import io.github.fragmer2.bslib.paper.gui.GuiManager;
import io.github.fragmer2.bslib.paper.module.PaperModuleContext;
import io.github.fragmer2.bslib.paper.module.PaperModuleManager;
import io.github.fragmer2.bslib.paper.placeholder.PaperPlaceholderRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BSLibPlugin extends JavaPlugin {

    private static BSLibPlugin instance;

    private GuiManager guiManager;
    private ServiceContainer container;
    private ModuleManager moduleManager;
    private PaperModuleManager paperModuleManager;

    @Override
    public void onEnable() {
        instance = this;

        // 1. Service Container (internal DI)
        container = new ServiceContainer();
        BSLib.setContainer(container);

        // 2. Task API
        Tasks.init(this);

        // 3. Async Safety
        Async.init(getLogger());

        // 4. GUI System
        guiManager = new GuiManager();
        guiManager.register(this);
        GuiManagerProvider.setInstance(new PaperGuiManager(guiManager));

        // 5. Placeholder Engine
        PaperPlaceholderRegistry placeholderRegistry = new PaperPlaceholderRegistry();
        Placeholders.setRegistry(placeholderRegistry);

        // 6. Command Framework
        BSLib.setCommandRegistryFactory(new PaperCommandRegistryFactory());

        // 7. API Module Manager (for FrameworkPlugin modules)
        moduleManager = new ModuleManager(this, container);
        BSLib.setModuleManager(moduleManager);

        // 8. Sessions
        Sessions.init(this);

        // 9. Interaction API
        Interact.init(this);

        // 10. Register core services in DI
        container.register(GuiManager.class, guiManager);
        container.register(PaperPlaceholderRegistry.class, placeholderRegistry);

        // 11. Paper Module Manager (for internal BSLib Paper modules)
        PaperModuleContext paperContext = new PaperModuleContext(container);
        paperModuleManager = new PaperModuleManager(this, paperContext);
        paperModuleManager.enableAll();

        getLogger().info("BSLib enabled — all systems ready.");
    }

    @Override
    public void onDisable() {
        // Disable Paper modules first (reverse order)
        if (paperModuleManager != null) paperModuleManager.disableAll();

        // Then disable API modules
        if (moduleManager != null) moduleManager.disableAll();

        if (guiManager != null) guiManager.cancel();

        Services.clear();
        Sessions.clear();
        Bus.clear();
        Events.clear();
        Cooldowns.clear();
        Features.clear();
        Interact.clear();
        ReactiveBinding.clear();
        io.github.fragmer2.bslib.api.state.States.shutdown();
        GuiInspector.clearStats();
        Debug.resetTimings();
        io.github.fragmer2.bslib.api.message.Msg.clear();

        if (container != null) container.clear();

        getLogger().info("BSLib disabled.");
    }

    // ========== Commands ==========

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("bslib")) return false;

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "debug"   -> handleDebug(sender);
            case "dev"     -> handleDev(sender);
            case "reload"  -> handleReload(sender, Arrays.copyOfRange(args, 1, args.length));
            case "plugins" -> handlePlugins(sender);
            case "modules" -> handleModules(sender);
            default -> sender.sendMessage("§cUnknown: " + args[0] + ". Use §e/bslib§c for help.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> cmds = new ArrayList<>();
            if (sender.hasPermission("bslib.debug")) cmds.addAll(List.of("debug", "plugins", "modules"));
            if (sender.hasPermission("bslib.dev"))    cmds.add("dev");
            if (sender.hasPermission("bslib.reload")) cmds.add("reload");
            return filter(cmds, args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            List<String> names = new ArrayList<>();
            names.add("hard");
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p instanceof FrameworkPlugin) names.add(p.getName());
            }
            return filter(names, args[1]);
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("reload") && args[1].equalsIgnoreCase("hard")) {
            List<String> names = new ArrayList<>();
            for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                if (p instanceof FrameworkPlugin) names.add(p.getName());
            }
            return filter(names, args[2]);
        }
        return List.of();
    }

    private List<String> filter(List<String> list, String prefix) {
        String lower = prefix.toLowerCase();
        return list.stream().filter(s -> s.toLowerCase().startsWith(lower)).toList();
    }

    // ========== Command Handlers ==========

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§lBSLib §7v" + getDescription().getVersion());
        sender.sendMessage("");
        sender.sendMessage("§e/bslib debug §7— system report (memory, TPS, plugins)");
        sender.sendMessage("§e/bslib dev §7— toggle GUI inspector mode");
        sender.sendMessage("§e/bslib reload <plugin> §7— live reload a plugin");
        sender.sendMessage("§e/bslib reload hard <plugin> §7— full re-register reload");
        sender.sendMessage("§e/bslib plugins §7— list all FrameworkPlugin plugins");
        sender.sendMessage("§e/bslib modules §7— list all active Paper modules");
    }

    private void handleDebug(CommandSender sender) {
        if (!sender.hasPermission("bslib.debug")) {
            sender.sendMessage("§cNo permission.");
            return;
        }
        Debug.enable();
        Debug.report(sender);

        sender.sendMessage("");
        sender.sendMessage("§6§lBSLib Systems:");
        int fwCount = 0;
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p instanceof FrameworkPlugin) fwCount++;
        }
        sender.sendMessage("  §7FrameworkPlugins: §f" + fwCount);
        sender.sendMessage("  §7Active sessions: §f" + Bukkit.getOnlinePlayers().size());
        sender.sendMessage("  §7Dev mode players: §f" +
                Bukkit.getOnlinePlayers().stream().filter(GuiInspector::isEnabled).count());
        sender.sendMessage("  §7Paper modules: §f" + paperModuleManager.getEnabledCount());
    }

    private void handleDev(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cPlayers only.");
            return;
        }
        if (!sender.hasPermission("bslib.dev")) {
            sender.sendMessage("§cNo permission.");
            return;
        }
        GuiInspector.toggle(player);
    }

    private void handleReload(CommandSender sender, String[] args) {
        if (!sender.hasPermission("bslib.reload")) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /bslib reload <plugin> §7or §c/bslib reload hard <plugin>");
            return;
        }

        boolean hard = args[0].equalsIgnoreCase("hard");
        String pluginName = hard ? (args.length > 1 ? args[1] : null) : args[0];

        if (pluginName == null) {
            sender.sendMessage("§cUsage: /bslib reload hard <plugin>");
            return;
        }

        Plugin target = Bukkit.getPluginManager().getPlugin(pluginName);
        if (target == null) {
            sender.sendMessage("§cPlugin not found: " + pluginName);
            return;
        }
        if (!(target instanceof FrameworkPlugin fp)) {
            sender.sendMessage("§c" + pluginName + " is not a FrameworkPlugin. Cannot live reload.");
            return;
        }

        long start = System.currentTimeMillis();
        try {
            if (hard) {
                fp.hardReload();
                sender.sendMessage("§a⚡ Hard reloaded §f" + pluginName + " §7(" +
                        (System.currentTimeMillis() - start) + "ms)");
            } else {
                fp.liveReload();
                sender.sendMessage("§a⚡ Live reloaded §f" + pluginName + " §7(" +
                        (System.currentTimeMillis() - start) + "ms)");
            }
        } catch (Exception e) {
            sender.sendMessage("§c⚠ Reload failed: " + e.getMessage());
            getLogger().severe("Reload failed for " + pluginName);
            e.printStackTrace();
        }
    }

    private void handlePlugins(CommandSender sender) {
        if (!sender.hasPermission("bslib.debug")) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        sender.sendMessage("§6§lFrameworkPlugin Plugins:");
        for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
            if (p instanceof FrameworkPlugin fp) {
                String status = fp.isEnabled() ? "§a●" : "§c●";
                String scan = fp.isAutoScanned() ? " §7[AutoScan]" : "";
                int managed = fp.getManagedObjects().size();
                int configs = fp.getConfigs().size();
                sender.sendMessage("  " + status + " §f" + fp.getName() + scan +
                        " §7(objects: " + managed + ", configs: " + configs + ")");
            }
        }
    }

    private void handleModules(CommandSender sender) {
        if (!sender.hasPermission("bslib.debug")) {
            sender.sendMessage("§cNo permission.");
            return;
        }
        sender.sendMessage("§6§lBSLib Paper Modules:");
        paperModuleManager.listModules().forEach(line -> sender.sendMessage("  " + line));
    }

    // ========== Accessors ==========

    public static BSLibPlugin getInstance()            { return instance; }
    public GuiManager getGuiManager()                  { return guiManager; }
    public ServiceContainer getContainer()             { return container; }
    public ModuleManager getModuleManager()            { return moduleManager; }
    public PaperModuleManager getPaperModuleManager()  { return paperModuleManager; }
}
