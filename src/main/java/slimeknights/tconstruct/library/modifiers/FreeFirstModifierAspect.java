package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

/** Like {@link FreeModifierAspect}, but skips the free modifier check if the modifier is already present. */
public class FreeFirstModifierAspect extends FreeModifierAspect {

  public FreeFirstModifierAspect(IModifier parent, int requiredModifiers) {
    super(parent, requiredModifiers);
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    // can always apply if the parent already has the modifier
    if(TinkerUtil.hasModifier(TagUtil.getTagSafe(stack), parent.getIdentifier())) {
      return true;
    }

    // otherwise he requires free modifiers
    return super.canApply(stack, original);
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    // same as above, if already present we don't need to reduce the free modifiers
    if(modifierTag.hasKey("modifierUsed")) {
      return;
    }

    super.updateNBT(root, modifierTag);
    modifierTag.setBoolean("modifierUsed", true);
  }
}