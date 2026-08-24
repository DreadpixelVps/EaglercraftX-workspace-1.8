package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIRagsTeleport;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRags extends EntityMob {

    public EntityRags(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAIRagsTeleport(this));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 180.0F, 1.0F));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
    }

    public void onLivingUpdate() {
        this.setHealth(this.getMaxHealth());
        
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

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        return super.onInitialSpawn(difficulty, livingData);
    }
}