package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;

public class EntityAINullWorried extends EntityAIBase {
    private final EntityNull theEntity;
    private int executionTick;
    private boolean hasTeleported;
    private boolean hasSpoken;
    private EntityPlayer targetPlayer;

    public EntityAINullWorried(EntityNull entityNull) {
        this.theEntity = entityNull;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.executionTick = 0;
        this.hasTeleported = false;
        this.hasSpoken = false;
        this.targetPlayer = null;
    }

    public boolean continueExecuting() {
        return this.executionTick < 100;
    }

    public void updateTask() {
        this.executionTick++;

        if (!this.hasTeleported && this.executionTick > 5) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 32.0D);
            if (this.targetPlayer != null) {
                double yawRad = Math.toRadians(this.targetPlayer.rotationYaw);
                double x = this.targetPlayer.posX - Math.sin(yawRad) * 2.0D;
                double z = this.targetPlayer.posZ + Math.cos(yawRad) * 2.0D;
                double y = this.targetPlayer.posY;

                this.theEntity.setPositionAndUpdate(x, y, z);
                this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.worldObj.playSoundAtEntity(this.targetPlayer, "custom.running", 1.0F, 1.0F);
                    this.targetPlayer.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                }
            }
            this.hasTeleported = true;
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
        }

        if (!this.hasSpoken && this.executionTick >= 20) {
            if (!this.theEntity.worldObj.isRemote && this.targetPlayer != null) {
                this.targetPlayer.addChatMessage(new ChatComponentText("§fNull: Leave"));
            }
            this.hasSpoken = true;
        }

        if (this.executionTick >= 90) {
            this.theEntity.setDead();
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}