package net.minecraft.client.model;

public class ModelNull extends ModelBiped {

    public ModelNull() {
        this(0.0F);
    }

    public ModelNull(float modelSize) {
        super(modelSize, 0.0F, 64, 64);
    }
}