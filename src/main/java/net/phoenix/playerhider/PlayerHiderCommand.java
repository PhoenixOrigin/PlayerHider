package net.phoenix.playerhider;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PlayerHiderCommand {

    public void build(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> source = literal("playerhider")
                .then(literal("add")
                        .then(argument("player", StringArgumentType.word())
                                .executes(this::add)))
                .then(literal("remove")
                        .then(argument("player", StringArgumentType.word())
                                .executes(this::remove)))
                .then(literal("list")
                        .executes(this::list))
                .then(literal("clear")
                        .executes(this::clear))
                .executes(this::help);
        LiteralCommandNode<FabricClientCommandSource> node = dispatcher.register(source);
        dispatcher.register(literal("ph").redirect(node));
    }

    private int add(CommandContext<FabricClientCommandSource> context) {
        if (context.getArgument("player", String.class) != null) {
            PlayerHider.addBlockedPlayer(context.getArgument("player", String.class));
            return 1;
        }
        return 0;
    }

    private int clear(CommandContext<FabricClientCommandSource> context) {
        for(String player : PlayerHider.username) {
            PlayerHider.removeBlockedPlayer(player);
        }
        return 0;
    }

    private int remove(CommandContext<FabricClientCommandSource> context) {
        if (context.getArgument("player", String.class) != null) {
            PlayerHider.removeBlockedPlayer(context.getArgument("player", String.class));
            return 1;
        }
        return 0;
    }

    private int list(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.of("Blocked Players: \n" + PlayerHider.username.stream().collect(StringBuilder::new, (sb, s) -> sb.append(s).append("\n"), StringBuilder::append)));
        return 0;
    }

    private int help(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.of("/playerhider <add/remove/list> <player>"));
        return 0;
    }
}
