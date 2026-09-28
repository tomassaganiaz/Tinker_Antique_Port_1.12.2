package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import slimeknights.tconstruct.library.Util;

/**
 * Saves the base data of the modifier onto the tool.
 * Any modifier not having this has to take care of it itself.
 */
public class DataAspect extends ModifierAspect {

  private final int color;

  public DataAspect(IModifier parent, TextFormatting color) {
    this(parent, Util.enumChatFormattingToColor(color));
  }

  public DataAspect(IModifier parent, int color) {
    super(parent);
    this.color = color;
  }

  public <T extends IModifier & IModifierDisplay> DataAspect(T parent) {
    super(parent);
    this.color = parent.getColor();
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) {
    // can always apply
    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    ModifierNBT data = ModifierNBT.readTag(modifierTag);
    data.identifier = parent.getIdentifier();
    data.color = color;
    data.write(modifierTag);
  }
}