package slimeknights.tconstruct.library.utils;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.tools.TinkerModifiers;

/** 
Lectura de las estadisticas basicas de una herramienta desde su NBT.
 */
public final class 
ToolStatsHelper
 {

  private 
ToolStatsHelper
() {
  }

  public static boolean hasCategory(ItemStack stack, Category category) {
    if(stack.isEmpty() || !(stack.getItem() instanceof TinkersItem)) {
      return false;
    }

    return ((TinkersItem) stack.getItem()).hasCategory(category);
  }

  public static int getDurabilityStat(ItemStack stack) {
    return getIntTag(stack, Tags.DURABILITY);
  }

  public static int getHarvestLevelStat(ItemStack stack) {
    return getIntTag(stack, Tags.HARVESTLEVEL);
  }

  public static float getMiningSpeedStat(ItemStack stack) {
    return getfloatTag(stack, Tags.MININGSPEED);
  }

  public static float getAttackStat(ItemStack stack) {
    return getfloatTag(stack, Tags.ATTACK);
  }

  public static float getActualAttack(ItemStack stack) {
    float damage = getAttackStat(stack);
    if(!stack.isEmpty() && stack.getItem() instanceof ToolCore) {
      damage *= ((ToolCore) stack.getItem()).damagePotential();
    }
    return damage;
  }

  public static float getAttackSpeedStat(ItemStack stack) {
    return getfloatTag(stack, Tags.ATTACKSPEEDMULTIPLIER);
  }

  public static float getActualAttackSpeed(ItemStack stack) {
    float speed = getAttackSpeedStat(stack);
    if(!stack.isEmpty() && stack.getItem() instanceof ToolCore) {
      speed *= ((ToolCore) stack.getItem()).attackSpeed();
    }
    return speed;
  }

  public static float getActualMiningSpeed(ItemStack stack) {
    float speed = getMiningSpeedStat(stack);
    if(!stack.isEmpty() && stack.getItem() instanceof ToolCore) {
      speed *= ((ToolCore) stack.getItem()).miningSpeedModifier();
    }
    return speed;
  }

  public static int getFreeModifiers(ItemStack stack) {
    return getIntTag(stack, Tags.FREE_MODIFIERS);
  }

  public static int getFortuneLevel(ItemStack stack) {
    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
    int luck = TinkerModifiers.modLuck.getLuckLevel(stack);

    return Math.max(fortune, luck);
  }

  public static List<ITrait> getTraits(ItemStack stack) {
    List<ITrait> traits = Lists.newLinkedList();
    NBTTagList traitsTagList = TagUtil.getTraitsTagList(stack);
    for(int i = 0; i < traitsTagList.tagCount(); i++) {
      ITrait trait = TinkerRegistry.getTrait(traitsTagList.getStringTagAt(i));
      if(trait != null) {
        traits.add(trait);
      }
    }
    return traits;
  }

  static int getIntTag(ItemStack stack, String key) {
    NBTTagCompound tag = TagUtil.getToolTag(stack);

    return tag.getInteger(key);
  }

  static float getfloatTag(ItemStack stack, String key) {
    NBTTagCompound tag = TagUtil.getToolTag(stack);

    return tag.getFloat(key);
  }
}
