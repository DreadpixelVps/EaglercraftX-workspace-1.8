package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelInvert;
import net.minecraft.entity.monster.EntityInvert;
import net.minecraft.util.ResourceLocation;

public class RenderInvert extends RenderBiped<EntityInvert> {
    private static final ResourceLocation SLEEP_TEXTURE = new ResourceLocation("textures/entity/custom/sleepinvert.png");
    private static final ResourceLocation ACTIVE_TEXTURE = new ResourceLocation("textures/entity/custom/invert.png");

    public RenderInvert(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelInvert(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityInvert entity) {
        if (entity.isAggressiveTexture()) {
            return ACTIVE_TEXTURE;
        } else {
            return SLEEP_TEXTURE;
        }
    }
}