package slimeknights.tconstruct.library.modifiers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import slimeknights.tconstruct.library.utils.TinkerUtil;

/** The modifier can be applied several times per modifier used. */
public class MultiAspect extends ModifierAspect {

  protected final int countPerLevel;

  protected DataAspect dataAspect;
  protected LevelAspect levelAspect;
  protected FreeModifierAspect freeModifierAspect;

  public <T extends IModifier & IModifierDisplay> MultiAspect(T parent, int maxLevel, int countPerLevel, int modifiersNeeded) {
    this(parent, parent.getColor(), maxLevel, countPerLevel, modifiersNeeded);
  }

  // multiple levels, once every time the maximum is reached
  public MultiAspect(IModifier parent, int color, int maxLevel, int countPerLevel, int modifiersNeeded) {
    super(parent);
    this.countPerLevel = countPerLevel;

    dataAspect = new DataAspect(parent, color);
    freeModifierAspect = new FreeModifierAspect(modifiersNeeded);
    levelAspect = new LevelAspect(parent, maxLevel);
  }

  // single-level
  public MultiAspect(IModifier parent, int color, int count) {
    this(parent, color, 1, count, 1);
  }

  protected int getMaxForLevel(int level) {
    return countPerLevel * level;
  }

  @Override
  public boolean canApply(ItemStack stack, ItemStack original) throws TinkerGuiException {
    // check if the threshold has been reached
    NBTTagCompound modifierTag = TinkerUtil.getModifierTag(stack, parent.getIdentifier());
    ModifierNBT.IntegerNBT data = getData(modifierTag);

    // the current level is full / level is 0
    if(data.current >= getMaxForLevel(data.level)) {
      // can we even apply a new level?
      if(!levelAspect.canApply(stack, original)) {
        return false;
      }

      // enough modifiers for another level?
      if(!freeModifierAspect.canApply(stack, original)) {
        return false;
      }
    }

    // we have not maxed out this level OR we have enough modifiers and can add a new level
    return true;
  }

  @Override
  public void updateNBT(NBTTagCompound root, NBTTagCompound modifierTag) {
    // simple data
    dataAspect.updateNBT(root, modifierTag);

    // increase the current level progress
    ModifierNBT.IntegerNBT data = getData(modifierTag);

    // new level?
    if(data.current >= getMaxForLevel(data.level)) {
      // remove modifiers
      freeModifierAspect.updateNBT(root, modifierTag);
      // add a level
      levelAspect.updateNBT(root, modifierTag);

      // update max. but to do so, we have to re-read the changed data again
      data = getData(modifierTag);
    }

    // always update max in case it changed since it got saved
    data.max = getMaxForLevel(data.level);

    // increase the level progress
    data.current++;
    data.write(modifierTag);
  }

  private ModifierNBT.IntegerNBT getData(NBTTagCompound tag) {
    ModifierNBT.IntegerNBT data = ModifierNBT.readInteger(tag);

    if(data.max == 0) {
      data.max = getMaxForLevel(data.level);
    }

    return data;
  }
}