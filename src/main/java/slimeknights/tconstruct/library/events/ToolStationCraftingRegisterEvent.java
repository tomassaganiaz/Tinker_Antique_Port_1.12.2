package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.tools.ToolCore;

/** Register a tool station addition. */
@Cancelable
public class ToolStationCraftingRegisterEvent extends TinkerRegisterEvent<ToolCore> {

  public ToolStationCraftingRegisterEvent(ToolCore tool) {
    super(tool);
  }
}