package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelAlex;
import net.minecraft.entity.monster.EntityAlex;
import net.minecraft.util.ResourceLocation;

public class RenderAlex extends RenderBiped<EntityAlex> {
    private static final ResourceLocation ALEX_TEXTURES = new ResourceLocation("textures/entity/custom/alex.png");

    public RenderAlex(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelAlex(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAlex entity) {
        return ALEX_TEXTURES;
    }
}