package me.kervand.optimizer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptimizerCommand extends BukkitCommand {

    private final Component helpComponent;
    private final OptimizerPlugin plugin;
    public OptimizerCommand(OptimizerPlugin plugin) {
        super("optimizer");
        this.plugin = plugin;
        setPermission("optimizer.use");
        helpComponent = Component.text("DOptimizer | Help")
                .color(NamedTextColor.RED).append(Component.newline())
                .append(Component.text("   /optimizer enable <group>").color(NamedTextColor.GRAY)
                        .append(Component.text(" - enables the AI for the given group.").color(NamedTextColor.WHITE)))
                .append(Component.newline())
                .append(Component.text("   /optimizer disable <group>").color(NamedTextColor.GRAY)
                        .append(Component.text(" - disables the AI for the given group.").color(NamedTextColor.WHITE)))
                .append(Component.newline())
                .append(Component.text("   /optimizer groups").color(NamedTextColor.GRAY)
                        .append(Component.text(" - shows what groups are disabled & enabled.").color(NamedTextColor.WHITE)))
                .append(Component.newline())
                .append(Component.text("   /optimizer reload").color(NamedTextColor.GRAY)
                        .append(Component.text(" - reloads the config.").color(NamedTextColor.WHITE)))
                .append(Component.newline());
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {

        if (strings.length == 0) {
            commandSender.sendMessage(helpComponent);
            return true;
        }

        switch (strings[0].toLowerCase()) {
            case "reload" -> handleReloadCommand(commandSender);
            case "disable" -> handleDisableCommand(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
            case "enable" -> handleEnableCommand(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
            case "groups" -> handleGroupsCommand(commandSender);
            default -> commandSender.sendMessage(helpComponent);
        }

        return true;
    }

    private void handleDisableCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("Usage: /optimizer disable <group_name>").color(NamedTextColor.WHITE)));
            return;
        }

        String groupName = args[0];
        if (!plugin.getGroupMap().containsKey(groupName)) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("Group " + groupName + " not found.").color(NamedTextColor.WHITE)));
            return;
        }

        if (!plugin.getGroupMap().get(groupName).isEnabled()) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("AI for " + groupName + " is already disabled.").color(NamedTextColor.WHITE)));
            return;
        }

        plugin.getGroupMap().get(groupName).disable();
        sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                .append(Component.text("Disabled the AI for all the mobs in " + groupName).color(NamedTextColor.WHITE)));

    }

    private void handleEnableCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("Usage: /optimizer enable <group_name>").color(NamedTextColor.WHITE)));
            return;
        }

        String groupName = args[0];
        if (!plugin.getGroupMap().containsKey(groupName)) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("Group " + groupName + " not found.").color(NamedTextColor.WHITE)));
            return;
        }

        if (plugin.getGroupMap().get(groupName).isEnabled()) {
            sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                    .append(Component.text("AI for " + groupName + " is already enabled.").color(NamedTextColor.WHITE)));
            return;
        }

        plugin.getGroupMap().get(groupName).enable();
        sender.sendMessage(Component.text("DOptimizer | ").color(NamedTextColor.RED)
                .append(Component.text("Enabled the AI for all the mobs in " + groupName).color(NamedTextColor.WHITE)));

    }

    private void handleGroupsCommand(CommandSender sender) {

        Component component = Component.text("DOptimizer | ")
                .color(NamedTextColor.RED).append(Component.text("Here are all the registered groups:").color(NamedTextColor.WHITE))
                .append(Component.newline());

        List<String> groups = plugin.getGroupMap().keySet().stream().toList();
        for (int i = 0; i < groups.size(); i++) {
            component = component.append(
                    Component.text(groups.get(i))
                            .color(plugin.getGroupMap().get(groups.get(i)).isEnabled()
                                    ? NamedTextColor.GREEN
                                    : NamedTextColor.RED)
            );
            if (i + 1 < groups.size()) {
                component = component.append(Component.text(", ").color(NamedTextColor.WHITE));
            }
        }
        sender.sendMessage(component);

    }

    private void handleReloadCommand(CommandSender sender) {
        plugin.reload();
        sender.sendMessage(Component.text("DOptimizer | ")
                .color(NamedTextColor.RED).append(Component.text("Reloaded successfully").color(NamedTextColor.WHITE)));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        if (args.length <= 1) {
            return List.of("groups", "enable", "disable", "reload");
        }

        return Collections.emptyList();
    }
}
