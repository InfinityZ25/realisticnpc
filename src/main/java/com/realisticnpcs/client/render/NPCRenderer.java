package com.realisticnpcs.client.render;

import com.realisticnpcs.RealisticNPCs;
import com.realisticnpcs.entities.NPCEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

// Clase responsable de renderizar el NPC en el mundo
public class NPCRenderer extends HumanoidMobRenderer<NPCEntity, PlayerModel<NPCEntity>> {
    // Ubicación de la textura del NPC
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(RealisticNPCs.MOD_ID, "textures/entity/npc.png");

    // Constructor del renderizador
    public NPCRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    }

    // Metodo que devuelve la textura a usar
    @Override
    @NotNull
    public ResourceLocation getTextureLocation(@NotNull NPCEntity entity) {
        return TEXTURE;
    }
}