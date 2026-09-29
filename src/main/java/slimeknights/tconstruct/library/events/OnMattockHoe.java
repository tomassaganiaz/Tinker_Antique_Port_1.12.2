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


public class OnMattockHoe extends TinkerToolEvent {

  public final BlockPos pos;
  public final World world;
  public final EntityPlayer player;

  public OnMattockHoe(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos) {
    super(itemStack);
    this.player = player;
    this.pos = pos;
    this.world = world;
  }

  public static void fireEvent(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos) {
    MinecraftForge.EVENT_BUS.post(new OnMattockHoe(itemStack, player, world, pos));
  }
}
