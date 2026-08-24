package net.minecraft.entity.ai;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityGreenSteve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

public class EntityAIGreenSteveMad extends EntityAIBase {
    private final EntityGreenSteve theEntity;
    private int executionTick;
    private boolean hasTeleported;
    private boolean hasGivenEmeralds;
    private boolean hasSpoken;
    private EntityPlayer targetPlayer;

    public EntityAIGreenSteveMad(EntityGreenSteve greenSteve) {
        this.theEntity = greenSteve;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.executionTick = 0;
        this.hasTeleported = false;
        this.hasGivenEmeralds = false;
        this.hasSpoken = false;
        this.targetPlayer = null;
    }

    public boolean continueExecuting() {
        return this.executionTick < 120;
    }

    public void updateTask() {
        this.executionTick++;

        if (!this.hasTeleported && this.executionTick > 2) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 32.0D);
            if (this.targetPlayer != null) {
                double yawRad = Math.toRadians(this.targetPlayer.rotationYaw + 90.0F);
                double targetX = this.targetPlayer.posX - Math.cos(yawRad) * 1.0D;
                double targetZ = this.targetPlayer.posZ - Math.sin(yawRad) * 1.0D;
                double targetY = this.targetPlayer.posY;

                this.theEntity.setPositionAndUpdate(targetX, targetY, targetZ);
                this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

                if (!this.theEntity.worldObj.isRemote) {
                    this.theEntity.worldObj.addWeatherEffect(new EntityLightningBolt(this.theEntity.worldObj, targetX, targetY, targetZ));
                }
            }
            this.hasTeleported = true;
        }

        if (this.targetPlayer != null) {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
            
            if (this.executionTick < 100) {
                this.targetPlayer.posX = this.targetPlayer.lastTickPosX;
                this.targetPlayer.posY = this.targetPlayer.lastTickPosY;
                this.targetPlayer.posZ = this.targetPlayer.lastTickPosZ;
                this.targetPlayer.motionX = 0.0D;
                this.targetPlayer.motionY = 0.0D;
                this.targetPlayer.motionZ = 0.0D;
            }

            if (this.executionTick >= 10 && this.executionTick < 110) {
                Vec3 playerLook = this.targetPlayer.getLook(1.0F).normalize();
                Vec3 toEntity = new Vec3(this.theEntity.posX - this.targetPlayer.posX, (this.theEntity.posY + (double)this.theEntity.getEyeHeight()) - (this.targetPlayer.posY + (double)this.targetPlayer.getEyeHeight()), this.theEntity.posZ - this.targetPlayer.posZ).normalize();
                double dot = playerLook.dotProduct(toEntity);

                if (dot > 0.5D) {
                    if (!this.theEntity.worldObj.isRemote) {
                        this.targetPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 1200, 0));
                    }
                }
            }
        }

        if (!this.hasGivenEmeralds && this.executionTick > 5) {
            if (this.targetPlayer != null && !this.theEntity.worldObj.isRemote) {
                this.targetPlayer.inventory.addItemStackToInventory(new ItemStack(Items.emerald, 2));
            }
            this.hasGivenEmeralds = true;
        }

        if (!this.hasSpoken && this.executionTick >= 20) {
            if (!this.theEntity.worldObj.isRemote && this.targetPlayer != null) {
                this.targetPlayer.addChatMessage(new ChatComponentText("§fGreen Steve: ?"));
            }
            this.hasSpoken = true;
        }

        if (this.executionTick >= 115) {
            this.theEntity.setDead();
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}