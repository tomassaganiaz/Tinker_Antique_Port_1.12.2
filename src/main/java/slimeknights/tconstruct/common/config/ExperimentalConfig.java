package slimeknights.tconstruct.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import static slimeknights.tconstruct.common.config.Config.*;

/** Carga la categoria "Experimental" del config. */
public final class ExperimentalConfig {

  private ExperimentalConfig() {
  }

  public static void loadConfig(Configuration configFile) {
    Property prop;

      String cat = "experimental";
      Experimental = configFile.getCategory(cat);

      prop = configFile.get(cat, "vanillaToolBreaking", vanillaToolBreaking);
      prop.setComment("[EXPERIMENTAL] If true, tools will be fully destroyed like vanilla tools when durability is depleted. You monster!");
      vanillaToolBreaking = prop.getBoolean();

      prop = configFile.get(cat, "jeiGuidebookButton", jeiGuidebookButton);
      prop.setComment("[EXPERIMENTAL] If true, a button is added to JEI material pages that opens the 'Materials and You' book at the approximate location.");
      jeiGuidebookButton = prop.getBoolean();

      prop = configFile.get(cat, "repairToolsOnAnvils", repairToolsOnAnvils);
      prop.setComment("[EXPERIMENTAL] If tools can be repaired or upgraded on anvils like vanilla equipment.");
      repairToolsOnAnvils = prop.getBoolean();
  }
}
