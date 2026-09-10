package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;

public class ModDoubleJump extends ToolModifier {
  public ModDoubleJump() {
    super("double_jump", 0x8CA1FF);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 3), new ModifierAspect.FreeAbilityAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {}
}
