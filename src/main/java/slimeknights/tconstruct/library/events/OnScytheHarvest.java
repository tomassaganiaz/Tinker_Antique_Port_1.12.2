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


public class OnScytheHarvest extends TinkerToolEvent {
  public final BlockPos pos;
  public final EntityPlayer player;
  public final IBlockState blockState;
  public final World world;
  public final boolean harvestable;

  public OnScytheHarvest(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, IBlockState blockState, boolean harvestable) {
    super(itemStack);
    this.pos = pos;
    this.player = player;
    this.world = world;
    this.blockState = blockState;
    this.harvestable = harvestable;
  }

  public static OnScytheHarvest fireEvent(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, IBlockState blockState, boolean harvestable) {
    OnScytheHarvest event = new OnScytheHarvest(itemStack, player, world, pos, blockState, harvestable);
    MinecraftForge.EVENT_BUS.post(event);
    return event;
  }
}
