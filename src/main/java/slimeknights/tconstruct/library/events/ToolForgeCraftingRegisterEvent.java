package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.tools.ToolCore;

/** Register a tool forge addition. */
@Cancelable
public class ToolForgeCraftingRegisterEvent extends TinkerRegisterEvent<ToolCore> {

  public ToolForgeCraftingRegisterEvent(ToolCore tool) {
    super(tool);
  }
}