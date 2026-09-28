package slimeknights.tconstruct.library.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/** Register a recipe for melting an entity. */
@Cancelable
public class EntityMeltingRegisterEvent extends TinkerRegisterEvent<Class<? extends Entity>> {

  protected final FluidStack fluidStack;
  protected FluidStack newFluidStack;

  public EntityMeltingRegisterEvent(Class<? extends Entity> entity, FluidStack fluidStack) {
    super(entity);
    this.fluidStack = fluidStack;
    this.newFluidStack = fluidStack;
  }

  public void setNewFluidStack(FluidStack fluidStack) {
    this.newFluidStack = fluidStack;
  }

  public FluidStack getNewFluidStack() {
    return newFluidStack;
  }

  public FluidStack getFluidStack() {
    return fluidStack;
  }
}