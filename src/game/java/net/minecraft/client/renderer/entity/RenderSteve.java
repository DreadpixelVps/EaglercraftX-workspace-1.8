package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSteve;
import net.minecraft.entity.monster.EntitySteve;
import net.minecraft.util.ResourceLocation;

public class RenderSteve extends RenderBiped<EntitySteve> {
    private static final ResourceLocation STEVE_TEXTURES = new ResourceLocation("textures/entity/custom/steve.png");

    public RenderSteve(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSteve(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySteve entity) {
        return STEVE_TEXTURES;
    }
}