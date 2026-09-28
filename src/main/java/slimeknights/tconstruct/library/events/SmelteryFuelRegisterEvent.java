package slimeknights.tconstruct.library.events;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/** Register a smeltery fuel. */
@Cancelable
public class SmelteryFuelRegisterEvent extends TinkerRegisterEvent<FluidStack> {

  private final int fuelDuration;

  public SmelteryFuelRegisterEvent(FluidStack recipe, int fuelDuration) {
    super(recipe);
    this.fuelDuration = fuelDuration;
  }

  public int getFuelDuration() {
    return fuelDuration;
  }
}