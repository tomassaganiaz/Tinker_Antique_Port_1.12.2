package slimeknights.tconstruct.foundry.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import slimeknights.tconstruct.foundry.tileentity.TileScorchedFoundry;
import slimeknights.tconstruct.smeltery.block.BlockSmelteryController;

/** Controller of the scorched foundry multiblock. */
public class BlockScorchedController extends BlockSmelteryController {

  public BlockScorchedController() {
    super();
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileScorchedFoundry();
  }
}