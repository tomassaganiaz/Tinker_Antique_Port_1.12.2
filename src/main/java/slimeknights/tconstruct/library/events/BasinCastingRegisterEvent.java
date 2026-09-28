package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

/** Register a casting basin recipe. */
@Cancelable
public class BasinCastingRegisterEvent extends TinkerRegisterEvent<ICastingRecipe> {

  public BasinCastingRegisterEvent(ICastingRecipe recipe) {
    super(recipe);
  }
}