package slimeknights.tconstruct.gadgets.block;  
import net.minecraft.util.IStringSerializable;
import java.util.Locale;


public enum ChannelConnected implements IStringSerializable {
  NONE,
  INNER,
  OUTER;

  @Override
  public String getName() {
    return this.toString().toLowerCase(Locale.US);
  }
}
