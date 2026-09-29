package slimeknights.tconstruct.library.events;

import slimeknights.tconstruct.library.materials.Material;

public abstract class MaterialEvent extends TinkerEvent {

  public final Material material;

  public MaterialEvent(Material material) {
    this.material = material;
  }
}