package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeAbilityAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModZooming extends ToolModifier {
  public ModZooming() {
    super("zooming", 0xFFD700);
    addAspects(new DataAspect(this), new LevelAspect(this, 3), new FreeAbilityAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
