package slimeknights.tconstruct.library.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

import slimeknights.tconstruct.library.modifiers.IModifier;

/** Register a modifier. */
@Cancelable
public class ModifierRegisterEvent extends TinkerRegisterEvent<IModifier> {

  public ModifierRegisterEvent(IModifier recipe) {
    super(recipe);
  }
}