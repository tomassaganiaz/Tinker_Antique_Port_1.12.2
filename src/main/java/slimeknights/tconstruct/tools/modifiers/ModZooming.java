package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModZooming extends ToolModifier {
  public ModZooming() {
    super("zooming", 0xFFD700);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeAbilityAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
