package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import slimeknights.tconstruct.library.utils.TinkerUtil;

/** Allows the modifier to be applied multiple times up to a max level. */
public class LevelAspect extends ModifierAspect {

  private final int maxLevel;

  public LevelAspect(IModifier parent, int maxLevel) {
    super(parent);
    this.maxLevel = maxLevel;
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    int levelNew = ModifierNBT.readTag(TinkerUtil.getModifierTag(stack, parent.getIdentifier())).level;
    int levelOld = ModifierNBT.readTag(TinkerUtil.getModifierTag(original, parent.getIdentifier())).level;

    // only 1 level per application
    // original and stack are equal for the first application, any multiple applications therefore yield >0
    if(levelNew - levelOld > 0) {
      return false;
    }

    // new level would be above max level
    if(levelNew >= maxLevel) {
      throw new TinkerGuiException(I18n.translateToLocalFormatted("gui.error.max_level_modifier", parent.getLocalizedName()));
    }

    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    ModifierNBT data = ModifierNBT.readTag(modifierTag);
    data.level = Math.min(data.level + 1, maxLevel);
    data.write(modifierTag);
  }
}