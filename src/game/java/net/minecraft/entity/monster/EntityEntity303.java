package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEntity303 extends EntityMob {

    private boolean hasExecutedAction = false;
    private int actionTimer = 0;
    
    // For above-Y50 lightning storm
    private int lightningCount = 0;
    private double spawnTargetX;
    private double spawnTargetY;
    private double spawnTargetZ;
    public boolean noMove = true;
    public EntityEntity303(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.noMove = true; // Floating/static behavior
        this.isImmuneToFire = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false; // Invulnerable
    }

    @Override
    public void onLivingUpdate() {
        // Prevent normal movement/ai walking updates
        this.updateEntityActionState();

        if (!this.worldObj.isRemote && !hasExecutedAction) {
            hasExecutedAction = true;
            
            // Record initial spawn position
            this.spawnTargetX = this.posX;
            this.spawnTargetY = this.posY;
            this.spawnTargetZ = this.posZ;

            if (this.posY >= 50.0D) {
                // Above Y 50: Float 10 blocks above spawn point
                this.setPosition(this.spawnTargetX, this.spawnTargetY + 10.0D, this.spawnTargetZ);
            } else {
                // Below Y 50: Underground behavior near player
                EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
                if (player != null) {
                    this.executeUndergroundEvent(player);
                } else {
                    this.setDead();
                }
            }
        }

        // Handle the continuous above-Y50 lightning storm over multiple ticks
        if (!this.worldObj.isRemote && this.spawnTargetY >= 50.0D) {
            actionTimer++;
            
            // Keep entity floating 10 blocks up
            this.setPosition(this.spawnTargetX, this.spawnTargetY + 10.0D, this.spawnTargetZ);
            
            // Look at closest player if available
            EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 64.0D);
            if (player != null) {
                this.faceEntity(player, 360.0F, 360.0F);
            }

            // Fire lightning bolts spaced out across ticks (e.g., 5 bolts every tick for 20 ticks = 100+ bolts)
            if (actionTimer <= 25 && lightningCount < 110) {
                for (int i = 0; i < 5; i++) {
                    double offsetX = (this.rand.nextDouble() - 0.5D) * 24.0D;
                    double offsetZ = (this.rand.nextDouble() - 0.5D) * 24.0D;
                    double targetX = this.posX + offsetX;
                    double targetZ = this.posZ + offsetZ;
                    int targetY = this.worldObj.getPrecipitationHeight(new BlockPos(targetX, 0, targetZ)).getY();

                    this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, targetX, targetY, targetZ));
                    lightningCount++;
                }
            } else if (actionTimer > 40) {
                // Despawn after storm finishes
                this.setDead();
            }
        }
    }

    private void executeUndergroundEvent(EntityPlayer player) {
        // 1. Remove all nearby torches within a 12-block radius
        int radius = 12;
        BlockPos playerPos = new BlockPos(player);
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos currentPos = playerPos.add(x, y, z);
                    Block block = this.worldObj.getBlockState(currentPos).getBlock();
                    
                    if (block == Blocks.torch || block == Blocks.unlit_redstone_torch || block == Blocks.redstone_torch) {
                        this.worldObj.setBlockToAir(currentPos);
                    }
                }
            }
        }

        // 2. Position Entity303 directly in front of the player's view
        Vec3 lookVec = player.getLookVec();
        double frontX = player.posX + (lookVec.xCoord * 5.0D);
        double frontY = player.posY;
        double frontZ = player.posZ + (lookVec.zCoord * 5.0D);
        this.setPosition(frontX, frontY, frontZ);
        this.faceEntity(player, 360.0F, 360.0F);

        // 3. Clear blocks obstructing the player's view so they can see him
        int clearRadiusX = (int)Math.abs(lookVec.xCoord * 5.0D);
        int clearRadiusZ = (int)Math.abs(lookVec.zCoord * 5.0D);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 2; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos obstructionPos = new BlockPos(player.posX + (lookVec.xCoord * (2.0 + dx)), player.posY + dy, player.posZ + (lookVec.zCoord * (2.0 + dz)));
                    if (this.worldObj.getBlockState(obstructionPos).getBlock() != Blocks.air) {
                        this.worldObj.setBlockToAir(obstructionPos);
                    }
                }
            }
        }

        // 4. Replace some nearby blocks 4-5 blocks away in front of the player with Bedrock
        for (int i = 4; i <= 5; i++) {
            double bedX = player.posX + (lookVec.xCoord * i);
            double bedY = player.posY;
            double bedZ = player.posZ + (lookVec.zCoord * i);
            
            BlockPos bedrockPos = new BlockPos(bedX, bedY, bedZ);
            this.worldObj.setBlockState(bedrockPos, Blocks.bedrock.getDefaultState());
            
            BlockPos bedrockPosUp = new BlockPos(bedX, bedY + 1.0D, bedZ);
            this.worldObj.setBlockState(bedrockPosUp, Blocks.bedrock.getDefaultState());
        }

        // 5. Instantly disappear/despawn after executing the event
        this.setDead();
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
}