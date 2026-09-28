package slimeknights.tconstruct.plugin.jei.foundry;

import java.util.LinkedHashSet;
import java.util.Set;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class FoundryRecipeChecker {
  public static Set<MeltingRecipe> getFoundryRecipes() {
    // the foundry melts any item; only alloying is restricted to tier 4+, so show every melting recipe
    return new LinkedHashSet<>(TinkerRegistry.getAllMeltingRecipies());
  }
}
