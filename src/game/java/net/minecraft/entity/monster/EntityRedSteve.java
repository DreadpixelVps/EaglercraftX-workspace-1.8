package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityRedSteve extends EntityMob {

    private int followTimer = 0;
    private static final int DESPAWN_TIME = 600; // 30 seconds (20 ticks per second)

    public EntityRedSteve(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        
        // General AI tasks for wandering and watching the player
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D); // Slightly faster to keep pace
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        // Player cannot hit or kill him (cancels all incoming damage)
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote) {
            // Find the closest player within 32 blocks
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
            
            if (player != null) {
                // Actively follow while maintaining ~4 blocks of distance
                double distanceToPlayer = this.getDistanceSqToEntity(player);
                
                if (distanceToPlayer > 16.0D) { // 4 blocks squared = 16
                    // Move towards player if further than 4 blocks
                    this.getNavigator().tryMoveToEntityLiving(player, 1.2D);
                } else if (distanceToPlayer < 9.0D) {
                    // Back up slightly if closer than 3 blocks to hold the 4-block buffer
                    double d0 = this.posX - player.posX;
                    double d1 = this.posZ - player.posZ;
                    this.getNavigator().tryMoveToXYZ(this.posX + d0, this.posY, this.posZ + d1, 1.0D);
                } else {
                    // Hold position / stop pathfinding when at the desired distance
                    this.getNavigator().clearPathEntity();
                }

                // Increment the tracking timer
                followTimer++;

                // After 30 seconds (600 ticks) of following
                if (followTimer >= DESPAWN_TIME) {
                    // Send message to the player
                    player.addChatMessage(new ChatComponentText("Red Steve: ?"));
                    // Despawn immediately
                    this.setDead();
                    return;
                }
            } else {
                // Reset timer if player goes too far away
                if (followTimer > 0) {
                    followTimer--;
                }
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true; // Can spawn anywhere regardless of light
    }
}