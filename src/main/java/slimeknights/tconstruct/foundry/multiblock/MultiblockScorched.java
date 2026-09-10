package slimeknights.tconstruct.foundry.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import slimeknights.tconstruct.foundry.TinkerScorched;
import slimeknights.tconstruct.smeltery.multiblock.MultiblockSmeltery;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;

/** Foundry multiblock: same size requirements as the smeltery, built from scorched blocks. */
public class MultiblockScorched extends MultiblockSmeltery {

  public MultiblockScorched(TileSmeltery smeltery) {
    super(smeltery);
  }

  @Override
  public boolean isValidBlock(World world, BlockPos pos) {
    if(pos.equals(tile.getPos())) {
      return true;
    }

    if(!isValidSlave(world, pos)) {
      return false;
    }

    IBlockState state = world.getBlockState(pos);

    // we need a scorched tank
    if(state.getBlock() == TinkerScorched.scorchedTank) {
      hasTank = true;
      return true;
    }

    return TinkerScorched.validScorchedBlocks.contains(state.getBlock());
  }

  @Override
  public boolean isFloorBlock(World world, BlockPos pos) {
    // only scorched blocks for the floor
    return world.getBlockState(pos).getBlock() == TinkerScorched.blockScorched && isValidBlock(world, pos);
  }
}