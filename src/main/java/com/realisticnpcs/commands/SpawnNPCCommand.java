package com.realisticnpcs.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.realisticnpcs.registry.ModEntityTypes;
import com.realisticnpcs.entities.NPCEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

// Clase que implementa el comando para spawnear NPCs
public class SpawnNPCCommand {
    // Registro del comando
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnnpc")
                .requires(source -> source.hasPermission(2)) // Nivel de permiso requerido
                .executes(context -> {
                    try {
                        // Obtener la fuente del comando y el nivel
                        CommandSourceStack source = context.getSource();
                        ServerLevel level = source.getLevel();

                        // Crear posición de spawn
                        BlockPos pos = new BlockPos(
                                (int)source.getPosition().x,
                                (int)source.getPosition().y,
                                (int)source.getPosition().z
                        );

                        // Crear y spawnear el NPC
                        NPCEntity npc = ModEntityTypes.NPC.get().create(level);
                        if (npc != null) {
                            npc.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                            level.addFreshEntity(npc);
                            source.sendSuccess(() ->
                                    Component.literal("NPC spawned successfully!"), true);
                        } else {
                            source.sendFailure(Component.literal("Failed to create NPC"));
                        }

                        return Command.SINGLE_SUCCESS;
                    } catch (Exception e) {
                        context.getSource().sendFailure(
                                Component.literal("Error spawning NPC: " + e.getMessage()));
                        return 0;
                    }
                }));
    }
}