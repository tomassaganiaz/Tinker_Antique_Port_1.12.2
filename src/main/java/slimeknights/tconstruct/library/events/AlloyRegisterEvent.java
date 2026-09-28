package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

/** Register a recipe for alloying multiple liquids. */
@Cancelable
public class AlloyRegisterEvent extends TinkerRegisterEvent<AlloyRecipe> {

  public AlloyRegisterEvent(AlloyRecipe recipe) {
    super(recipe);
  }
}