package slimeknights.tconstruct.foundry.tileentity;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import slimeknights.mantle.common.IInventoryGui;
import slimeknights.mantle.tileentity.TileInventory;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.sound.SoundFading;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.ISmelteryTankHandler;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.smeltery.SmelteryTank;
import slimeknights.tconstruct.library.sound.ISoundSource;
import slimeknights.tconstruct.library.sound.SoundType;
import slimeknights.tconstruct.library.utils.FluidUtil;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.network.ITileInfoPacketHandler;
import slimeknights.tconstruct.smeltery.network.SmelteryFluidUpdatePacket;
import slimeknights.tconstruct.smeltery.network.TileProcessingPacket;
import slimeknights.tconstruct.foundry.client.GuiMelter;
import slimeknights.tconstruct.foundry.inventory.ContainerMelter;

/**
 * Single block melter. Smelts a single item at a time into a small internal tank, powered by solid fuels.
 * Can only reach a limited temperature, so high-tier materials keep needing the smeltery/foundry.
 */
public class TileMelter extends TileInventory implements ITickable, IInventoryGui, ISmelteryTankHandler, ITileInfoPacketHandler, ISoundSource, net.minecraft.inventory.ISidedInventory {

  public static final int INPUT = 0;
  public static final int FUEL = 1;

  /** Capacity of the internal tank, 4 ingots worth of liquid */
  public static final int MELTER_CAPACITY = Material.VALUE_Ingot * 4;
  /** Maximum heat the melter can reach with solid fuels, everything above needs the smeltery/foundry */
  public static final int MAX_TEMPERATURE = 550;

  // NBT tags, keeping the names from TileHeatingStructure
  public static final String TAG_FUEL = "fuel";
  public static final String TAG_TEMPERATURE = "temperature";
  public static final String TAG_NEEDS_FUEL = "needsFuel";
  public static final String TAG_ITEM_TEMPERATURES = "itemTemperatures";
  public static final String TAG_ITEM_TEMP_REQUIRED = "itemTempRequired";
  public static final String TAG_IS_HEATING = "isHeating";

  // basically an "accuracy" so the heat can be more fine grained, required temp is multiplied by this
  protected static final int TIME_FACTOR = 8;

  // Ticks left until the current fuel is depleted, depletes every tick
  protected int fuel;
  // internal temperature of the melter == speed of the heater
  protected int temperature;
  // If the last tick executed an operation that required fuel
  protected boolean needsFuel;
  public boolean isHeating;

  // current temperature of the item in the slot / where it wants to go
  protected int[] itemTemperatures = new int[2];
  protected int[] itemTempRequired = new int[2];

  // the internal tank
  protected SmelteryTank liquids;

  public TileMelter() {
    super("gui.melter.name", 2, 1);
    liquids = new SmelteryTank(this);
    liquids.setCapacity(MELTER_CAPACITY);
  }

  @Override
  public void update() {
    if(world != null && world.isRemote) {
      return;
    }

    // heat the item, this also updates the needsFuel flag so we only consume fuel when needed
    heatItems();

    if(needsFuel) {
      consumeFuel();
    }
  }

  /* Heating & melting */

  protected void heatItems() {
    boolean heatedItem = false;
    for(int i = 0; i < getSizeInventory(); i++) {
      ItemStack stack = getStackInSlot(i);
      if(!stack.isEmpty()) {
        // heat item if possible
        if(itemTempRequired[i] > 0) {
          // fuel is present, turn up the heat
          if(hasFuel()) {
            // if the temperature is high enough for the slot
            if(canHeat(i)) {
              // are we done heating?
              if(itemTemperatures[i] >= itemTempRequired[i]) {
                if(onItemFinishedHeating(stack, i)) {
                  itemTemperatures[i] = 0;
                  itemTempRequired[i] = 0;
                }
              }
              // otherwise turn up the heat
              else {
                itemTemperatures[i] += heatSlot(i);
                heatedItem = true;
              }
            }
          }
          else {
            // can't heat. no fuel. abort and try to get fuel for next tick
            this.needsFuel = true;
            break;
          }
        }
      }
      else {
        itemTemperatures[i] = 0;
      }
    }

    if(heatedItem) {
      fuel--;
    }
    updateIfChanged(heatedItem);
  }

  protected int heatSlot(int i) {
    // solid fuel burning gives the melter a bit of a boost over the smelteries fuel ticks
    return temperature / 40;
  }

  public boolean canHeat(int index) {
    return temperature >= getHeatRequiredForSlot(index);
  }

