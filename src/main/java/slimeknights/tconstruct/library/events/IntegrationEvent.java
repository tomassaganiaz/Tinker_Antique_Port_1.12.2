package slimeknights.tconstruct.library.events; 
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;


public class IntegrationEvent extends MaterialEvent {

  public final MaterialIntegration materialIntegration;

  public IntegrationEvent(Material material, MaterialIntegration materialIntegration) {
    super(material);
    this.materialIntegration = materialIntegration;
  }
}
