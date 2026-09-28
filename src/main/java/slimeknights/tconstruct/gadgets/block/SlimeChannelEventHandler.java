package slimeknights.tconstruct.gadgets.block;  
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public final class SlimeChannelEventHandler {

  public static final SlimeChannelEventHandler instance = new SlimeChannelEventHandler();

  private SlimeChannelEventHandler() {
  }

  // stop items from despawning when inside channels
  // this won't give them a full 5 minutes upon exiting, only upon attempting to despawn
  @SubscribeEvent
  public void onItemExpire(ItemExpireEvent event) {
    EntityItem item = event.getEntityItem();
    if(item.getEntityWorld().getBlockState(item.getPosition()).getBlock() instanceof BlockSlimeChannel) {
      event.setCanceled(true);
    }
  }
}
