package slimeknights.tconstruct.gadgets.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static slimeknights.tconstruct.gadgets.block.BlockSlimeChannel.*;

/**
 * Cálculo de las bounding boxes y caras de render del canal de slime.
 */
public final class SlimeChannelShapes {

  private SlimeChannelShapes() {
  }

  /* Bounds */
  // Block hitbox and main location for motion
  static final ImmutableMap<EnumFacing, AxisAlignedBB> BOUNDS;
  // "quarter slab" bounds on bottom used for location checks on connected blocks
  static final ImmutableMap<EnumFacing, AxisAlignedBB> LOWER_BOUNDS;
  // "quarter slab" bounds on side for sideways connected
  static final ImmutableMap<EnumFacing, AxisAlignedBB> SIDE_BOUNDS;
  // "quarter slab" bounds on top for bottom outer connected
  static final ImmutableMap<EnumFacing, AxisAlignedBB> UPPER_BOUNDS;

  static {
    ImmutableMap.Builder<EnumFacing, AxisAlignedBB> builder = ImmutableMap.builder();
    builder.put(EnumFacing.UP, new AxisAlignedBB(0, 0.5, 0, 1, 1, 1));
    builder.put(EnumFacing.DOWN, new AxisAlignedBB(0, 0, 0, 1, 0.5, 1));
    builder.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.5));
    builder.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.5, 1, 1, 1));
    builder.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.5, 1, 1));
    builder.put(EnumFacing.EAST, new AxisAlignedBB(0.5, 0, 0, 1, 1, 1));
    BOUNDS = builder.build();

    builder = ImmutableMap.builder();
    builder.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 0.5, 0.5));
    builder.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.5, 1, 0.5, 1));
    builder.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.5, 0.5, 1));
    builder.put(EnumFacing.EAST, new AxisAlignedBB(0.5, 0, 0, 1, 0.5, 1));
    LOWER_BOUNDS = builder.build();

    builder = ImmutableMap.builder();
    builder.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 0.5, 1, 0.5));
    builder.put(EnumFacing.SOUTH, new AxisAlignedBB(0.5, 0, 0.5, 1, 1, 1));
    builder.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0.5, 0.5, 1, 1));
    builder.put(EnumFacing.EAST, new AxisAlignedBB(0.5, 0, 0, 1, 1, 0.5));
    SIDE_BOUNDS = builder.build();

    builder = ImmutableMap.builder();
    builder.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0.5, 0, 1, 1, 0.5));
    builder.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0.5, 0.5, 1, 1, 1));
    builder.put(EnumFacing.WEST, new AxisAlignedBB(0, 0.5, 0, 0.5, 1, 1));
    builder.put(EnumFacing.EAST, new AxisAlignedBB(0.5, 0.5, 0, 1, 1, 1));
    UPPER_BOUNDS = builder.build();
  }

  /**
   * Returns the bounds for the current state
   * <br>
   * Makes sure you pas the actual state into this or it won't take connections into account
   */
  static AxisAlignedBB getBounds(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing side = state.getValue(SIDE);
    EnumFacing facing = state.getValue(DIRECTION).getFacing();
    ChannelConnected connected = state.getValue(CONNECTED);

    // diagonals return null above, and cannot have such connections anyways
    if(connected == ChannelConnected.INNER && facing != null) {
      if(side == EnumFacing.DOWN) {
        return LOWER_BOUNDS.get(facing);
      }
      else if(side == EnumFacing.UP) {
        return UPPER_BOUNDS.get(facing);
      }
      else {
        switch(facing) {
          case NORTH:
            return UPPER_BOUNDS.get(side);
          case SOUTH:
            return LOWER_BOUNDS.get(side);
          case WEST:
            return SIDE_BOUNDS.get(side);
          case EAST:
            return SIDE_BOUNDS.get(side.rotateY());
        }
      }
    }
    return BOUNDS.get(side);
  }

  static AxisAlignedBB getSecondaryBounds(IBlockState state) {
    EnumFacing side = state.getValue(SIDE);
    EnumFacing facing = state.getValue(DIRECTION).getFacing();

    // this just prevents a NPE in the case of an invalid state
    // as a block will never be connected and diagonal except in debug
    if(facing == null) {
      return Block.FULL_BLOCK_AABB;
    }

    if(side == EnumFacing.DOWN) {
      return UPPER_BOUNDS.get(facing.getOpposite());
    }
    else if(side == EnumFacing.UP) {
      return LOWER_BOUNDS.get(facing.getOpposite());
    }
    else {
      switch(facing) {
        case NORTH:
          return LOWER_BOUNDS.get(side.getOpposite());
        case SOUTH:
          return UPPER_BOUNDS.get(side.getOpposite());
        case WEST:
          return SIDE_BOUNDS.get(side.getOpposite());
        case EAST:
          return SIDE_BOUNDS.get(side.rotateYCCW());
        default:
          return Block.FULL_BLOCK_AABB;
      }
    }
  }

  static BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
    state = state.getActualState(world, pos);
    EnumFacing side = state.getValue(SIDE);
    if(hasFullSide(face.getOpposite(), side, state.getValue(DIRECTION).getFlow(side), state.getValue(CONNECTED))) {
      return BlockFaceShape.SOLID;
    }
    return BlockFaceShape.UNDEFINED;
  }

  static boolean hasFullSide(EnumFacing orginFace, EnumFacing side, EnumFacing flow, ChannelConnected connected) {
    // back, full unless we are the inner corner
    if(orginFace == side.getOpposite() && connected != ChannelConnected.INNER) {
      return true;
    }
    // back side face, full if we are connected
    return orginFace == flow && connected == ChannelConnected.OUTER;
  }

  static boolean hasHalfSide(EnumFacing orginHalf, EnumFacing orginFace, EnumFacing side, EnumFacing flow, ChannelConnected connected) {
    // if we are on the same side as the half face
    if(side == orginHalf) {
      // make sure inner connections face the same direction
      if(connected == ChannelConnected.INNER) {
        // the direction is opposite the face of the half
        return flow == orginFace.getOpposite();
      }
      // both outer and none have this face solid
      return true;
    }

    // pressed up against this, basically the same as above only switched half and direction
    if(side == orginFace.getOpposite()) {
      // inner connection must be on the same half as the direction its going
      if(connected == ChannelConnected.INNER) {
        return flow == orginHalf;
      }
      // both outer and none have this face solid
      return true;
    }

    // opposite side of the block
    if(side == orginFace) {
      // it must face the opposite of the half and be connected outer
      return connected == ChannelConnected.OUTER && flow == orginHalf.getOpposite();
    }

    // there are three remaining directions, but their only chance is if they have the outer chance
    if(connected == ChannelConnected.OUTER) {
      // if its facing away, it has a full face here
      if(flow == orginFace) {
        return true;
      }
      // if the channel is opposite the half, the only valid facing is the one handled above
      if(side == orginHalf.getOpposite()) {
        return false;
      }
      // otherwise there is an additional valid facing, going the opposite direction of the half leading to "stairs"
      return flow == orginHalf.getOpposite();
    }

    return false;
  }
}