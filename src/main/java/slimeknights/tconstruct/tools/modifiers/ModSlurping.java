package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModSlurping extends ToolModifier {
  public ModSlurping() {
    super("slurping", 0x8BC34A);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeAbilityAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
