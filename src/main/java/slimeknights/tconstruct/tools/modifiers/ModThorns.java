package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.Tags;

public class ModThorns extends ToolModifier {
  public ModThorns() {
    super("thorns", 0x3A7D44);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeUpgradeAspect(1));
  }
  @Override
  public void applyEffect(net.minecraft.nbt.NBTTagCompound rootCompound, net.minecraft.nbt.NBTTagCompound modifierTag) {}
  @Override
  public String getTooltip(net.minecraft.nbt.NBTTagCompound modifierTag, boolean detailed) {
    return getLeveledTooltip(modifierTag, detailed);
  }
}
