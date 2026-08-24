package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityGreenSteve;
import net.minecraft.entity.player.EntityPlayer;

public class EntityGreenSteveNice extends EntityAIBase {
    private final EntityGreenSteve theEntity;
    private int executionTick;
    private boolean hasTeleported;
    private EntityPlayer targetPlayer;

    public EntityGreenSteveNice(EntityGreenSteve greenSteve) {
        this.theEntity = greenSteve;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.executionTick = 0;
        this.hasTeleported = false;
        this.targetPlayer = null;
    }

    public boolean continueExecuting() {
        return this.executionTick < 600;
    }

    public void updateTask() {
        this.executionTick++;

        if (!this.hasTeleported && this.executionTick > 2) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 48.0D);
            if (this.targetPlayer != null) {
                double angle = this.theEntity.getRNG().nextDouble() * Math.PI * 2.0D;
                double targetX = this.targetPlayer.posX + Math.cos(angle) * 20.0D;
                double targetZ = this.targetPlayer.posZ + Math.sin(angle) * 20.0D;
                double targetY = this.targetPlayer.posY;

                this.theEntity.setPositionAndUpdate(targetX, targetY, targetZ);
                this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
            }
            this.hasTeleported = true;
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
            
            double distanceSq = this.theEntity.getDistanceSqToEntity(this.targetPlayer);
            if (distanceSq <= 100.0D) {
                this.theEntity.setDead();
            }
        }

        if (this.executionTick >= 590) {
            this.theEntity.setDead();
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}