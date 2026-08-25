package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityHerobrine extends EntityMob {

    private int spawnBehaviorType = -1; // 0, 1, 2, or 3
    private int actionTimer = 0;
    private boolean hasInitializedBehavior = false;
    private boolean isChasing = false;

    public EntityHerobrine(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D); // Fast chase speed
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D); // Handled manually to leave half a heart
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false; // Invulnerable to direct damage
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.worldObj.isRemote) {
            if (!hasInitializedBehavior) {
                hasInitializedBehavior = true;
                this.spawnBehaviorType = this.rand.nextInt(4); // Pick one of the 4 actions randomly on spawn
                this.initializeSpawnBehavior();
            }

            // Execute behavior updates per type
            switch (spawnBehaviorType) {
                case 0:
                    this.updateBehaviorType0();
                    break;
                case 1:
                    this.updateBehaviorType1();
                    break;
                case 2:
                    // Type 2 executes fully on spawn, just despawn after a brief moment if needed
                    actionTimer++;
                    if (actionTimer > 20) {
                        this.setDead();
                    }
                    break;
                case 3:
                    this.updateBehaviorType3();
                    break;
            }
        }
    }

    private void initializeSpawnBehavior() {
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 64.0D);

        switch (spawnBehaviorType) {
            case 0: // TP near player, look at him, disappear after 120 seconds (2400 ticks)
                if (player != null) {
                    this.setPosition(player.posX + (this.rand.nextDouble() - 0.5D) * 10.0D, player.posY, player.posZ + (this.rand.nextDouble() - 0.5D) * 10.0D);
                }
                break;

            case 1: // Spawn 6 blocks in front of player, hit with lightning, despawn (Triggered on proximity/gaze or immediately set up)
                if (player != null) {
                    Vec3 lookVec = player.getLookVec();
                    this.setPosition(player.posX + (lookVec.xCoord * 6.0D), player.posY, player.posZ + (lookVec.zCoord * 6.0D));
                    this.faceEntity(player, 360.0F, 360.0F);
                    this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));
                    this.setDead();
                }
                break;

            case 2: // Remove all leaves nearby in a 6x6 chunk area (~96x96 blocks around spawn)
                int radius = 48; // 6 chunks roughly
                BlockPos centerPos = new BlockPos(this);
                for (int x = -radius; x <= radius; x++) {
                    for (int y = -20; y <= 30; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            BlockPos targetPos = centerPos.add(x, y, z);
                            Block block = this.worldObj.getBlockState(targetPos).getBlock();
                            if (block == Blocks.leaves || block == Blocks.leaves2) {
                                this.worldObj.setBlockToAir(targetPos);
                            }
                        }
                    }
                }
                break;

            case 3: // Apply slowness & blindness, remove effects, spawn 10 blocks from player front (unobstructed), chase down, leave at half heart, play cave sound, despawn
                if (player != null) {
                    player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 2));
                    player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));

                    // Find unobstructed spot ~10 blocks in front
                    Vec3 lookVec = player.getLookVec();
                    double spawnX = player.posX + (lookVec.xCoord * 10.0D);
                    double spawnZ = player.posZ + (lookVec.zCoord * 10.0D);
                    int spawnY = this.worldObj.getTopSolidOrLiquidBlock(new BlockPos(spawnX, 0, spawnZ)).getY();
                    
                    this.setPosition(spawnX, spawnY, spawnZ);
                    this.faceEntity(player, 360.0F, 360.0F);

                    // Add attack task for chase
                    this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.3D, true));
                    this.isChasing = true;
                }
                break;
        }
    }

    private void updateBehaviorType0() {
        actionTimer++;
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
        if (player != null) {
            this.faceEntity(player, 360.0F, 360.0F);
        }

        // Disappear after 120 seconds (2400 ticks) or if player looks directly at him/gets within 20 blocks
        if (actionTimer >= 2400 || (player != null && (this.getDistanceSqToEntity(player) <= 400.0D || this.isPlayerLookingAtMe(player)))) {
            this.setDead();
        }
    }

    private void updateBehaviorType1() {
        // Handled entirely on initialization / proximity trigger
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 20.0D);
        if (player != null || this.isPlayerLookingAtMe(player)) {
            this.worldObj.addWeatherEffect(newEntityLightningBolt(this.worldObj, this.posX, this.posY, this.posZ));
            this.setDead();
        }
    }

    private void updateBehaviorType3() {
        actionTimer++;
        // Remove effects after 5 seconds (100 ticks) and start active chase
        if (actionTimer == 100) {
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 64.0D);
            if (player != null) {
                player.removePotionEffect(Potion.moveSlowdown.id);
                player.removePotionEffect(Potion.blindness.id);
            }
        }

        // Check if Herobrine reaches/hits the player during chase
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 2.0D);
        if (player != null && isChasing) {
            // Set player health to 1.0 (half a heart out of 20 max health)
            player.setHealth(1.0F);

            // Play random cave sound at player's location
            this.worldObj.playSoundAtEntity(player, "ambient.cave.cave", 1.0F, 1.0F);

            // Despawn immediately
            this.setDead();
        }
    }

    private boolean isPlayerLookingAtMe(EntityPlayer player) {
        Vec3 lookVec = player.getLookVec().normalize();
        Vec3 entityVec = new Vec3(this.posX - player.posX, (this.posY + (double)this.getEyeHeight()) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
        double entityDist = entityVec.lengthVector();
        entityVec = entityVec.normalize();
        double dotProduct = lookVec.dotProduct(entityVec);
        return dotProduct > 0.85D && entityDist < 20.0D;
    }

    private EntityLightningBolt newEntityLightningBolt(World world, double x, double y, double z) {
        return new EntityLightningBolt(world, x, y, z);
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
}