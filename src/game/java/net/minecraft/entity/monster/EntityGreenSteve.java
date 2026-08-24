package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityGreenSteveNice;
import net.minecraft.entity.ai.EntityAIGreenSteveMad;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityGreenSteve extends EntityMob {

    public EntityGreenSteve(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 180.0F, 1.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(20, Byte.valueOf((byte) 0));
    }

    public void setBehaviorType(byte type) {
        this.getDataWatcher().updateObject(20, Byte.valueOf(type));
        
        this.tasks.removeTask(new EntityGreenSteveNice(this));
        this.tasks.removeTask(new EntityAIGreenSteveMad(this));

        if (type == 1) {
            this.tasks.addTask(1, new EntityGreenSteveNice(this));
        } else if (type == 2) {
            this.tasks.addTask(1, new EntityAIGreenSteveMad(this));
        }
    }

    public byte getBehaviorType() {
        return this.getDataWatcher().getWatchableObjectByte(20);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
    }

    public void onLivingUpdate() {
        this.setHealth(this.getMaxHealth());
        if (!this.worldObj.isRemote && this.getBehaviorType() == 0) {
            this.setBehaviorType((byte) (this.rand.nextInt(2) + 1));
        }
        
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
        if (player != null) {
            this.getLookHelper().setLookPositionWithEntity(player, 30.0F, 30.0F);
            this.getLookHelper().onUpdateLook();
        }

        super.onLivingUpdate();
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source == DamageSource.outOfWorld) {
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return null;
    }

    protected String getDeathSound() {
        return null;
    }

    protected void playStepSound(BlockPos var1, net.minecraft.block.Block var2) {
    }

    protected Item getDropItem() {
        return null;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("BehaviorType", this.getBehaviorType());
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        if (tagCompound.hasKey("BehaviorType")) {
            this.setBehaviorType(tagCompound.getByte("BehaviorType"));
        }
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        livingData = super.onInitialSpawn(difficulty, livingData);
        this.setBehaviorType((byte) (this.rand.nextInt(2) + 1));
        return livingData;
    }
}