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
import slimeknights.tconstruct.library.events.OnToolPartReplacement;
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
Reparacion, modificacion y reemplazo de partes.
 */
public final class 
ToolModifyBuilder
 {

  private 
ToolModifyBuilder
() {
  }

  public static void addTrait(NBTTagCompound rootCompound, ITrait trait, int color) {
    // only registered traits allowed
    if(TinkerRegistry.getTrait(trait.getIdentifier()) == null) {
      log.error("addTrait: Trying to apply unregistered Trait {}", trait.getIdentifier());
      return;
    }

    IModifier modifier = TinkerRegistry.getModifier(trait.getIdentifier());

    if(modifier == null || !(modifier instanceof AbstractTrait)) {
      log.error("addTrait: No matching modifier for the Trait {} present", trait.getIdentifier());
      return;
    }

    AbstractTrait traitModifier = (AbstractTrait) modifier;

    NBTTagCompound tag = new NBTTagCompound();
    NBTTagList tagList = TagUtil.getModifiersTagList(rootCompound);
    int index = TinkerUtil.getIndexInList(tagList, traitModifier.getModifierIdentifier());
    if(index < 0) {
      traitModifier.updateNBTforTrait(tag, color);
      tagList.appendTag(tag);
      TagUtil.setModifiersTagList(rootCompound, tagList);
    }
    else {
      tag = tagList.getCompoundTagAt(index);
    }

    traitModifier.applyEffect(rootCompound, tag);
  }

  public static ItemStack tryRepairTool(NonNullList<ItemStack> stacks, ItemStack toolStack, boolean removeItems) {
    if(toolStack == null || !(toolStack.getItem() instanceof IRepairable)) {
      return ItemStack.EMPTY;
    }

    // obtain a working copy of the items if the originals shouldn't be modified
    if(!removeItems) {
      stacks = Util.deepCopyFixedNonNullList(stacks);
    }

    return ((IRepairable) toolStack.getItem()).repair(toolStack, stacks);
  }

  public static ItemStack tryModifyTool(NonNullList<ItemStack> input, ItemStack toolStack, boolean removeItems)
      throws TinkerGuiException {
    ItemStack copy = toolStack.copy();

    // obtain a working copy of the items if the originals shouldn't be modified
    NonNullList<ItemStack> stacks = Util.deepCopyFixedNonNullList(input);
    NonNullList<ItemStack> usedStacks = Util.deepCopyFixedNonNullList(input);

    Set<IModifier> appliedModifiers = Sets.newHashSet();
    for(IModifier modifier : TinkerRegistry.getAllModifiers()) {
      Optional<RecipeMatch.Match> matchOptional;
      do {
        matchOptional = modifier.matches(stacks);
        ItemStack backup = copy.copy();

        // found a modifier that is applicable. Try to apply the match
        if(matchOptional.isPresent()) {
          RecipeMatch.Match match = matchOptional.get();
          // we need to apply the whole match
          while(match.amount > 0) {
            TinkerGuiException caughtException = null;
            boolean canApply = false;
            try {
              canApply = modifier.canApply(copy, toolStack);
            } catch(TinkerGuiException e) {
              caughtException = e;
            }

            // but can it be applied?
            if(canApply) {
              modifier.apply(copy);

              appliedModifiers.add(modifier);
              match.amount--;
            }
            else {
              // materials would allow another application, but modifier doesn't
              // if we have already applied another modifier we cancel the whole thing to prevent situations where
              // only a part of the modifiers gets applied. either all or none.
              // if we have a reason, rather tell the player that
              if(caughtException != null && !appliedModifiers.contains(modifier)) {
                throw caughtException;
              }

              copy = backup;
              RecipeMatch.removeMatch(stacks, match);
              break;
            }
          }

          if(match.amount == 0) {
            RecipeMatch.removeMatch(stacks, match);
            RecipeMatch.removeMatch(usedStacks, match);
          }
        }
      } while(matchOptional.isPresent());
    }

    // check if all itemstacks were touched - otherwise there's an invalid item in the input
    for(int i = 0; i < input.size(); i++) {
      if(!input.get(i).isEmpty() && ItemStack.areItemStacksEqual(input.get(i), stacks.get(i))) {
        if(!appliedModifiers.isEmpty()) {
          String error = I18n.translateToLocalFormatted("gui.error.no_modifier_for_item", input.get(i).getDisplayName());
          throw new TinkerGuiException(error);
        }
        return ItemStack.EMPTY;
      }
    }

    // update output itemstacks
    if(removeItems) {
      for(int i = 0; i < input.size(); i++) {
        // stacks might be null because stacksize got 0 during processing, we have to reflect that in the input
        // so the caller can identify that
        if(usedStacks.get(i).isEmpty()) {
          input.get(i).setCount(0);
        }
        else {
          input.get(i).setCount(usedStacks.get(i).getCount());
        }
      }
    }

    if(!appliedModifiers.isEmpty()) {
      // always rebuild tinkers items to ensure consistency and find problems earlier
      if(copy.getItem() instanceof TinkersItem) {
        NBTTagCompound root = TagUtil.getTagSafe(copy);
        ToolCraftingBuilder.rebuildTool(root, (TinkersItem) copy.getItem());
        copy.setTagCompound(root);
      }
      return copy;
    }

    return ItemStack.EMPTY;
  }

  public static ItemStack tryReplaceToolParts(ItemStack toolStack, final NonNullList<ItemStack> toolPartsIn, final boolean removeItems)
      throws TinkerGuiException {
    if(toolStack == null || !(toolStack.getItem() instanceof TinkersItem)) {
      return ItemStack.EMPTY;
    }

    // we never modify the original. Caller can remove all of them if we return a result
    NonNullList<ItemStack> inputItems = ItemStackList.of(Util.deepCopyFixedNonNullList(toolPartsIn));
    if(!OnToolPartReplacement.fireEvent(inputItems, toolStack)) {
      // event cancelled
      return ItemStack.EMPTY;
    }
    // technically we don't need a deep copy here, but meh. less code.
    final NonNullList<ItemStack> toolParts = Util.deepCopyFixedNonNullList(inputItems);

    TIntIntMap assigned = new TIntIntHashMap();
    TinkersItem tool = (TinkersItem) toolStack.getItem();
    // materiallist has to be copied because it affects the actual NBT on the tool if it's changed
    final NBTTagList materialList = TagUtil.getBaseMaterialsTagList(toolStack).copy();

    Material newHeadMaterial = null;
    // assign each toolpart to a slot in the tool
    for(int i = 0; i < toolParts.size(); i++) {
      ItemStack part = toolParts.get(i);
      if(part.isEmpty()) {
        continue;
      }
      if(!(part.getItem() instanceof IToolPart)) {
        // invalid item for toolpart replacement
        return ItemStack.EMPTY;
      }

      int candidate = -1;
      // find an applicable slot in the tool structure corresponding to the toolparts position
      List<PartMaterialType> pms = tool.getRequiredComponents();
      for(int j = 0; j < pms.size(); j++) {
        PartMaterialType pmt = pms.get(j);
        String partMat = ((IToolPart) part.getItem()).getMaterial(part).getIdentifier();
        String currentMat = materialList.getStringTagAt(j);
        // is valid and not the same material?
        if(pmt.isValid(part) && !partMat.equals(currentMat)) {
          // part not taken up by previous part already?
          if(!assigned.valueCollection().contains(j)) {
            candidate = j;
            // if a tool has multiple of the same parts we may want to replace another one as the currently selected
            // for that purpose we only allow to overwrite the current selection if the input slot is a later one than the current one
            if(i <= j) {
              break;
            }
          }
        }
      }

      // if this part is a head type, capture its material for later fortify comparison
      if(candidate >= 0) {
        PartMaterialType pmt = tool.getRequiredComponents().get(candidate);
        if(pmt.usesStat(MaterialTypes.HEAD)) {
          newHeadMaterial = ((IToolPart) part.getItem()).getMaterial(part);
        }
      }
      // no assignment found for a part. Invalid input.
      else {
        return ItemStack.EMPTY;
      }
      assigned.put(i, candidate);
    }

    // did we assign nothing?
    if(assigned.isEmpty()) {
      return ItemStack.EMPTY;
    }

    // We now know which parts to replace with which inputs. Yay. Now we only have to do so.
    // to do so we simply switch out the materials used and rebuild the tool
    assigned.forEachEntry((i, j) -> {
      String mat = ((IToolPart) toolParts.get(i).getItem()).getMaterial(toolParts.get(i)).getIdentifier();
      materialList.set(j, new NBTTagString(mat));
      if(removeItems) {
        if(i < toolPartsIn.size() && !toolPartsIn.get(i).isEmpty()) {
          toolPartsIn.get(i).shrink(1);
        }
      }
      return true;
    });

    // check that each material is still compatible with each modifier
    TinkersItem tinkersItem = (TinkersItem) toolStack.getItem();
    ItemStack copyToCheck = tinkersItem.buildItem(TinkerUtil.getMaterialsFromTagList(materialList));
    // this includes traits
    NBTTagList modifiers = TagUtil.getBaseModifiersTagList(toolStack);
    for(int i = 0; i < modifiers.tagCount(); i++) {
      String id = modifiers.getStringTagAt(i);
      IModifier mod = TinkerRegistry.getModifier(id);

      boolean canApply = false;
      try {
        // will throw an exception if it can't apply
        canApply = mod != null && mod.canApply(copyToCheck, copyToCheck);
      } catch(TinkerGuiException e) {
        // try again with more modifiers, in case something modified them (tinkers tool leveling)
        // ensure that free modifiers are present (
        if(ToolHelper.getFreeModifiers(copyToCheck) < ToolCore.DEFAULT_MODIFIERS) {
          ItemStack copyWithModifiers = copyToCheck.copy();
          NBTTagCompound nbt = TagUtil.getToolTag(copyWithModifiers);
          nbt.setInteger(Tags.FREE_MODIFIERS, ToolCore.DEFAULT_MODIFIERS);
          TagUtil.setToolTag(copyWithModifiers, nbt);
          canApply = mod.canApply(copyWithModifiers, copyWithModifiers);
        }
      }
      if(!canApply) {
        throw new TinkerGuiException();
      }
    }

    final NBTTagList modifierList = TagUtil.getBaseModifiersTagList(toolStack).copy();
    for(int i = 0; i < modifierList.tagCount(); i++) {
      String id = modifierList.getStringTagAt(i);
      IModifier mod = TinkerRegistry.getModifier(id);
      // if the new head's harvest level equals/exceeds the fortification level, it's no longer beneficial. good riddance!
      if(newHeadMaterial != null && mod instanceof ModFortify) {
        HeadMaterialStats newHeadStats = newHeadMaterial.getStats(MaterialTypes.HEAD);
        HeadMaterialStats fortifyStats = ((ModFortify) mod).material.getStats(MaterialTypes.HEAD);
        if(newHeadStats != null && fortifyStats != null && newHeadStats.harvestLevel >= fortifyStats.harvestLevel) {
          modifierList.removeTag(i);
        }
      }
    }

    ItemStack output = toolStack.copy();
    TagUtil.setBaseMaterialsTagList(output, materialList);
    TagUtil.setBaseModifiersTagList(output, modifierList);
    NBTTagCompound tag = TagUtil.getTagSafe(output);
    ToolCraftingBuilder.rebuildTool(tag, (TinkersItem) output.getItem());
    output.setTagCompound(tag);

    // check if the output has enough durability. we only allow it if the result would not be broken
    if(output.getItemDamage() > output.getMaxDamage()) {
      String error = I18n.translateToLocalFormatted("gui.error.not_enough_durability", output.getItemDamage() - output.getMaxDamage());
      throw new TinkerGuiException(error);
    }

    return output;
  }

}
