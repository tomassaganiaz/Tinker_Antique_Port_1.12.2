package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.DryingRecipe;

/** Register a drying rack recipe. */
@Cancelable
public class DryingRackRegisterEvent extends TinkerRegisterEvent<DryingRecipe> {

  public DryingRackRegisterEvent(DryingRecipe recipe) {
    super(recipe);
  }
}