package slimeknights.tconstruct.library.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/** Base event used when something is registered in the Tinker Registry. */
@Cancelable
public abstract class TinkerRegisterEvent<T> extends TinkerEvent {

  protected final T recipe;

  public TinkerRegisterEvent(T recipe) {
    this.recipe = recipe;
  }

  public T getRecipe() {
    return recipe;
  }

  /** Returns true on success, false if cancelled. */
  public boolean fire() {
    return !MinecraftForge.EVENT_BUS.post(this);
  }
}