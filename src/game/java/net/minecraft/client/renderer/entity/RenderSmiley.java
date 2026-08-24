package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSmiley;
import net.minecraft.entity.monster.EntitySmiley;
import net.minecraft.util.ResourceLocation;

public class RenderSmiley extends RenderBiped<EntitySmiley> {
    private static final ResourceLocation SMILEY_TEXTURES = new ResourceLocation("textures/entity/custom/smiley.png");

    public RenderSmiley(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSmiley(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySmiley entity) {
        return SMILEY_TEXTURES;
    }
}