package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModSpilling extends ToolModifier {
  public ModSpilling() {
    super("spilling", 0xFF6B6B);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeAbilityAspect(1));
  }
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {}
}