  public float getProgress(int index) {
    if(index >= itemTemperatures.length) {
      return 0f;
    }
    return (float) itemTemperatures[index] / (float) itemTempRequired[index];
  }

  protected void setHeatRequiredForSlot(int index, int heat) {
    if(index < itemTempRequired.length) {
      itemTempRequired[index] = heat * TIME_FACTOR;
    }
  }

  protected int getHeatRequiredForSlot(int index) {
    if(index >= itemTempRequired.length) {
      return 0;
    }
    return itemTempRequired[index] / TIME_FACTOR;
  }

  protected void updateHeatRequiredForSlot(int index) {
    if(index == INPUT) {
      ItemStack stack = getStackInSlot(INPUT);
      if(!stack.isEmpty()) {
        MeltingRecipe melting = TinkerRegistry.getMelting(stack);
        if(melting != null && melting.getUsableTemperature() <= MAX_TEMPERATURE) {
          setHeatRequiredForSlot(index, Math.max(5, melting.getUsableTemperature()));

          // instantly consume fuel if required
          if(!hasFuel()) {
            consumeFuel();
          }
          return;
        }
      }
    }

    setHeatRequiredForSlot(index, 0);
  }

  protected boolean onItemFinishedHeating(ItemStack stack, int slot) {
    // skip if full, as there is no case where we can melt an item into a full melter
    if(liquids.getFluidAmount() >= liquids.getCapacity()) {
      // set error state for the UI
      itemTemperatures[slot] = itemTempRequired[slot] * 2 + 1;
      return false;
    }
    MeltingRecipe recipe = TinkerRegistry.getMelting(stack);

    if(recipe == null) {
      return false;
    }

    FluidStack fluidStack = FluidUtil.getValidFluidStackOrNull(recipe.output.copy());
    int filled = liquids.fill(fluidStack, false);

    if(filled == fluidStack.amount) {
      liquids.fill(fluidStack, true);

      // only clear out the item if it was successful
      setInventorySlotContents(slot, ItemStack.EMPTY);
      return true;
    }
    else {
      // can't fill into the melter, set error state
      itemTemperatures[slot] = itemTempRequired[slot] * 2 + 1;
    }

    return false;
  }

  /* Fuel */

  protected void consumeFuel() {
    // no need to consume fuel
    if(hasFuel()) {
      needsFuel = false;
      return;
    }

    // solid fuel from the fuel slot
    ItemStack fuelStack = getStackInSlot(FUEL);
    if(!fuelStack.isEmpty()) {
      int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);
      if(burnTime > 0) {
        addFuel(burnTime, MAX_TEMPERATURE);
        fuelStack.shrink(1);
        if(fuelStack.isEmpty()) {
          setInventorySlotContents(FUEL, ItemStack.EMPTY);
        }
        return;
      }
    }

