package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGiantAlex extends EntityMob {
    public boolean noMove = true;
    public EntityGiantAlex(World worldIn) {
        super(worldIn);
        // Scaled up 5x to match ModelGiantAlex (Width 3.0 blocks, Height 9.0 blocks)
        this.setSize(3.0F, 9.0F);
        this.noMove = true; // Stationary giant presence
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false; // Invulnerable
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.updateEntityActionState();

        if (!this.worldObj.isRemote) {
            // Check for players within 10 blocks
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10.0D);
            if (player != null) {
                // Apply 10 seconds of blindness (200 ticks)
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, 0));

                // Teleport the user 15 blocks straight up (handling multiplayer server player packet/teleport correctly if EntityPlayerMP)
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;
                    playerMP.playerNetServerHandler.setPlayerLocation(
                        playerMP.posX, 
                        playerMP.posY + 15.0D, 
                        playerMP.posZ, 
                        playerMP.rotationYaw, 
                        playerMP.rotationPitch
                    );
                } else {
                    player.setPosition(player.posX, player.posY + 15.0D, player.posZ);
                }

                // Despawn immediately after triggering
                this.setDead();
            }
        }
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
}