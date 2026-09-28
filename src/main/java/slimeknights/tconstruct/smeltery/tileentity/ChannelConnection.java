package slimeknights.tconstruct.smeltery.tileentity;

import net.minecraft.util.IStringSerializable;
import java.util.Locale;

public enum ChannelConnection implements IStringSerializable {
  NONE,
  IN,
  OUT;

  byte index;
  ChannelConnection() {
    index = (byte)ordinal();
  }

  public byte getIndex() {
    return index;
  }

  public ChannelConnection getOpposite() {
    switch(this) {
      case IN:  return OUT;
      case OUT: return IN;
    }
    return NONE;
  }

  public ChannelConnection getNext(boolean reverse) {
    if(reverse) {
      switch(this) {
        case NONE: return IN;
        case IN:   return OUT;
        case OUT:  return NONE;
      }
    } else {
      switch(this) {
        case NONE: return OUT;
        case OUT:  return IN;
        case IN:   return NONE;
      }
    }
    // not possible
    throw new UnsupportedOperationException();
  }

  public static ChannelConnection fromIndex(int index) {
    if(index < 0 || index >= values().length) {
      return NONE;
    }

    return values()[index];
  }

  @Override
  public String getName() {
    return this.toString().toLowerCase(Locale.US);
  }

  public boolean canFlow() {
    return this != NONE;
  }

  public static boolean canFlow(ChannelConnection connection) {
    return connection != null && connection != NONE;
  }
}
