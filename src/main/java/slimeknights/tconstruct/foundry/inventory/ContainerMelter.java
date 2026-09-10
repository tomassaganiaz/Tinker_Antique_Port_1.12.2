package slimeknights.tconstruct.foundry.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;

import slimeknights.mantle.inventory.ContainerMultiModule;
import slimeknights.tconstruct.foundry.tileentity.TileMelter;

public class ContainerMelter extends ContainerMultiModule<TileMelter> {

  protected int oldFuel;
  protected int oldTemp;
  protected int oldTempRequired;

  public ContainerMelter(InventoryPlayer inventoryPlayer, TileMelter tile) {
    super(tile);

    // input and fuel slot
    addSlotToContainer(new Slot(tile, TileMelter.INPUT, 56, 17));
    addSlotToContainer(new Slot(tile, TileMelter.FUEL, 56, 53));

    // player stuffs
    addPlayerInventory(inventoryPlayer, 8, 84);

    oldFuel = 0;
    oldTemp = 0;
    oldTempRequired = 0;
  }

  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);

    listener.sendWindowProperty(this, 0, tile.getFuel());
    listener.sendWindowProperty(this, 1, tile.getTemperature(TileMelter.INPUT));
    listener.sendWindowProperty(this, 2, tile.getTempRequired(TileMelter.INPUT));
  }

  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();

    // changed fuel data
    int fuel = tile.getFuel();
    if(fuel != oldFuel) {
      oldFuel = fuel;
      for(IContainerListener crafter : this.listeners) {
        crafter.sendWindowProperty(this, 0, fuel);
      }
    }

    // changed heat data
    int temp = tile.getTemperature(TileMelter.INPUT);
    if(temp != oldTemp) {
      oldTemp = temp;
      for(IContainerListener crafter : this.listeners) {
        crafter.sendWindowProperty(this, 1, temp);
      }
    }
    temp = tile.getTempRequired(TileMelter.INPUT);
    if(temp != oldTempRequired) {
      oldTempRequired = temp;
      for(IContainerListener crafter : this.listeners) {
        crafter.sendWindowProperty(this, 2, temp);
      }
    }
  }

  @Override
  public void updateProgressBar(int id, int data) {
    if(id == 0) {
      tile.updateFuelFromPacket(0, data);
    }
    else if(id == 1) {
      tile.updateTemperatureFromPacket(TileMelter.INPUT, data);
    }
    else if(id == 2) {
      tile.updateTempRequiredFromPacket(TileMelter.INPUT, data);
    }
  }
}