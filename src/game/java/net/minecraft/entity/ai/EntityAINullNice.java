package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

public class EntityAINullNice extends EntityAIBase {
    private final EntityNull entityNull;
    private EntityPlayer targetPlayer;
    private boolean sequenceStarted;
    private int floatTicks;
    private boolean hasDroppedItems;

    public EntityAINullNice(EntityNull entityNull) {
        this.entityNull = entityNull;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        this.targetPlayer = this.entityNull.worldObj.getClosestPlayerToEntity(this.entityNull, 16.0D);
        return this.targetPlayer != null && !this.targetPlayer.isDead;
    }

    @Override
    public void startExecuting() {
        this.sequenceStarted = false;
        this.floatTicks = 0;
        this.hasDroppedItems = false;
        
        if (this.targetPlayer != null) {
            Vec3 lookVec = this.targetPlayer.getLookVec();
            double spawnX = this.targetPlayer.posX + lookVec.xCoord * 3.0D;
            double spawnZ = this.targetPlayer.posZ + lookVec.zCoord * 3.0D;
            double spawnY = this.targetPlayer.posY;
            
            BlockPos pos = new BlockPos(spawnX, spawnY, spawnZ);
            while (spawnY > 0 && this.entityNull.worldObj.isAirBlock(pos.down())) {
                spawnY -= 1.0D;
                pos = new BlockPos(spawnX, spawnY, spawnZ);
            }
            
            this.entityNull.setPositionAndUpdate(spawnX, spawnY, spawnZ);
            
            this.entityNull.setCurrentItemOrArmor(0, new ItemStack(Items.iron_pickaxe));

            if (this.targetPlayer instanceof net.minecraft.entity.player.EntityPlayerMP) {
                this.targetPlayer.addChatMessage(new ChatComponentText("Null: Take care"));
            }
        }
    }

    @Override
    public void updateTask() {
        if (this.targetPlayer == null || this.targetPlayer.isDead) return;

        this.entityNull.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
        this.entityNull.getLookHelper().onUpdateLook();

        double distanceSq = this.entityNull.getDistanceSqToEntity(this.targetPlayer);
        if (distanceSq <= 16.0D && !this.sequenceStarted) {
            this.sequenceStarted = true;
        }

        if (this.sequenceStarted) {
            if (!this.hasDroppedItems) {
                this.hasDroppedItems = true;
                int count = 3 + (int)(Math.random() * 3);
                ItemStack ironIngots = new ItemStack(Items.iron_ingot, count);
                EntityItem entityItem = new EntityItem(this.entityNull.worldObj, this.entityNull.posX, this.entityNull.posY + 0.5D, this.entityNull.posZ, ironIngots);
                this.entityNull.worldObj.spawnEntityInWorld(entityItem);
            }

            this.floatTicks++;
            if (this.floatTicks <= 100) {
                this.entityNull.motionY = 0.05D;
                this.entityNull.isAirBorne = true;
            } else {
                this.entityNull.setDead();
            }
        }
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityNull.isDead && this.floatTicks <= 100;
    }
}