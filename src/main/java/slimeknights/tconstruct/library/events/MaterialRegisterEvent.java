package slimeknights.tconstruct.library.events; 
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;


public class MaterialRegisterEvent extends MaterialEvent {

  public MaterialRegisterEvent(Material material) {
    super(material);
  }
}
