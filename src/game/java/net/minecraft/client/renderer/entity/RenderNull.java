package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelNull;
import net.minecraft.entity.monster.EntityNull;
import net.minecraft.util.ResourceLocation;

public class RenderNull extends RenderBiped<EntityNull> {
    private static final ResourceLocation NULL_TEXTURES = new ResourceLocation("textures/entity/custom/null.png");

    public RenderNull(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelNull(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNull entity) {
        return NULL_TEXTURES;
    }
}