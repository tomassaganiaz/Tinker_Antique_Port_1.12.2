package slimeknights.tconstruct.debug;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.tconstruct.library.events.TinkerCraftingEvent;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.library.events.ToolCraftingEvent;
import slimeknights.tconstruct.library.events.ToolModifyEvent;
import slimeknights.tconstruct.library.events.ToolPartCraftingEvent;
import slimeknights.tconstruct.library.events.ToolPartReplaceEvent;
import slimeknights.tconstruct.tools.harvest.TinkerHarvestTools;

//@Mod.EventBusSubscriber
public class EventTesting {

  @SubscribeEvent
  public static void onToolCraft(ToolCraftingEvent event) {
    if(event.getItemStack().getItem() == TinkerHarvestTools.hammer) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onToolPartCraft(ToolPartCraftingEvent event) {
    if(event.getItemStack().getItem() == TinkerTools.arrowHead) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onToolModify(ToolModifyEvent event) {
    if(event.getModifiers().contains(TinkerModifiers.modDiamond)) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onToolPartReplace(ToolPartReplaceEvent event) {
    ItemStack firstStack = event.getToolParts().get(0);
    if(firstStack.getItem() instanceof ToolPart) {
      if(((ToolPart) firstStack.getItem()).getMaterial(firstStack).equals(TinkerMaterials.ardite)) {
        event.setCanceled(true);
      }
    }
  }
}
