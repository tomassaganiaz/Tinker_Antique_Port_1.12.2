package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.SingleAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModIncognito extends ToolModifier {

  public ModIncognito() {
    super("incognito", 0x575757);

    addAspects(new SingleAspect(this), new DataAspect(this));
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    // nothing to do :(
  }
}
