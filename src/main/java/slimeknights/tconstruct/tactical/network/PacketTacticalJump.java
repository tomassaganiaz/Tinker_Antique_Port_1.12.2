package slimeknights.tconstruct.tactical.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import slimeknights.tconstruct.tactical.events.TacticalScoutArmorEvents;

public class PacketTacticalJump implements IMessage {
    public PacketTacticalJump(){}
    @Override public void fromBytes(ByteBuf buf){}
    @Override public void toBytes(ByteBuf buf){}
    public static class Handler implements IMessageHandler<PacketTacticalJump, IMessage> {
        @Override public IMessage onMessage(PacketTacticalJump m, MessageContext ctx){
            EntityPlayerMP p=ctx.getServerHandler().player;
            p.getServerWorld().addScheduledTask(() -> TacticalScoutArmorEvents.handleJumpStatic(p));
            return null;
        }
    }
}
