package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityNull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class EntityAINullMad extends EntityAIBase {
    private final EntityNull entityNull;
    private EntityPlayer targetPlayer;
    private int chatTimer;
    private int chatCount;
    private boolean sequenceStarted;

    public EntityAINullMad(EntityNull entityNull) {
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
        this.chatTimer = 0;
        this.chatCount = 0;
        this.sequenceStarted = false;
    }

    @Override
    public void updateTask() {
        if (this.targetPlayer == null || this.targetPlayer.isDead) return;

        this.entityNull.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);
        this.entityNull.getLookHelper().onUpdateLook();

        double distanceSq = this.entityNull.getDistanceSqToEntity(this.targetPlayer);
        if (distanceSq <= 16.0D && !this.sequenceStarted) {
            this.sequenceStarted = true;
            this.executeMadSequence();
        }

        if (this.sequenceStarted && this.chatCount < 10) {
            this.chatTimer++;
            if (this.chatTimer >= 20) {
                this.chatTimer = 0;
                this.chatCount++;
                if (this.targetPlayer instanceof EntityPlayerMP) {
                    this.targetPlayer.addChatMessage(new ChatComponentText("01010101010101010101010101010101"));
                }
                
                if (this.chatCount >= 10) {
                    this.safelyTeleportPlayerUp();
                    this.entityNull.setDead();
                }
            }
        }
    }

    private void executeMadSequence() {
        ItemStack binaryBook = new ItemStack(Items.written_book);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("title", "01010101");
        tag.setString("author", "EntityNull");
        
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("01010101010101010101010101010101"));
        tag.setTag("pages", pages);
        binaryBook.setTagCompound(tag);

        if (this.targetPlayer.inventory != null) {
            this.targetPlayer.inventory.addItemStackToInventory(binaryBook);
            this.targetPlayer.inventory.markDirty();
            if (this.targetPlayer instanceof EntityPlayerMP) {
                ((EntityPlayerMP) this.targetPlayer).sendContainerToPlayer(this.targetPlayer.inventoryContainer);
            }
        }
    }

    private void safelyTeleportPlayerUp() {
        double x = this.targetPlayer.posX;
        double y = this.targetPlayer.posY + 10.0D;
        double z = this.targetPlayer.posZ;

        BlockPos pos = new BlockPos(x, y + 1.0D, z);
        while (y < 255 && (!this.entityNull.worldObj.isAirBlock(pos) || !this.entityNull.worldObj.isAirBlock(pos.up()))) {
            y += 1.0D;
            pos = new BlockPos(x, y + 1.0D, z);
        }

        this.targetPlayer.setPositionAndUpdate(x, y, z);
        this.targetPlayer.fallDistance = 0.0F;
    }

    @Override
    public boolean continueExecuting() {
        return !this.entityNull.isDead && this.chatCount < 10;
    }
}