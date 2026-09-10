package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModWetting extends ToolModifier {
  public ModWetting() {
    super("wetting", 0x4A90E2);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeAbilityAspect(1));
  }
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {}
}
