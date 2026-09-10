package slimeknights.tconstruct.foundry.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import slimeknights.tconstruct.smeltery.block.BlockTank;
import slimeknights.tconstruct.smeltery.tileentity.TileTank;

/** Scorched tank used to hold the foundry fuel (blazing blood) and store molten metals. */
public class BlockScorchedTank extends BlockTank {

  public BlockScorchedTank() {
    super();
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileTank();
  }
}
