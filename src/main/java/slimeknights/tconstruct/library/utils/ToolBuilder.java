package slimeknights.tconstruct.library.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.Collection;

import org.apache.logging.log4j.Logger;

import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * Fachada de la construccion de herramientas.
 * <p>
 * La implementación vive en {@link ToolCraftingBuilder} (build), {@link ToolModifyBuilder}
 * (reparar/modificar/reemplazar partes) y {@link EnchantmentHelper}. Esta clase solo expone la
 * API pública estática delegando en ellos.
 */
public final class ToolBuilder {

  static Logger log = Util.getLogger("ToolBuilder");

  private ToolBuilder() {
  }

  public static ItemStack tryBuildTool(NonNullList<ItemStack> stacks, String name) {
    return ToolCraftingBuilder.tryBuildTool(stacks, name);
  }

  public static ItemStack tryBuildTool(NonNullList<ItemStack> stacks, String name, Collection<ToolCore> possibleTools) {
    return ToolCraftingBuilder.tryBuildTool(stacks, name, possibleTools);
  }

  public static void addTrait(NBTTagCompound rootCompound, ITrait trait, int color) {
    ToolModifyBuilder.addTrait(rootCompound, trait, color);
  }

  public static ItemStack tryRepairTool(NonNullList<ItemStack> stacks, ItemStack toolStack, boolean removeItems) {
    return ToolModifyBuilder.tryRepairTool(stacks, toolStack, removeItems);
  }

  public static ItemStack tryModifyTool(NonNullList<ItemStack> input, ItemStack toolStack, boolean removeItems) throws TinkerGuiException {
    return ToolModifyBuilder.tryModifyTool(input, toolStack, removeItems);
  }

  public static ItemStack tryReplaceToolParts(ItemStack toolStack, final NonNullList<ItemStack> toolPartsIn, final boolean removeItems) throws TinkerGuiException {
    return ToolModifyBuilder.tryReplaceToolParts(toolStack, toolPartsIn, removeItems);
  }

  public static NonNullList<ItemStack> tryBuildToolPart(ItemStack pattern, NonNullList<ItemStack> materialItems, boolean removeItems) throws TinkerGuiException {
    return ToolCraftingBuilder.tryBuildToolPart(pattern, materialItems, removeItems);
  }

  public static void rebuildTool(NBTTagCompound rootNBT, TinkersItem tinkersItem) throws TinkerGuiException {
    ToolCraftingBuilder.rebuildTool(rootNBT, tinkersItem);
  }

  public static short getEnchantmentLevel(NBTTagCompound rootTag, Enchantment enchantment) {
    return EnchantmentHelper.getEnchantmentLevel(rootTag, enchantment);
  }

  public static void addEnchantment(NBTTagCompound rootTag, Enchantment enchantment) {
    EnchantmentHelper.addEnchantment(rootTag, enchantment);
  }
}