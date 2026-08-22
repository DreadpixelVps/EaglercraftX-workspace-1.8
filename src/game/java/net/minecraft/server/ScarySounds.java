package net.minecraft.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;
import java.util.Random;

public class ScarySounds {
    private int tickCounter = 0;
    private final Random random = new Random();
    
    private final String[] scarySounds = new String[] {
        "custom.scary1",
        "custom.scary2",
        "custom.scary3",
        "custom.scary4",
        "custom.scary5",
        "custom.scary6",
        "custom.scary7",
        "custom.scary8",
        "custom.scary9",
        "custom.scary10",
        "custom.scary11",
        "custom.scary12",
        "custom.scary13",
        "custom.scary14"
    };

    public void update(MinecraftServer server) {
        this.tickCounter++;
        
        if (this.tickCounter >= 1200) {
            this.tickCounter = 0;
            
            if (this.random.nextDouble() < 0.10) {
                String chosenSound = this.scarySounds[this.random.nextInt(this.scarySounds.length)];
                
                for (WorldServer world : server.worldServers) {
                    if (world == null || world.playerEntities == null) continue;
                    
                    for (Object playerObj : world.playerEntities) {
                        if (playerObj instanceof EntityPlayerMP) {
                            EntityPlayerMP player = (EntityPlayerMP) playerObj;
                            
                            player.playerNetServerHandler.sendPacket(new S29PacketSoundEffect(
                                chosenSound, 
                                player.posX, player.posY, player.posZ, 
                                1.0F, 1.0F
                            ));
                            
                            if (chosenSound.equals("custom.scary9")) {
                                player.attackEntityFrom(DamageSource.generic, 2.0F);
                            }
                        }
                    }
                }
            }
        }
    }
}