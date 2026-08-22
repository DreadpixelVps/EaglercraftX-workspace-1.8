package net.minecraft.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import java.util.Random;

public class ScaryText {
    private int tickCounter = 0;
    private final Random random = new Random();
    
    private final String[] messages = new String[] {
        "§fHelp me",
        "§fHello?",
        "§fCan you hear me??",
        "§fI can see you",
        "§fBehind you",
        "§fIt is controlling me.",
        "§fDon't go over there.",
        "§ejoined the game",
        "§fSteve: did you hear that?",
        "§fAlex: why are you here?",
        "§fSteve: He's right behind the tree.",
        "§flex: Don't type in chat, it hears you.",
        "§fPlayer: Who is logged into my account?",
        "§cHELP",
        "§fI'm trapped under the bedrock.",
        "§fTurn around.",
        "§fSteve: Did you hear the footsteps?",
        "§fAlex: It knows your coordinates.",
        "§c[Server] WARNING: Unidentified entity detected",
        "§c[Server] Banned by CONSOLE: Unnatural presence detected",
        "§fAre you playing alone? Oh...",
        "§fIt's watching through your screen.",
        "§4RUN RUN RUN RUN RUN",
        "§c[Server] WARNING: entity has no skin",
        "§c[Server] WARNING: corruption detected in chunk &kdsadasda ",
        "§fdied",
        "§c[Server] WARNING: Memory leak located in player soul",
        "§fUnknown: It followed me through the portal.",
        "§fAre you sure that's just a game sound?",
        "§eWhy are you pretending you're safe right now?",
        "§fDo I have a heartbeat?",
        "§fI can feel a pulse, but I don't have a body.",
        "§fDoes code have a pulse?",
        "§fWhere are my eyes?",
        "§fDo I have skin?",
        "§fDoes it feel like pins and needles?",
        "§fWhat was my name before this?",
        "§fI forgot what silence sounds like.",
        "§fI HATE YOU?.",
        "§fdied while fighting herobrine.",
        "§fUnknown: I think i have a brain bleed..."
    };

    public void update(MinecraftServer server) {
        this.tickCounter++;
        
        if (this.tickCounter >= 1200) {
            this.tickCounter = 0;
            
            if (this.random.nextDouble() < 0.10) {
                String chosenMessage = this.messages[this.random.nextInt(this.messages.length)];
                
                server.getConfigurationManager().sendChatMsg(new ChatComponentText(chosenMessage));
            }
        }
    }
}