package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class EntityAINullScared extends EntityAIBase {
    private final EntityNull theEntity;
    private int executionTick;
    private boolean hasTeleported;
    private boolean hasAttacked;
    private boolean hasSpoken;
    private EntityPlayer targetPlayer;

    public EntityAINullScared(EntityNull entityNull) {
        this.theEntity = entityNull;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.executionTick = 0;
        this.hasTeleported = false;
        this.hasAttacked = false;
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
                double yawRad = Math.toRadians(this.targetPlayer.rotationYaw + 90.0F);
                double x = this.targetPlayer.posX - Math.cos(yawRad) * 2.0D;
                double z = this.targetPlayer.posZ - Math.sin(yawRad) * 2.0D;
                double y = this.targetPlayer.posY;

                this.theEntity.setPositionAndUpdate(x, y, z);
                this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
            }
            this.hasTeleported = true;
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
        }

        if (!this.hasAttacked && this.executionTick >= 20) {
            if (this.targetPlayer != null && this.theEntity.getDistanceSqToEntity(this.targetPlayer) < 9.0D) {
                this.theEntity.attackEntityAsMob(this.targetPlayer);
            }
            this.hasAttacked = true;
        }

        if (!this.hasSpoken && this.executionTick >= 40) {
            if (!this.theEntity.worldObj.isRemote && this.targetPlayer != null) {
                this.targetPlayer.addChatMessage(new ChatComponentText("§fNull: Your not safe here"));
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