package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeAbilityAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModSlippery extends ToolModifier {
  public ModSlippery() {
    super("slippery", 0x87CEEB);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeAbilityAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
