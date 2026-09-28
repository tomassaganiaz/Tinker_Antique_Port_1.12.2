package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

/** Can only be applied once. */
public class SingleAspect extends ModifierAspect {

  public SingleAspect(IModifier parent) {
    super(parent);
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    // check if the modifier is present in the base info.
    // this is not the same as checking if the modifier has data. But should be sufficient
    if(TinkerUtil.hasModifier(TagUtil.getTagSafe(stack), parent.getIdentifier())) {
      // check if original already had it too
      if(TinkerUtil.hasModifier(TagUtil.getTagSafe(original), parent.getIdentifier())) {
        // error, can't apply if it already had it
        throw new TinkerGuiException(I18n.translateToLocalFormatted("gui.error.single_modifier",
                                                                    parent.getLocalizedName()));
      }
      else {
        // original didn't have it, we can apply it once therefore, no error
        return false;
      }
    }

    // applicable if not found
    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    // no extra information needed, taken care of by base modifier
  }
}