package slimeknights.tconstruct.foundry.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.foundry.FoundryTierHelper;
import slimeknights.tconstruct.foundry.TinkerScorched;
import slimeknights.tconstruct.foundry.multiblock.MultiblockScorched;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.smeltery.multiblock.MultiblockDetection;
import slimeknights.tconstruct.smeltery.network.HeatingStructureFuelUpdatePacket;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;
import slimeknights.tconstruct.smeltery.tileentity.TileTank;

/**
 * Scorched foundry multiblock controller.
 * <p>
 * Functionally a smeltery that only accepts blazing blood as fuel and never creates alloys.
 */
public class TileScorchedFoundry extends TileSmeltery {

  public TileScorchedFoundry() {
    super();
    this.inventoryTitle = "gui.foundry.name";
    setMultiblock(new MultiblockScorched(this));
  }

  @Override
  protected void updateStructureInfo(MultiblockDetection.MultiblockStructure structure) {
    // find all scorched tanks for fuel input, the base implementation only looks for seared tanks
    tanks.clear();
    for(BlockPos pos : structure.blocks) {
      if(getWorld().getBlockState(pos).getBlock() == TinkerScorched.scorchedTank) {
        tanks.add(pos);
      }
    }

    int inventorySize = getUpdatedInventorySize(structure.xd, structure.yd, structure.zd);

    // if the new multiblock is smaller we pop out all items that don't fit in anymore
    if(!getWorld().isRemote && this.getSizeInventory() > inventorySize) {
      for(int i = inventorySize; i < getSizeInventory(); i++) {
        if(!getStackInSlot(i).isEmpty()) {
          dropItem(getStackInSlot(i));
        }
      }
    }

    // adjust inventory sizes
    this.resize(inventorySize);

    this.liquids.setCapacity(getSizeInventory() * CAPACITY_PER_BLOCK);
  }

  /** The foundry only creates tier 4+ alloys. */
  @Override
  protected void alloyAlloys() {
    FoundryTierHelper.alloyAlloysForTier(liquids, true);
  }

  @Override
  protected void consumeFuel() {
    // no need to consume fuel
    if(hasFuel()) {
      return;
    }

    // keep the current tank if it still holds fuel, otherwise search the others
    if(currentTank == null || !hasBlazeBlood(currentTank)) {
      currentTank = null;
      for(BlockPos pos : tanks) {
        if(hasBlazeBlood(pos)) {
          currentTank = pos;
          break;
        }
      }
    }

    // got a tank?
    if(currentTank != null) {
      TileEntity te = getWorld().getTileEntity(currentTank);
      if(te instanceof TileTank) {
        IFluidTank tank = ((TileTank) te).getInternalTank();

        FluidStack liquid = tank.getFluid();
        // only blazing blood is valid foundry fuel
        if(liquid != null && liquid.getFluid() == TinkerFluids.blazingBlood) {
          FluidStack in = liquid.copy();
          int bonusFuel = TinkerRegistry.consumeSmelteryFuel(in);
          int amount = liquid.amount - in.amount;
          FluidStack drained = tank.drain(amount, false);

          // we can drain. actually drain and add the fuel
          if(drained != null && drained.amount == amount) {
            tank.drain(amount, true);
            currentFuel = drained.copy();
            fuelQuality = bonusFuel;
            addFuel(bonusFuel, drained.getFluid().getTemperature(drained) - 300); // convert to degree celcius

            // notify client of fuel/temperature changes
            if(isServerWorld()) {
              TinkerNetwork.sendToAll(new HeatingStructureFuelUpdatePacket(pos, currentTank, temperature, currentFuel));
            }

            return;
          }
        }

        fuelQuality = 0;
      }
    }
  }

  /**
   * Can be used by the GUI to determine fuel percentage, restricted to blazing blood
   */
  @SideOnly(Side.CLIENT)
  @Override
  public FuelInfo getFuelDisplay() {
    FuelInfo info = new FuelInfo();

    // we still have leftover fuel
    if(hasFuel()) {
      if(currentFuel == null) {
        info.fluid = new FluidStack(TinkerFluids.blazingBlood, 0);
        info.maxCap = 1;
      } else {
        info.fluid = currentFuel.copy();
        info.fluid.amount = 0;
        info.maxCap = currentFuel.amount;
      }
      info.heat = this.temperature + 300;
    }
    else {
      // no fuel yet, look for blazing blood in the tanks
      for(BlockPos pos : tanks) {
        IFluidTank tank = getScorchedTank(pos);
        if(tank != null && tank.getFluidAmount() > 0 && tank.getFluid() != null && tank.getFluid().getFluid() == TinkerFluids.blazingBlood) {
          info.fluid = tank.getFluid().copy();
          info.heat = info.fluid.getFluid().getTemperature(info.fluid);
          info.maxCap = tank.getCapacity();
          break;
        }
      }
    }

    return info;
  }

  // checks if the given location has a fluid tank that contains blazing blood
  private boolean hasBlazeBlood(BlockPos pos) {
    IFluidTank tank = getScorchedTank(pos);
    return tank != null && tank.getFluidAmount() > 0 && tank.getFluid() != null && tank.getFluid().getFluid() == TinkerFluids.blazingBlood;
  }

  private IFluidTank getScorchedTank(BlockPos pos) {
    TileEntity te = getWorld().getTileEntity(pos);
    if(te instanceof TileTank) {
      return ((TileTank) te).getInternalTank();
    }

    return null;
  }
}