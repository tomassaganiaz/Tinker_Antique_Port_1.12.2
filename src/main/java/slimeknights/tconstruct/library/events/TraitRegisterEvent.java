package slimeknights.tconstruct.library.events; 
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;


public class TraitRegisterEvent<T extends ITrait> extends MaterialEvent {

  public final T trait;

  public TraitRegisterEvent(Material material, T trait) {
    super(material);
    this.trait = trait;
  }
}
