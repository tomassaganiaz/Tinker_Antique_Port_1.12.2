package slimeknights.tconstruct.tactical.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.Util;

public class TacticalNetwork {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("tactical");
    private static int id=0;
    public static void register(){
        INSTANCE.registerMessage(PacketTacticalJump.Handler.class, PacketTacticalJump.class, id++, Side.SERVER);
    }
}
