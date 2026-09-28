package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeAbilityAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModWetting extends ToolModifier {
  public ModWetting() {
    super("wetting", 0x4A90E2);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeAbilityAspect(1));
  }
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {}
}
