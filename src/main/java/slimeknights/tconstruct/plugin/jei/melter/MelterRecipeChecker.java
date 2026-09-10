package slimeknights.tconstruct.plugin.jei.melter;

import java.util.LinkedHashSet;
import java.util.Set;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class MelterRecipeChecker {
  public static Set<MeltingRecipe> getMelterRecipes() {
    Set<MeltingRecipe> set = new LinkedHashSet<>();
    for(MeltingRecipe r : TinkerRegistry.getAllMeltingRecipies()) {
      if(r.output != null && r.getUsableTemperature() <= 550) set.add(r);
    }
    return set;
  }
}
