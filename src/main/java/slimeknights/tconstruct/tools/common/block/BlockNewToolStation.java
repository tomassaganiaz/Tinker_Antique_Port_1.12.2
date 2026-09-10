package slimeknights.tconstruct.tools.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
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
}
