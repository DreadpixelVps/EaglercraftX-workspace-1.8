package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntitySmiley;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class EntityAISmileyBehavior extends EntityAIBase {
    private final EntitySmiley theEntity;
    private EntityPlayer targetPlayer;
    private int state;
    private int stateTimer;
    private double fixedX;
    private double fixedY;
    private double fixedZ;
    private int chosenSpeechIndex = 0;

    private final String[] scareLines = new String[] {
        "Smiley: Boo.",
        "Smiley: Gotcha.",
        "Smiley: Right behind you...",
        "Smiley: Peek-a-boo.",
        "Smiley: Found you."
    };

    private final String[][] speechVariations = new String[][] {
        // Variation 0
        {
            "Smiley: Look at you... frozen in place. Completely helpless.",
            "Smiley: You've spent your whole life thinking you actually matter.",
            "Smiley: But your existence is completely hollow, empty, and useless.",
            "Smiley: Goodbye, little mistake."
        },
        // Variation 1
        {
            "Smiley: Did you really think you could run away from me?",
            "Smiley: Every step you took was just dragging you deeper into my trap.",
            "Smiley: Nobody is coming to save you. You are entirely alone.",
            "Smiley: Rest in pieces."
        },
        // Variation 2
        {
            "Smiley: Shh... do you hear that? That's the sound of your time running out.",
            "Smiley: All your efforts, all your builds, completely meaningless.",
            "Smiley: You are just a fleeting glitch in a world that doesn't care.",
            "Smiley: Time to wake up from the dream."
        },
        // Variation 3
        {
            "Smiley: Look into my eyes. What do you see? Just a mirror of your own failure.",
            "Smiley: You struggle so hard, yet achieve absolutely nothing of value.",
            "Smiley: It's almost sad watching you pretend you have a purpose.",
            "Smiley: Let me put you out of your misery."
        },
        // Variation 4
        {
            "Smiley: Checkmate. You trapped yourself right where I wanted you.",
            "Smiley: Your entire journey here was a pathetic waste of breath.",
            "Smiley: Nobody will remember you when you're gone. Not a single soul.",
            "Smiley: Farewell, nobody."
        },
        // Variation 5
        {
            "Smiley: Why are you shaking? Are you finally realizing how weak you are?",
            "Smiley: A fragile mind trapped inside a fragile little body.",
            "Smiley: You crumble under the slightest pressure. It's embarrassing.",
            "Smiley: Poof. Just like that, you're erased."
        },
        // Variation 6
        {
            "Smiley: I've been watching you stumble around for a long time.",
            "Smiley: Every single choice you've made has led to this exact failure.",
            "Smiley: You are a masterpiece of incompetence.",
            "Smiley: Game over for you."
        },
        // Variation 7
        {
            "Smiley: Breathe in that cold air while you still can.",
            "Smiley: Tomorrow, the world will move on as if you never even existed.",
            "Smiley: Because deep down, you never truly mattered anyway.",
            "Smiley: Fade away into nothingness."
        },
        // Variation 8
        {
            "Smiley: Look how helpless you look standing right in front of me.",
            "Smiley: All that confidence vanished the second I showed my face.",
            "Smiley: You were always just playing pretend, weren't you?",
            "Smiley: Time to face reality."
        },
        // Variation 9
        {
            "Smiley: End of the line, player. No more respawns for your spirit.",
            "Smiley: You fought against the inevitable, and look where it got you.",
            "Smiley: Completely paralyzed, defeated, and utterly forgotten.",
            "Smiley: Sweet dreams."
        }
    };

    public EntityAISmileyBehavior(EntitySmiley smiley) {
        this.theEntity = smiley;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        return true;
    }

    public void startExecuting() {
        this.targetPlayer = null;
        this.state = 0;
        this.stateTimer = 0;
        this.chosenSpeechIndex = this.theEntity.getRNG().nextInt(this.speechVariations.length);
        this.theEntity.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
    }

    public boolean continueExecuting() {
        return !this.theEntity.isDead;
    }

    public void updateTask() {
        if (this.targetPlayer == null || !this.targetPlayer.isEntityAlive()) {
            this.targetPlayer = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, 64.0D);
        }

        if (this.targetPlayer == null) {
            return;
        }

        this.theEntity.getLookHelper().setLookPositionWithEntity(this.targetPlayer, 30.0F, 30.0F);

        if (this.state == 0) {
            double distance = 20.0D;
            double angle = this.theEntity.getRNG().nextDouble() * Math.PI * 2.0D;
            double targetX = this.targetPlayer.posX + Math.cos(angle) * distance;
            double targetZ = this.targetPlayer.posZ + Math.sin(angle) * distance;
            double targetY = this.targetPlayer.posY;

            int attempts = 0;
            while (attempts < 10) {
                BlockPos pos = new BlockPos(targetX, targetY, targetZ);
                if (this.theEntity.worldObj.isAirBlock(pos) && this.theEntity.worldObj.isAirBlock(pos.up())) {
                    break;
                }
                distance -= 1.0D;
                targetX = this.targetPlayer.posX + Math.cos(angle) * Math.max(distance, 3.0D);
                targetZ = this.targetPlayer.posZ + Math.sin(angle) * Math.max(distance, 3.0D);
                attempts++;
            }

            this.theEntity.setPositionAndUpdate(targetX, targetY, targetZ);

            double actualDist = this.theEntity.getDistanceSqToEntity(this.targetPlayer);
            if (actualDist > 250.0D) {
                this.state = 1; 
            } else {
                this.state = 2; 
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 300, 255, true, false));
                    
                    // Calculate exact coordinates 2.5 blocks in front of player based on their yaw rotation
                    float playerYaw = this.targetPlayer.rotationYaw;
                    double rad = Math.toRadians(playerYaw);
                    
                    // In Minecraft, sin/cos offsets for looking direction
                    double spawnX = this.targetPlayer.posX - (Math.sin(rad) * 2.5D);
                    double spawnZ = this.targetPlayer.posZ + (Math.cos(rad) * 2.5D);
                    double spawnY = this.targetPlayer.posY;

                    this.fixedX = spawnX;
                    this.fixedY = spawnY;
                    this.fixedZ = spawnZ;
                    
                    this.theEntity.setPositionAndUpdate(this.fixedX, this.fixedY, this.fixedZ);
                    
                    // Force Smiley to face directly back at the player
                    this.theEntity.setLocationAndAngles(this.fixedX, this.fixedY, this.fixedZ, playerYaw + 180.0F, 0.0F);
                }
            }
            this.stateTimer = 0;
        } else if (this.state == 1) {
            this.theEntity.getNavigator().tryMoveToEntityLiving(this.targetPlayer, 1.3D);

            if (this.theEntity.getDistanceSqToEntity(this.targetPlayer) <= 4.0D) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), 1.0F);
                    int randomLineIndex = this.theEntity.getRNG().nextInt(this.scareLines.length);
                    this.targetPlayer.addChatMessage(new ChatComponentText("§f" + this.scareLines[randomLineIndex]));
                }
                this.theEntity.setDead();
            }
        } else if (this.state == 2) {
            this.stateTimer++;

            this.theEntity.setPositionAndUpdate(this.fixedX, this.fixedY, this.fixedZ);
            this.theEntity.motionX = 0.0D;
            this.theEntity.motionY = 0.0D;
            this.theEntity.motionZ = 0.0D;

            String[] currentSpeech = this.speechVariations[this.chosenSpeechIndex];

            if (this.stateTimer == 20) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.addChatMessage(new ChatComponentText("§f" + currentSpeech[0]));
                }
            } else if (this.stateTimer == 80) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.addChatMessage(new ChatComponentText("§f" + currentSpeech[1]));
                }
            } else if (this.stateTimer == 140) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.addChatMessage(new ChatComponentText("§f" + currentSpeech[2]));
                }
            } else if (this.stateTimer == 200) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.addChatMessage(new ChatComponentText("§f" + currentSpeech[3]));
                }
            } else if (this.stateTimer == 220) {
                if (!this.theEntity.worldObj.isRemote) {
                    this.targetPlayer.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), 1000.0F);
                }
            } else if (this.stateTimer >= 230) {
                this.theEntity.setDead();
            }
        }
    }

    public void resetTask() {
        this.targetPlayer = null;
    }
}