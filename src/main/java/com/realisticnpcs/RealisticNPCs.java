package com.realisticnpcs;

import com.realisticnpcs.commands.SpawnNPCCommand;
import com.realisticnpcs.commands.EditNPCCommand;  // Nuevo import
import com.realisticnpcs.entities.NPCEntity;
import com.realisticnpcs.registry.ModEntityTypes;
import com.realisticnpcs.client.render.NPCRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RealisticNPCs.MOD_ID)
public class RealisticNPCs {
    public static final String MOD_ID = "realisticnpcs";
    private static final Logger LOGGER = LogManager.getLogger();

    public RealisticNPCs() {
        LOGGER.info("Initializing Realistic NPCs Mod");

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registrar entidades
        ModEntityTypes.register(modEventBus);

        // Registrar eventos
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(this::clientSetup);

        // Registrar comandos
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Realistic NPCs Mod Initialized!");
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.NPC.get(), NPCEntity.createAttributes().build());
    }

    private void registerCommands(RegisterCommandsEvent event) {
        SpawnNPCCommand.register(event.getDispatcher());  // Comando original
        EditNPCCommand.register(event.getDispatcher());   // Nuevo comando
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntityTypes.NPC.get(), NPCRenderer::new);
        });
    }
}