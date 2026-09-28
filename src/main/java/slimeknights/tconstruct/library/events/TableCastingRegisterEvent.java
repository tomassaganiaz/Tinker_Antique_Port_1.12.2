package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

/** Register a casting table recipe. */
@Cancelable
public class TableCastingRegisterEvent extends TinkerRegisterEvent<ICastingRecipe> {

  public TableCastingRegisterEvent(ICastingRecipe recipe) {
    super(recipe);
  }
}