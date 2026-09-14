package slimeknights.tconstruct.tactical.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.tactical.network.PacketTacticalJump;
import slimeknights.tconstruct.tactical.network.TacticalNetwork;

public class TacticalClientEvents {
    private boolean wasJump=false;
    @SubscribeEvent public void onTick(TickEvent.ClientTickEvent e){
        if(e.phase!=TickEvent.Phase.END) return;
        Minecraft mc=Minecraft.getMinecraft();
        if(mc.player==null || mc.world==null) return;
        KeyBinding jump=mc.gameSettings.keyBindJump;
        boolean isJump=jump.isKeyDown();
        if(isJump && !wasJump && !mc.player.onGround && !mc.player.capabilities.isFlying){
            TacticalNetwork.INSTANCE.sendToServer(new PacketTacticalJump());
        }
        wasJump=isJump;
    }
}
