package slimeknights.tconstruct.library.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.tinkering.TinkersItem;

/** 
Helpers de encantamientos de herramientas.
 */
public final class 
EnchantmentHelper
 {

  private 
EnchantmentHelper
() {
  }

  public static short getEnchantmentLevel(NBTTagCompound rootTag, Enchantment enchantment) {
    NBTTagList enchantments = rootTag.getTagList("ench", 10);

    int id = Enchantment.getEnchantmentID(enchantment);

    for(int i = 0; i < enchantments.tagCount(); i++) {
      if(enchantments.getCompoundTagAt(i).getShort("id") == id) {
        return enchantments.getCompoundTagAt(i).getShort("lvl");
      }
    }

    return 0;
  }

  public static void addEnchantment(NBTTagCompound rootTag, Enchantment enchantment) {
    NBTTagList enchantments = rootTag.getTagList("ench", 10);

    NBTTagCompound enchTag = new NBTTagCompound();
    int enchId = Enchantment.getEnchantmentID(enchantment);

    int id = -1;
    for(int i = 0; i < enchantments.tagCount(); i++) {
      if(enchantments.getCompoundTagAt(i).getShort("id") == enchId) {
        enchTag = enchantments.getCompoundTagAt(i);
        id = i;
        break;
      }
    }

    int level = enchTag.getShort("lvl") + 1;
    level = Math.min(level, enchantment.getMaxLevel());
    enchTag.setShort("id", (short) enchId);
    enchTag.setShort("lvl", (short) level);

    if(id < 0) {
      enchantments.appendTag(enchTag);
    }
    else {
      enchantments.set(id, enchTag);
    }

    rootTag.setTag("ench", enchantments);
  }

}
