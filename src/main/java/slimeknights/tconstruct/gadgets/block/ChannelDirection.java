package slimeknights.tconstruct.gadgets.block;  
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.ImmutableList;


public enum ChannelDirection implements IStringSerializable {
  SOUTH,
  SOUTHWEST,
  WEST,
  NORTHWEST,
  NORTH,
  NORTHEAST,
  EAST,
  SOUTHEAST;

  public final int index;

  ChannelDirection() {
    this.index = this.ordinal();
  }

  @Override
  public String getName() {
    return this.toString().toLowerCase(Locale.US);
  }

  /**
   * @return an integer representing this value, used for the sake of saving this to the TE
   */
  public int getIndex() {
    return index;
  }

  /**
   * @return the value corresponding to the integer given, used for loading from the TE
   */
  public static ChannelDirection fromIndex(int index) {
    if(index < 0 || index >= values().length) {
      index = 0;
    }

    return values()[index];
  }

  /**
   * @return the opposite direction for the current side
   */
  public ChannelDirection getOpposite() {
    switch(this) {
      case SOUTH:
        return NORTH;
      case SOUTHWEST:
        return NORTHEAST;
      case WEST:
        return EAST;
      case NORTHWEST:
        return SOUTHEAST;
      case NORTH:
        return SOUTH;
      case NORTHEAST:
        return SOUTHWEST;
      case EAST:
        return WEST;
      case SOUTHEAST:
        return NORTHWEST;
    }
    // not possible, but here because eclipse wants it
    return null;
  }

  /**
   * @return the opposite direction for the current side
   */
  public ChannelDirection rotate90() {
    switch(this) {
      case SOUTH:
        return WEST;
      case SOUTHWEST:
        return NORTHWEST;
      case WEST:
        return NORTH;
      case NORTHWEST:
        return NORTHEAST;
      case NORTH:
        return EAST;
      case NORTHEAST:
        return SOUTHEAST;
      case EAST:
        return SOUTH;
      case SOUTHEAST:
        return SOUTHWEST;
      default:
        throw new IllegalArgumentException("Unknown enum value? Impossibru!");
    }
  }

  /**
   * Gets the EnumFacing value with the same name as one of this Enum
   */
  public EnumFacing getFacing() {
    switch(this) {
      case NORTH:
        return EnumFacing.NORTH;
      case SOUTH:
        return EnumFacing.SOUTH;
      case WEST:
        return EnumFacing.WEST;
      case EAST:
        return EnumFacing.EAST;
    }
    return null;
  }

  /**
   * Gets the EnumFacing value with the same name as one of this Enum
   */
  public ChannelDirection fromFacing(EnumFacing facing) {
    switch(facing) {
      case NORTH:
        return NORTH;
      case SOUTH:
        return SOUTH;
      case WEST:
        return WEST;
      case EAST:
        return EAST;
    }
    return null;
  }

  /**
   * Returns the direction of flow for the given side
   * <br>
   * If the side is a diagonal, it returns null, use getDiagonal below for the two relevant directions
   */
  @Nullable
  public EnumFacing getFlow(EnumFacing side) {
    switch(side) {
      case NORTH:
        switch(this) {
          case NORTH:
            return EnumFacing.UP;
          case SOUTH:
            return EnumFacing.DOWN;
          case WEST:
            return EnumFacing.WEST;
          case EAST:
            return EnumFacing.EAST;
        }
      case SOUTH:
        switch(this) {
          case NORTH:
            return EnumFacing.UP;
          case SOUTH:
            return EnumFacing.DOWN;
          case WEST:
            return EnumFacing.EAST;
          case EAST:
            return EnumFacing.WEST;
        }
      case WEST:
        switch(this) {
          case NORTH:
            return EnumFacing.UP;
          case SOUTH:
            return EnumFacing.DOWN;
          case WEST:
            return EnumFacing.SOUTH;
          case EAST:
            return EnumFacing.NORTH;
        }
      case EAST:
        switch(this) {
          case NORTH:
            return EnumFacing.UP;
          case SOUTH:
            return EnumFacing.DOWN;
          case WEST:
            return EnumFacing.NORTH;
          case EAST:
            return EnumFacing.SOUTH;
        }
      default:
        // note that this returns null for diagonals
        return this.getFacing();
    }
  }

  /**
   * Returns a list of one or two directions for the sake of liquid flow
   */
  public List<EnumFacing> getFlowDiagonals(@Nonnull EnumFacing side) {
    switch(this) {
      case NORTH:
        return ImmutableList.of(NORTH.getFlow(side));
      case SOUTH:
        return ImmutableList.of(SOUTH.getFlow(side));
      case WEST:
        return ImmutableList.of(WEST.getFlow(side));
      case EAST:
        return ImmutableList.of(EAST.getFlow(side));
      case NORTHWEST:
        return ImmutableList.of(NORTH.getFlow(side), WEST.getFlow(side));
      case NORTHEAST:
        return ImmutableList.of(NORTH.getFlow(side), EAST.getFlow(side));
      case SOUTHWEST:
        return ImmutableList.of(SOUTH.getFlow(side), WEST.getFlow(side));
      case SOUTHEAST:
        return ImmutableList.of(SOUTH.getFlow(side), EAST.getFlow(side));
    }
    return ImmutableList.of();
  }
}
