package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRedSteve;
import net.minecraft.entity.monster.EntityRedSteve;
import net.minecraft.util.ResourceLocation;

public class RenderRedSteve extends RenderBiped<EntityRedSteve> {
    private static final ResourceLocation REDSTEVE_TEXTURES = new ResourceLocation("textures/entity/custom/redsteve.png");

    public RenderRedSteve(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelRedSteve(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityRedSteve entity) {
        return REDSTEVE_TEXTURES;
    }
}