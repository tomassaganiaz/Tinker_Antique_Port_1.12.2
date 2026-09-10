package slimeknights.tconstruct.plugin.jei.foundry;

import java.util.LinkedHashSet;
import java.util.Set;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class FoundryRecipeChecker {
  public static Set<MeltingRecipe> getFoundryRecipes() {
    Set<MeltingRecipe> set = new LinkedHashSet<>();
    for(MeltingRecipe r : TinkerRegistry.getAllMeltingRecipies()) {
      if(r.output != null && r.output.getFluid() != null && slimeknights.tconstruct.foundry.FoundryTierHelper.isTier4Fluid(r.output.getFluid())) {
        set.add(r);
      }
    }
    if(set.isEmpty()) set.addAll(TinkerRegistry.getAllMeltingRecipies());
    return set;
  }
}
