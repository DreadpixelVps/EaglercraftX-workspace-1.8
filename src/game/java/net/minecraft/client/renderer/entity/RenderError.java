package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelError;
import net.minecraft.entity.monster.EntityError;
import net.minecraft.util.ResourceLocation;

public class RenderError extends RenderBiped<EntityError> {
    private static final ResourceLocation ERROR_TEXTURES = new ResourceLocation("textures/entity/custom/error.png");

    public RenderError(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelError(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityError entity) {
        return ERROR_TEXTURES;
    }
}