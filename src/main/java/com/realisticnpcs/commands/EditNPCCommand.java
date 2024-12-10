package com.realisticnpcs.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.realisticnpcs.entities.NPCEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EditNPCCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("npc")
                .then(Commands.literal("name")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> setNPCName(context.getSource(),
                                        StringArgumentType.getString(context, "name")))))
                .then(Commands.literal("skin")
                        .then(Commands.argument("skinType", StringArgumentType.word())
                                .executes(context -> setNPCSkin(context.getSource(),
                                        StringArgumentType.getString(context, "skinType")))))
                .then(Commands.literal("url")
                        .then(Commands.argument("url", StringArgumentType.greedyString())
                                .executes(context -> setSkinUrl(context.getSource(),
                                        StringArgumentType.getString(context, "url"))))));
    }

    private static int setNPCName(CommandSourceStack source, String name) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be used by a player"));
            return 0;
        }

        NPCEntity target = getNearestNPC(player);
        if (target == null) {
            source.sendFailure(Component.literal("No NPC found nearby! Get closer to an NPC."));
            return 0;
        }

        target.setCustomName(Component.literal(name));
        target.setCustomNameVisible(true);
        source.sendSuccess(() -> Component.literal("Set NPC name to: " + name), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setNPCSkin(CommandSourceStack source, String skinType) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be used by a player"));
            return 0;
        }

        NPCEntity target = getNearestNPC(player);
        if (target == null) {
            source.sendFailure(Component.literal("No NPC found nearby! Get closer to an NPC."));
            return 0;
        }

        target.setSkinType(skinType);
        source.sendSuccess(() -> Component.literal("Set NPC skin to: " + skinType), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setSkinUrl(CommandSourceStack source, String url) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be used by a player"));
            return 0;
        }

        NPCEntity target = getNearestNPC(player);
        if (target == null) {
            source.sendFailure(Component.literal("No NPC found nearby! Get closer to an NPC."));
            return 0;
        }

        if (!url.startsWith("http")) {
            source.sendFailure(Component.literal("Invalid URL! Must start with http:// or https://"));
            return 0;
        }

        target.setSkinUrl(url);
        source.sendSuccess(() -> Component.literal("Set NPC skin URL to: " + url), true);
        return Command.SINGLE_SUCCESS;
    }

    private static NPCEntity getNearestNPC(ServerPlayer player) {
        Vec3 pos = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        double range = 5.0D;

        AABB searchBox = player.getBoundingBox().inflate(range);

        return player.level().getEntitiesOfClass(NPCEntity.class, searchBox).stream()
                .filter(npc -> {
                    Vec3 toNPC = npc.position().subtract(pos).normalize();
                    return toNPC.dot(look) > 0.7D;
                })
                .min((npc1, npc2) -> {
                    double dist1 = npc1.distanceToSqr(player);
                    double dist2 = npc2.distanceToSqr(player);
                    return Double.compare(dist1, dist2);
                })
                .orElse(null);
    }
}
