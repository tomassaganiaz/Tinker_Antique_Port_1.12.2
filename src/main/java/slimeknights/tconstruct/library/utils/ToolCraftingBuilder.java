package slimeknights.tconstruct.library.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import org.apache.logging.log4j.Logger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import slimeknights.mantle.util.ItemStackList;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.OnItemBuilding;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.events.TinkerEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.tinkering.IRepairable;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tinkering.TinkersItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;
import static slimeknights.tconstruct.library.utils.ToolBuilder.log;
import slimeknights.tconstruct.tools.modifiers.ModFortify;

/** 
Construccion de herramientas y partes.
 */
public final class 
ToolCraftingBuilder
 {

  private 
ToolCraftingBuilder
() {
  }

  public static ItemStack tryBuildTool(NonNullList<ItemStack> stacks, String name) {
    return tryBuildTool(stacks, name, TinkerRegistry.getTools());
  }

  public static ItemStack tryBuildTool(NonNullList<ItemStack> stacks, String name, Collection<ToolCore> possibleTools) {
    int length = -1;
    NonNullList<ItemStack> input;
    // remove trailing empty slots
    for(int i = 0; i < stacks.size(); i++) {
      if(stacks.get(i).isEmpty()) {
        if(length < 0) {
          length = i;
        }
      }
      else if(length >= 0) {
        // incorrect input. gap with null in the stacks passed
        return ItemStack.EMPTY;
      }
    }

    if(length < 0) {
      return ItemStack.EMPTY;
    }

    input = ItemStackList.of(stacks);

    for(Item item : possibleTools) {
      if(!(item instanceof ToolCore)) {
        continue;
      }
      ItemStack output = ((ToolCore) item).buildItemFromStacks(input);
      if(!output.isEmpty()) {
        // name the item
        if(name != null && !name.isEmpty()) {
          output.setStackDisplayName(name);
        }

        return output;
      }
    }

    return ItemStack.EMPTY;
  }

  public static NonNullList<ItemStack> tryBuildToolPart(ItemStack pattern, NonNullList<ItemStack> materialItems, boolean removeItems)
      throws TinkerGuiException {
    Item itemPart = Pattern.getPartFromTag(pattern);
    if(itemPart == null || !(itemPart instanceof MaterialItem) || !(itemPart instanceof IToolPart)) {
      String error = I18n.translateToLocalFormatted("gui.error.invalid_pattern");
      throw new TinkerGuiException(error);
    }

    IToolPart part = (IToolPart) itemPart;

    if(!removeItems) {
      materialItems = Util.deepCopyFixedNonNullList(materialItems);
    }

    // find the material from the input
    Optional<RecipeMatch.Match> match = Optional.empty();
    Material foundMaterial = null;
    for(Material material : TinkerRegistry.getAllMaterials()) {
      // craftable?
      if(!material.isCraftable()) {
        continue;
      }
      Optional<RecipeMatch.Match> newMatch = material.matches(materialItems, part.getCost());
      if(!newMatch.isPresent()) {
        continue;
      }

      // we found a match, yay
      if(!match.isPresent()) {
        match = newMatch;
        foundMaterial = material;
        // is it more complex than the old one?
      }
    }

    // nope, no material
    if(!match.isPresent()) {
      return null;
    }

    ItemStack output = ((MaterialItem) itemPart).getItemstackWithMaterial(foundMaterial);
    if(output.isEmpty()) {
      return null;
    }
    if(output.getItem() instanceof IToolPart && !((IToolPart) output.getItem()).canUseMaterial(foundMaterial)) {
      return null;
    }

    RecipeMatch.removeMatch(materialItems, match.get());

    // check if we have secondary output
    ItemStack secondary = ItemStack.EMPTY;
    int leftover = (match.get().amount - part.getCost()) / Material.VALUE_Shard;
    if(leftover > 0) {
      secondary = TinkerRegistry.getShard(foundMaterial);
      secondary.setCount(leftover);
    }

    // build an item with this
    return ListUtil.getListFrom(output, secondary);
  }

  public static void rebuildTool(NBTTagCompound rootNBT, TinkersItem tinkersItem) throws TinkerGuiException {
    boolean broken = TagUtil.getToolTag(rootNBT).getBoolean(Tags.BROKEN);
    // Recalculate tool base stats from material stats
    NBTTagList materialTag = TagUtil.getBaseMaterialsTagList(rootNBT);
    List<Material> materials = TinkerUtil.getMaterialsFromTagList(materialTag);
    List<PartMaterialType> pms = tinkersItem.getRequiredComponents();

    // ensure all needed Stats are present
    while(materials.size() < pms.size()) {
      materials.add(Material.UNKNOWN);
    }
    for(int i = 0; i < pms.size(); i++) {
      if(!pms.get(i).isValidMaterial(materials.get(i))) {
        materials.set(i, Material.UNKNOWN);
      }
    }

    // the base stats of the tool
    NBTTagCompound toolTag = tinkersItem.buildTag(materials);
    TagUtil.setToolTag(rootNBT, toolTag);
    // and its copy for reference
    rootNBT.setTag(Tags.TOOL_DATA_ORIG, toolTag.copy());

    // save the old modifiers list and clean up all tags that get set by modifiers/traits
    NBTTagList modifiersTagOld = TagUtil.getModifiersTagList(rootNBT);
    rootNBT.removeTag(Tags.TOOL_MODIFIERS); // the active-modifiers tag
    rootNBT.setTag(Tags.TOOL_MODIFIERS, new NBTTagList());
    rootNBT.removeTag("ench"); // and the enchantments tag
    rootNBT.removeTag(Tags.ENCHANT_EFFECT); // enchant effect too, will be readded by a trait either way

    // clean up traits
    rootNBT.removeTag(Tags.TOOL_TRAITS);
    tinkersItem.addMaterialTraits(rootNBT, materials);

    // fire event
    OnItemBuilding.fireEvent(rootNBT, ImmutableList.copyOf(materials), tinkersItem);

    // reapply modifiers
    NBTTagList modifiers = TagUtil.getBaseModifiersTagList(rootNBT);
    NBTTagList modifiersTag = TagUtil.getModifiersTagList(rootNBT);
    // copy over and reapply all relevant modifiers
    for(int i = 0; i < modifiers.tagCount(); i++) {
      String identifier = modifiers.getStringTagAt(i);
      IModifier modifier = TinkerRegistry.getModifier(identifier);
      if(modifier == null) {
        log.debug("Missing modifier: {}", identifier);
        continue;
      }

      NBTTagCompound tag;
      int index = TinkerUtil.getIndexInList(modifiersTagOld, modifier.getIdentifier());

      if(index >= 0) {
        tag = modifiersTagOld.getCompoundTagAt(index);
      }
      else {
        tag = new NBTTagCompound();
      }

      modifier.applyEffect(rootNBT, tag);
      if(!tag.hasNoTags()) {
        int indexNew = TinkerUtil.getIndexInList(modifiersTag, modifier.getIdentifier());
        if(indexNew >= 0) {
          modifiersTag.set(indexNew, tag);
        }
        else {
          modifiersTag.appendTag(tag);
        }
      }
    }

    // remaining info, get updated toolTag
    toolTag = TagUtil.getToolTag(rootNBT);
    // adjust free modifiers
    int freeModifiers = toolTag.getInteger(Tags.FREE_MODIFIERS);
    freeModifiers -= TagUtil.getBaseModifiersUsed(rootNBT);
    toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, freeModifiers));

    // broken?
    if(broken) {
      toolTag.setBoolean(Tags.BROKEN, true);
    }

    TagUtil.setToolTag(rootNBT, toolTag);

    if(freeModifiers < 0) {
      throw new TinkerGuiException(Util.translateFormatted("gui.error.not_enough_modifiers", -freeModifiers));
    }
  }

}
