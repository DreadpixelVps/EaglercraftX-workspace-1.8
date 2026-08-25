package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEntity303;
import net.minecraft.entity.monster.EntityEntity303;
import net.minecraft.util.ResourceLocation;

public class RenderEntity303 extends RenderBiped<EntityEntity303> {
    private static final ResourceLocation ENTITY303_TEXTURES = new ResourceLocation("textures/entity/custom/entity303.png");

    public RenderEntity303(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelEntity303(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEntity303 entity) {
        return ENTITY303_TEXTURES;
    }
}