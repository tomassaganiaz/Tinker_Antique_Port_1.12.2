package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.utils.ToolHelper;

/** Applicable on all tools that have at least one of those categories. */
public class CategoryAnyAspect extends CategoryAspect {

  public CategoryAnyAspect(Category... category) {
    super(category);
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) {
    for(Category cat : category) {
      if(ToolHelper.hasCategory(stack, cat)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    super.updateNBT(root, modifierTag);
  }
}