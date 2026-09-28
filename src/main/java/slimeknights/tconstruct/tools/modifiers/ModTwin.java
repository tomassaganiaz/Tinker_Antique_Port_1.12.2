package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModTwin extends ToolModifier {

  public ModTwin() {
    super("twin", 0x6BC8FF);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeUpgradeAspect(1));
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
  }
}
