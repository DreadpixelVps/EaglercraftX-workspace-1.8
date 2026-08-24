package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityError;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class EntityAIErrorBehavior extends EntityAIBase {
    private final EntityError theEntity;
    private EntityPlayer targetPlayer;

    public EntityAIErrorBehavior(EntityError errorEntity) {
        this.theEntity = errorEntity;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.targetPlayer = null;
    }

    public boolean continueExecuting() {
        return true;
    }

    public void updateTask() {
        if (this.theEntity.posY < 60.0D) {
            this.theEntity.setDead();
            return;
        }

        double targetY = this.theEntity.posY - 10.0D;
        int groundY = this.theEntity.worldObj.getTopSolidOrLiquidBlock(new net.minecraft.util.BlockPos(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ)).getY();
        double idealY = Math.max(targetY, (double)(groundY + 10));

        this.theEntity.motionX = 0.0D;
        this.theEntity.motionY = (idealY - this.theEntity.posY) * 0.1D;
        this.theEntity.motionZ = 0.0D;

        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive()) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 32.0D);
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

            double distanceSq = this.theEntity.getDistanceSqToEntity(this.targetPlayer);
            if (distanceSq <= 225.0D) {
                if (!this.theEntity.worldObj.isRemote && this.targetPlayer instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) this.targetPlayer).playerNetServerHandler.kickPlayerFromServer("Error Chunk Does Not Exist :)");
                }
                this.theEntity.setDead();
            }
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}