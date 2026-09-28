package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

/** Consumes free ability slots instead of generic free modifiers. */
public class FreeAbilityAspect extends ModifierAspect {

  private final int required;

  public FreeAbilityAspect(int required) {
    this.required = required;
  }

  protected FreeAbilityAspect(IModifier parent, int required) {
    super(parent);
    this.required = required;
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    NBTTagCompound tag = TagUtil.getToolTag(stack);
    int free = tag.getInteger(Tags.FREE_ABILITIES);
    if(free == 0 && tag.hasKey(Tags.FREE_MODIFIERS)) {
      free = tag.getInteger(Tags.FREE_MODIFIERS);
    }
    if(free < required) {
      throw new TinkerGuiException(I18n.translateToLocalFormatted("gui.error.not_enough_modifiers", required));
    }
    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modTag) {
    NBTTagCompound tag = TagUtil.getToolTag(root);
    int free = tag.getInteger(Tags.FREE_ABILITIES);
    if(free == 0 && tag.hasKey(Tags.FREE_MODIFIERS)) {
      free = tag.getInteger(Tags.FREE_MODIFIERS);
    }
    tag.setInteger(Tags.FREE_ABILITIES, Math.max(0, free - required));
    if(tag.hasKey(Tags.FREE_MODIFIERS)) {
      tag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, tag.getInteger(Tags.FREE_MODIFIERS) - required));
    }
    TagUtil.setToolTag(root, tag);
  }
}