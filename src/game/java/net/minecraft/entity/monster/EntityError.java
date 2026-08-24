package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIErrorBehavior;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityError extends EntityMob {

    public EntityError(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAIErrorBehavior(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
    }

    public void onLivingUpdate() {
        this.setHealth(this.getMaxHealth());
        
        if (this.posY < 60.0D) {
            this.setDead();
            return;
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

    protected boolean canDespawn() {
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

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        if (this.posY < 60.0D) {
            this.setDead();
        }
        return super.onInitialSpawn(difficulty, livingData);
    }
}