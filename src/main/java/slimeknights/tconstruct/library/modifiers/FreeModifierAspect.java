package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.library.utils.ToolHelper;

/** The modifier requires sufficient free modifier slots to be present. */
public class FreeModifierAspect extends ModifierAspect {

  private final int requiredModifiers;

  public FreeModifierAspect(int requiredModifiers) {
    this.requiredModifiers = requiredModifiers;
  }

  protected FreeModifierAspect(IModifier parent, int requiredModifiers) {
    super(parent);
    this.requiredModifiers = requiredModifiers;
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    NBTTagCompound toolTag = TagUtil.getToolTag(stack);
    if(ToolHelper.getFreeModifiers(stack) < requiredModifiers) {
      String error = I18n.translateToLocalFormatted("gui.error.not_enough_modifiers", requiredModifiers);
      throw new TinkerGuiException(error);
    }

    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    // substract the modifiers
    NBTTagCompound toolTag = TagUtil.getToolTag(root);
    int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) - requiredModifiers;
    toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));

    // and increase the count of used modifiers
    int usedModifiers = TagUtil.getBaseModifiersUsed(root);
    usedModifiers += requiredModifiers;
    TagUtil.setBaseModifiersUsed(root, usedModifiers);
  }
}