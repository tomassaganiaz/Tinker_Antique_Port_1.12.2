package slimeknights.tconstruct.library.events; 
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;


public class StatRegisterEvent<T extends IMaterialStats> extends MaterialEvent {

  public final T stats;
  public T newStats;

  public StatRegisterEvent(Material material, T stats) {
    super(material);
    this.stats = stats;
  }

  public void overrideResult(T newStats) {
    if(!stats.getIdentifier().equals(newStats.getIdentifier())) {
      TinkerRegistry.log.error("StatRegisterEvent: New stats don't match old stats type. New is {}, old was {}",
                               newStats.getIdentifier(), stats.getIdentifier());
      return;
    }

    this.newStats = newStats;
    this.setResult(Result.ALLOW);
  }
}
