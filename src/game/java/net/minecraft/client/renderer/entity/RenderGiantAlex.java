package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGiantAlex;
import net.minecraft.entity.monster.EntityGiantAlex;
import net.minecraft.util.ResourceLocation;

public class RenderGiantAlex extends RenderBiped<EntityGiantAlex> {
    private static final ResourceLocation GIANTALEX_TEXTURES = new ResourceLocation("textures/entity/custom/giantalex.png");

    public RenderGiantAlex(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGiantAlex(), 2.5F); // Increased shadow size to match 5x scale
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGiantAlex entity) {
        return GIANTALEX_TEXTURES;
    }
}