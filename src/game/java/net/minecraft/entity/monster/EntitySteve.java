package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySteve extends EntityMob {

    public EntitySteve(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        
        // AI Tasks for hunting animals and wandering
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.25D, true));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        
        // Target selectors to hunt sheep, cows, and pigs
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntitySheep.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCow.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPig.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        // Fully killable with standard health and damage
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote) {
            // Check for players within 15 blocks
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 15.0D);
            if (player != null) {
                // Apply 10 seconds of blindness (200 ticks)
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, 0));
                // Despawn immediately
                this.setDead();
                return;
            }

            // Periodically break logs near him
            this.breakNearbyLogs();
        }
    }

    private void breakNearbyLogs() {
        int range = 2;
        int minX = (int)Math.floor(this.posX) - range;
        int maxX = (int)Math.floor(this.posX) + range;
        int minY = (int)Math.floor(this.posY);
        int maxY = (int)Math.floor(this.posY) + 2;
        int minZ = (int)Math.floor(this.posZ) - range;
        int maxZ = (int)Math.floor(this.posZ) + range;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = this.worldObj.getBlockState(pos).getBlock();

                    // Check if block is wood/log
                    if (block == Blocks.log || block == Blocks.log2) {
                        this.worldObj.destroyBlock(pos, true);
                    }
                }
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true; // Can spawn anywhere regardless of light
    }
}