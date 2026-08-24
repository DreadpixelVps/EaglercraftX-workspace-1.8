package net.minecraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISmileyBehavior;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntitySmiley extends EntityMob {

    public EntitySmiley(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAISmileyBehavior(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
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
        this.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
        return super.onInitialSpawn(difficulty, livingData);
    }
}