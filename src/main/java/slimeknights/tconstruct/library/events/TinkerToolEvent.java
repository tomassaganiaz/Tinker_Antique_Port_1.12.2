package slimeknights.tconstruct.library.events;

import net.minecraft.item.ItemStack;

import slimeknights.tconstruct.library.tools.ToolCore;

public abstract class TinkerToolEvent extends TinkerEvent {

  public final ItemStack itemStack;
  public final ToolCore tool;

  public TinkerToolEvent(ItemStack itemStack) {
    this.itemStack = itemStack;
    this.tool = (ToolCore) itemStack.getItem();
  }
}