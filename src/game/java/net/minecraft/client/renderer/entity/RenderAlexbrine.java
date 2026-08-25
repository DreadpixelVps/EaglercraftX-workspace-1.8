package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelAlexbrine;
import net.minecraft.entity.monster.EntityAlexbrine;
import net.minecraft.util.ResourceLocation;

public class RenderAlexbrine extends RenderBiped<EntityAlexbrine> {
    private static final ResourceLocation ALEXBRINE_TEXTURES = new ResourceLocation("textures/entity/custom/alexbrine.png");

    public RenderAlexbrine(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelAlexbrine(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityAlexbrine entity) {
        return ALEXBRINE_TEXTURES;
    }
}