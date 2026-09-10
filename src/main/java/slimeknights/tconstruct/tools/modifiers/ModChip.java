package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModChip extends ToolModifier {
  public ModChip() {
    super("chip", 0x9E9E9E);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
