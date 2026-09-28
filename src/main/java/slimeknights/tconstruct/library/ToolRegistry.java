package slimeknights.tconstruct.library;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import slimeknights.tconstruct.library.events.StencilTableCraftingRegisterEvent;
import slimeknights.tconstruct.library.events.ToolForgeCraftingRegisterEvent;
import slimeknights.tconstruct.library.events.ToolStationCraftingRegisterEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.IPattern;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Shard;
import slimeknights.tconstruct.library.tools.ToolCore;

import static slimeknights.tconstruct.library.TinkerRegistry.log;

/** 
Registro de herramientas, partes y crafting.
 */
public final class 
ToolRegistry
 {

  private 
ToolRegistry
() {
  }

  private static final Set<ToolCore> tools = new ObjectLinkedOpenHashSet<>();
  private static final Set<IToolPart> toolParts = new ObjectLinkedOpenHashSet<>();
  private static final Set<ToolCore> toolStationCrafting = new ObjectLinkedOpenHashSet<>();
  private static final Set<ToolCore> toolForgeCrafting = new ObjectLinkedOpenHashSet<>();
  private static final List<ItemStack> stencilTableCrafting = new ObjectArrayList<>();
  private static final Set<Item> patternItems = new ObjectOpenHashSet<>();
  private static final Set<Item> castItems = new ObjectOpenHashSet<>();
  private static Shard shardItem;

  public static void registerTool(ToolCore tool) {
    tools.add(tool);

    for(PartMaterialType pmt : tool.getRequiredComponents()) {
      for(IToolPart tp : pmt.getPossibleParts()) {
        registerToolPart(tp);
      }
    }
  }

  public static Set<ToolCore> getTools() {
    return ImmutableSet.copyOf(tools);
  }

  public static void registerToolPart(IToolPart part) {
    toolParts.add(part);
    if(part instanceof Item) {
      if(part.canBeCrafted()) {
        addPatternForItem((Item) part);
      }
      if(part.canBeCasted()) {
        addCastForItem((Item) part);
      }
    }
  }

  public static Set<IToolPart> getToolParts() {
    return ImmutableSet.copyOf(toolParts);
  }

  public static void registerToolCrafting(ToolCore tool) {
    registerToolStationCrafting(tool);
    registerToolForgeCrafting(tool);
  }

  public static void registerToolStationCrafting(ToolCore tool) {
    if(new ToolStationCraftingRegisterEvent(tool).fire()) {
      toolStationCrafting.add(tool);
    } else {
      log.debug("Registration of tool station recipe " + tool.getRegistryName().toString() + " has been cancelled by event");
    }
  }

  public static Set<ToolCore> getToolStationCrafting() {
    return ImmutableSet.copyOf(toolStationCrafting);
  }

  public static void registerToolForgeCrafting(ToolCore tool) {
    if(new ToolForgeCraftingRegisterEvent(tool).fire()) {
      toolForgeCrafting.add(tool);
    } else {
      log.debug("Registration of tool forge recipe " + tool.getRegistryName().toString() + " has been cancelled by event");
    }
  }

  public static Set<ToolCore> getToolForgeCrafting() {
    return ImmutableSet.copyOf(toolForgeCrafting);
  }

  public static void registerStencilTableCrafting(ItemStack stencil) {
    if(!(stencil.getItem() instanceof IPattern)) {
      log.fatal("Stencil Table Crafting has to be a pattern ({})", stencil);
      return;
    }
    if(new StencilTableCraftingRegisterEvent(stencil).fire()) {
      stencilTableCrafting.add(stencil);
    } else {
      log.debug("Registration of stencil table stencil " + stencil + " has been cancelled by event");
    }
  }

  public static List<ItemStack> getStencilTableCrafting() {
    return ImmutableList.copyOf(stencilTableCrafting);
  }

  public static void setShardItem(Shard shard) {
    if(shard == null) {
      return;
    }
    shardItem = shard;
  }

  public static Shard getShard() {
    return shardItem;
  }

  public static ItemStack getShard(Material material) {
    ItemStack out = material.getShard();
    if(out.isEmpty()) {
      out = shardItem.getItemstackWithMaterial(material);
    }
    return out;
  }

  public static void addPatternForItem(Item item) {
    patternItems.add(item);
  }

  public static void addCastForItem(Item item) {
    castItems.add(item);
  }

  public static Collection<Item> getPatternItems() {
    return ImmutableList.copyOf(patternItems);
  }

  public static Collection<Item> getCastItems() {
    return ImmutableList.copyOf(castItems);
  }

}
