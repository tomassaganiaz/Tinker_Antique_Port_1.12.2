package slimeknights.tconstruct.library.events; 
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ranged.BowCore;


public class OnRepair extends TinkerToolEvent {

  public final int amount;

  public OnRepair(ItemStack itemStack, int amount) {
    super(itemStack);
    this.amount = amount;
  }

  public static boolean fireEvent(ItemStack itemStack, int amount) {
    OnRepair event = new OnRepair(itemStack, amount);
    return !MinecraftForge.EVENT_BUS.post(event);
  }
}
