package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModShattering extends ToolModifier {
  public ModShattering() {
    super("shattering", 0xB0B0B0);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
