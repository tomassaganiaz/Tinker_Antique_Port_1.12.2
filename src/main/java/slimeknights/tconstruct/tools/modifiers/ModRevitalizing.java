package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModRevitalizing extends ToolModifier {
  public ModRevitalizing() {
    super("revitalizing", 0x55E0CC);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(net.minecraft.nbt.NBTTagCompound rootCompound, net.minecraft.nbt.NBTTagCompound modifierTag) {}
  @Override public String getTooltip(net.minecraft.nbt.NBTTagCompound modifierTag, boolean detailed) { return getLeveledTooltip(modifierTag, detailed); }
}
