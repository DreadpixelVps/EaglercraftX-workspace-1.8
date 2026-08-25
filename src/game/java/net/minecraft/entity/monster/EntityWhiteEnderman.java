package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityWhiteEnderman extends EntityEnderman {

    public EntityWhiteEnderman(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(10.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        // Completely immune to water and drowning
        if (source == DamageSource.drown || source.damageType.equals("drown") || source.damageType.equals("water")) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    // Custom tracking check without line-of-sight requirement (no @Override needed)
    public boolean shouldAttackPlayerCustom(EntityPlayer player) {
        double d0 = this.getDistanceSq(player.posX, player.getEntityBoundingBox().minY, player.posZ);
        return d0 < 64.0D * 64.0D;
    }

    @Override
    protected Item getDropItem() {
        return Items.ender_pearl;
    }

    @Override
    public boolean attackEntityAsMob(net.minecraft.entity.Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        
        // Kick player with custom error message upon death by this mob
        if (flag && entityIn instanceof EntityPlayer && !this.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.getHealth() <= 0.0F) {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler
                        .kickPlayerFromServer("Error Enderman Consciousness?");
                }
            }
        }
        return flag;
    }
}