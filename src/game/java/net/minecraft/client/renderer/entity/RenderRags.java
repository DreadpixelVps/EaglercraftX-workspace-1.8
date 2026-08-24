package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRags;
import net.minecraft.entity.monster.EntityRags;
import net.minecraft.util.ResourceLocation;

public class RenderRags extends RenderBiped<EntityRags> {
    private static final ResourceLocation RAGS_TEXTURES = new ResourceLocation("textures/entity/custom/rags.png");

    public RenderRags(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelRags(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityRags entity) {
        return RAGS_TEXTURES;
    }
}