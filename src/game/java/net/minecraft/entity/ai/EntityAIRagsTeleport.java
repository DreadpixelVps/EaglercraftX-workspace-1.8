package net.minecraft.entity.ai;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityRags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;

public class EntityAIRagsTeleport extends EntityAIBase {
    private final EntityRags theEntity;
    private int executionTick;
    private int teleportCooldown;
    private EntityPlayer targetPlayer;

    public EntityAIRagsTeleport(EntityRags rags) {
        this.theEntity = rags;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.executionTick = 0;
        this.teleportCooldown = 0;
        this.targetPlayer = null;
    }

    public boolean continueExecuting() {
        return this.executionTick < 200;
    }

    public void updateTask() {
        this.executionTick++;
        this.teleportCooldown--;

        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive()) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 32.0D);
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

            if (this.teleportCooldown <= 0) {
                double oldX = this.theEntity.posX;
                double oldY = this.theEntity.posY;
                double oldZ = this.theEntity.posZ;

                double distance = 5.0D + (this.theEntity.getRNG().nextDouble() * 5.0D);
                double angle = this.theEntity.getRNG().nextDouble() * Math.PI * 2.0D;

                double targetX = this.targetPlayer.posX + Math.cos(angle) * distance;
                double targetZ = this.targetPlayer.posZ + Math.sin(angle) * distance;
                double targetY = this.targetPlayer.posY;

                this.theEntity.setPositionAndUpdate(targetX, targetY, targetZ);
                this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

                if (!this.theEntity.worldObj.isRemote) {
                    this.theEntity.worldObj.addWeatherEffect(new EntityLightningBolt(this.theEntity.worldObj, oldX, oldY, oldZ));
                }

                this.teleportCooldown = 20;
            }
        }

        if (this.executionTick >= 195) {
            if (!this.theEntity.worldObj.isRemote && this.targetPlayer != null) {
                this.targetPlayer.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                this.targetPlayer.addChatMessage(new ChatComponentText("§fUnknown: Your heart does beat..."));
            }
            this.theEntity.setDead();
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}