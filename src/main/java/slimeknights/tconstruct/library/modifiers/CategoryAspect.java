package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.utils.ToolHelper;

/** Only applicable on tools with the given categories. */
public class CategoryAspect extends ModifierAspect {

  protected final Category[] category;

  public CategoryAspect(Category... category) {
    this.category = category;
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) {
    for(Category cat : category) {
      if(!ToolHelper.hasCategory(stack, cat)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    // no extra information needed
  }
}