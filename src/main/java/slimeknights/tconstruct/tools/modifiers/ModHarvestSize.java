package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Locale;

import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.SingleAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModHarvestSize extends ToolModifier {

  public ModHarvestSize(String name) {
    super("harvest" + name.toLowerCase(Locale.US), 0xcaf6a2);

    addAspects(new SingleAspect(this), new DataAspect(this), ModifierAspect.aoeOnly, ModifierAspect.freeModifier);
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    // no extra data needed
  }
}
