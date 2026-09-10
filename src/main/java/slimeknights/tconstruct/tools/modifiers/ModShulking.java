package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModShulking extends ToolModifier {
  public ModShulking() {
    super("shulking", 0x8A4FFF);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 1), new ModifierAspect.FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(net.minecraft.nbt.NBTTagCompound rootCompound, net.minecraft.nbt.NBTTagCompound modifierTag) {}
  @Override public String getTooltip(net.minecraft.nbt.NBTTagCompound modifierTag, boolean detailed) { return getLeveledTooltip(modifierTag, detailed); }
}
