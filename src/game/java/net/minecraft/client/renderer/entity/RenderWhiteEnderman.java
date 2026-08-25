package net.minecraft.client.renderer.entity;

import net.minecraft.entity.monster.EntityWhiteEnderman;
import net.minecraft.util.ResourceLocation;

public class RenderWhiteEnderman extends RenderEnderman {
    private static final ResourceLocation WHITE_ENDERMAN_TEXTURES = new ResourceLocation("textures/entity/custom/whiteenderman.png");

    public RenderWhiteEnderman(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(net.minecraft.entity.monster.EntityEnderman entity) {
        return WHITE_ENDERMAN_TEXTURES;
    }
}