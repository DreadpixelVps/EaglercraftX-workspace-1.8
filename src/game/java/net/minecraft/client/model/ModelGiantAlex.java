package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

public class ModelGiantAlex extends ModelBiped {

    public ModelGiantAlex() {
        this(0.0F);
    }

    public ModelGiantAlex(float modelSize) {
        super(modelSize, 0.0F, 64, 64);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        GlStateManager.pushMatrix();
        
        GlStateManager.scale(5.0F, 5.0F, 5.0F);
        
        GlStateManager.translate(0.0F, -0.6F, 0.0F);

        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);

        GlStateManager.popMatrix();
    }
}