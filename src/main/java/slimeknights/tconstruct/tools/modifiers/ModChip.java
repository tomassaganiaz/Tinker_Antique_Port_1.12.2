package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModChip extends ToolModifier {
  public ModChip() {
    super("chip", 0x9E9E9E);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
