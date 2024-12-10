package com.realisticnpcs.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NPCEntity extends PathfinderMob {
    private static final EntityDataAccessor<String> SKIN_TYPE =
            SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SKIN_URL =
            SynchedEntityData.defineId(NPCEntity.class, EntityDataSerializers.STRING);

    private String skinType = "default";
    private String skinUrl = "";

    public NPCEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SKIN_TYPE, "default");
        this.entityData.define(SKIN_URL, "");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("SkinType", getSkinType());
        tag.putString("SkinURL", getSkinUrl());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SkinType")) {
            setSkinType(tag.getString("SkinType"));
        }
        if (tag.contains("SkinURL")) {
            setSkinUrl(tag.getString("SkinURL"));
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String type) {
        this.skinType = type;
        this.entityData.set(SKIN_TYPE, type);
    }

    public String getSkinUrl() {
        return this.entityData.get(SKIN_URL);
    }

    public void setSkinUrl(String url) {
        this.entityData.set(SKIN_URL, url);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }
}