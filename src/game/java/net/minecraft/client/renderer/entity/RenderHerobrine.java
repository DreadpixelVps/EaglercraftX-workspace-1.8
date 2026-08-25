package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSteve;
import net.minecraft.entity.monster.EntityHerobrine;
import net.minecraft.util.ResourceLocation;

public class RenderHerobrine extends RenderBiped<EntityHerobrine> {
    private static final ResourceLocation HEROBRINE_TEXTURES = new ResourceLocation("textures/entity/custom/herobrine.png");

    public RenderHerobrine(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSteve(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHerobrine entity) {
        return HEROBRINE_TEXTURES;
    }
}