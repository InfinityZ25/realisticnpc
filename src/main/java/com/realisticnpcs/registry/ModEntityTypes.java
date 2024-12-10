package com.realisticnpcs.registry;

import com.realisticnpcs.RealisticNPCs;
import com.realisticnpcs.entities.NPCEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// Clase que maneja el registro de entidades del mod
public class ModEntityTypes {
    // Registro diferido de entidades
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RealisticNPCs.MOD_ID);

    // Registro de la entidad NPC
    public static final RegistryObject<EntityType<NPCEntity>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.<NPCEntity>of(NPCEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F) // Tamaño del NPC
                            .build(RealisticNPCs.MOD_ID + ":npc"));

    // Metodo para registrar todas las entidades
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}