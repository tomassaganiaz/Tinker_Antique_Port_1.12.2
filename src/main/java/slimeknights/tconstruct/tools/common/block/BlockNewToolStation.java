package slimeknights.tconstruct.tools.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.tools.common.tileentity.TileNewToolStation;

public class BlockNewToolStation extends BlockToolTable {
  public BlockNewToolStation() {
    super();
    this.setCreativeTab(TinkerRegistry.tabTools);
  }
  @Override
  public String getUnlocalizedName() {
    return "tile.tconstruct.new_tool_station.name";
  }
  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new TileNewToolStation();
  }
  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
    // es una estacion unica: no heredar los subtipos de BlockToolTable (plank/log/etc.)
    list.add(new ItemStack(this, 1, 0));
  }
}
