package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGreenSteve;
import net.minecraft.entity.monster.EntityGreenSteve;
import net.minecraft.util.ResourceLocation;

public class RenderGreenSteve extends RenderBiped<EntityGreenSteve> {
    private static final ResourceLocation GREENSTEVE_TEXTURES = new ResourceLocation("textures/entity/custom/greensteve.png");

    public RenderGreenSteve(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGreenSteve(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGreenSteve entity) {
        return GREENSTEVE_TEXTURES;
    }
}