    needsFuel = true;
  }

  protected void addFuel(int fuel, int newTemperature) {
    this.fuel += fuel;
    this.needsFuel = false;
    this.temperature = newTemperature;
  }

  public boolean hasFuel() {
    return fuel > 0;
  }

  protected void updateIfChanged(boolean heatedItem) {
    if(heatedItem != isHeating) {
      isHeating = heatedItem;
      sendTileInfo(world, pos);
    }
  }

  /* Inventory */

  @Override
  public void setInventorySlotContents(int slot, @Nonnull ItemStack itemstack) {
    // reset heat if set to null or a different item
    if(itemstack.isEmpty() || (!getStackInSlot(slot).isEmpty() && !ItemStack.areItemStacksEqual(itemstack, getStackInSlot(slot)))) {
      itemTemperatures[slot] = 0;
    }
    super.setInventorySlotContents(slot, itemstack);

    // when an item gets added, check for its heat required
    updateHeatRequiredForSlot(slot);
  }

  @Override
  public boolean isItemValidForSlot(int slot, @Nonnull ItemStack itemstack) {
    if(slot == INPUT) {
      return hasMeltableRecipe(itemstack);
    }
    if(slot == FUEL) {
      return TileEntityFurnace.isItemFuel(itemstack);
    }
    return super.isItemValidForSlot(slot, itemstack);
  }

  private boolean hasMeltableRecipe(ItemStack stack) {
    if(!stack.isEmpty()) {
      MeltingRecipe melting = TinkerRegistry.getMelting(stack);
      if(melting != null) {
        return melting.getUsableTemperature() <= MAX_TEMPERATURE;
      }
    }
    return false;
  }

  /* GUI */

  @Override
  public Container createContainer(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
    return new ContainerMelter(inventoryplayer, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainer createGui(InventoryPlayer inventoryplayer, World world, BlockPos pos) {
    return new GuiMelter((ContainerMelter) createContainer(inventoryplayer, world, pos), this);
  }

  /* Fluid handling */

  @Nullable
  public SmelteryTank getTank() {
    return liquids;
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return true;
    }
    if(capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return false;
    }
    return super.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Nonnull
  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      if(facing == null || facing == EnumFacing.DOWN || facing != EnumFacing.UP) {
        return (T) liquids;
      }
      return (T) liquids;
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if(side == EnumFacing.UP) {
      return new int[]{INPUT};
    }
    if(side == EnumFacing.DOWN) {
      return new int[]{};
    }
    return new int[]{FUEL};
  }

  @Override
  public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
    if(index == INPUT && direction == EnumFacing.UP) {
      return isItemValidForSlot(index, stack);
    }
    if(index == FUEL && direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
      return isItemValidForSlot(index, stack);
    }
    return false;
  }

  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return false;
  }

  @Override
  public void onTankChanged(List<FluidStack> fluids, FluidStack changed) {
    // notify clients of liquid changes, the null check is to prevent potential crashes during loading
    if(world != null && !world.isRemote) {
      TinkerNetwork.sendToAll(new SmelteryFluidUpdatePacket(pos, fluids));
    }
    // tell the chunk the tank changed
    this.markDirtyFast();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void updateFluidsFromPacket(List<FluidStack> fluids) {
    this.liquids.setFluids(fluids);
  }

  /* Networking */

  @Override
  public TileProcessingPacket getTileInfoPacket() {
    return new TileProcessingPacket(pos, isHeating);
  }

  @Override
  public void onTileInfoPacket(TileProcessingPacket packet) {
    boolean wasHeating = isHeating;
    isHeating = packet.isProcessing;

    if(!wasHeating && isHeating) {
      TinkerSmeltery.proxy.playSound(getSound());
    }
  }

  public int getFuel() {
    return fuel;
  }

  @SideOnly(Side.CLIENT)
  public void updateFuelFromPacket(int index, int fuel) {
    if(index == 0) {
      this.fuel = fuel;
    }
  }

  @SideOnly(Side.CLIENT)
  public void updateTemperatureFromPacket(int index, int heat) {
    if(index < 0 || index > getSizeInventory() - 1) {
      return;
    }
    itemTemperatures[index] = heat;
  }

  @SideOnly(Side.CLIENT)
  public void updateTempRequiredFromPacket(int index, int heat) {
    if(index < 0 || index > getSizeInventory() - 1) {
      return;
    }
    itemTempRequired[index] = heat;
  }

  public int getTemperature(int i) {
    if(i < 0 || i >= itemTemperatures.length) {
      return 0;
    }
    return itemTemperatures[i];
  }

  public int getTempRequired(int i) {
    if(i < 0 || i >= itemTempRequired.length) {
      return 0;
    }
    return itemTempRequired[i];
  }

  /* Sound */

  @Override
  public SoundType getSoundType() {
    return SoundType.HEATING_STRUCTURE;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ISound getSound() {
    return new SoundFading(this, pos);
  }

  @Override
  public boolean shouldPlaySound() {
    return !this.isInvalid() && this.isHeating;
  }

  /* Loading and saving */

  @Override
  public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
    boolean wasHeating = isHeating;
    readFromNBT(tag);

    // check if the sound should be played on load
    if(!wasHeating && isHeating) {
      TinkerSmeltery.proxy.playSound(getSound());
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags = super.writeToNBT(tags);
    tags.setInteger(TAG_FUEL, fuel);
    tags.setInteger(TAG_TEMPERATURE, temperature);
    tags.setBoolean(TAG_NEEDS_FUEL, needsFuel);
    tags.setIntArray(TAG_ITEM_TEMPERATURES, itemTemperatures);
    tags.setIntArray(TAG_ITEM_TEMP_REQUIRED, itemTempRequired);
    tags.setBoolean(TAG_IS_HEATING, isHeating);
    return tags;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    fuel = tags.getInteger(TAG_FUEL);
    temperature = tags.getInteger(TAG_TEMPERATURE);
    needsFuel = tags.getBoolean(TAG_NEEDS_FUEL);
    itemTemperatures = tags.getIntArray(TAG_ITEM_TEMPERATURES);
    itemTempRequired = tags.getIntArray(TAG_ITEM_TEMP_REQUIRED);
    isHeating = tags.getBoolean(TAG_IS_HEATING);
  }
}