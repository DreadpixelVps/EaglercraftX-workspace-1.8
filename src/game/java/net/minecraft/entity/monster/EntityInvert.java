package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInvert extends EntityMob {
    private int lifetimeTimer = 0;
    private static final int MAX_LIFETIME = 2400; // 120 seconds (20 ticks per second)
    private boolean isTriggered = false;
    public boolean noMove = true;
    public EntityInvert(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        
        // Prevent movement completely by disabling AI movement capabilities
        this.noMove = true;
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D); // Zero movement speed
    }

    // Disable standard AI movement updates to ensure it stays completely still
    @Override
    public void moveEntity(double x, double y, double z) {
        // Do nothing to lock position in place
    }

    @Override
    public void setMoveForward(float p_70606_1_) {
        super.setMoveForward(0.0F);
    }

    @Override
    public void setJumping(boolean p_70637_1_) {
        // Cannot jump
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false; // Immune to damage or player attacks
    }

    @Override
    public void onLivingUpdate() {
        // Do not call super.onLivingUpdate() to prevent standard mob walking/jumping logic
        this.updateEntityActionState();

        if (!this.worldObj.isRemote) {
            lifetimeTimer++;

            // Despawn after 120 seconds (2400 ticks)
            if (lifetimeTimer >= MAX_LIFETIME) {
                this.setDead();
                return;
            }

            // Check for players within 10 blocks
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10.0D);
            if (player != null) {
                if (!isTriggered) {
                    isTriggered = true;

                    // Set world time to night (18000 ticks is midnight)
                    long worldTime = this.worldObj.getWorldTime();
                    long currentDayTime = worldTime % 24000L;
                    this.worldObj.setWorldTime(worldTime - currentDayTime + 18000L);

                    // Strike the player with lightning
                    this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, player.posX, player.posY, player.posZ));
                }

                // Look directly at the player
                this.faceEntity(player, 360.0F, 360.0F);
            }
        }
    }

    // Data watcher index for tracking texture state (0 = sleepinvert, 1 = invert)
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(20, Byte.valueOf((byte) 0));
    }

    public boolean isAggressiveTexture() {
        return this.dataWatcher.getWatchableObjectByte(20) == 1;
    }

    public void setAggressiveTexture(boolean aggressive) {
        this.dataWatcher.updateObject(20, Byte.valueOf((byte) (aggressive ? 1 : 0)));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote) {
            // Update texture state based on whether a player is within 10 blocks
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10.0D);
            this.setAggressiveTexture(player != null);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("LifetimeTimer", this.lifetimeTimer);
        tagCompound.setBoolean("IsTriggered", this.isTriggered);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        this.lifetimeTimer = tagCompound.getInteger("LifetimeTimer");
        this.isTriggered = tagCompound.getBoolean("IsTriggered");
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
}