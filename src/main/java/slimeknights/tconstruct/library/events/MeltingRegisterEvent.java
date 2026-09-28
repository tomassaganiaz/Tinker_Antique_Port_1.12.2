package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

/** Register a recipe for melting something in the smeltery. */
@Cancelable
public class MeltingRegisterEvent extends TinkerRegisterEvent<MeltingRecipe> {

  public MeltingRegisterEvent(MeltingRecipe recipe) {
    super(recipe);
  }
}