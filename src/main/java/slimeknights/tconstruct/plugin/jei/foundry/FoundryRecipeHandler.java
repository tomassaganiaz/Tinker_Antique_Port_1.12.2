package slimeknights.tconstruct.plugin.jei.foundry;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class FoundryRecipeHandler implements IRecipeHandler<MeltingRecipe> {
  @Override public Class<MeltingRecipe> getRecipeClass() { return MeltingRecipe.class; }
  @Override public String getRecipeCategoryUid(MeltingRecipe recipe) { return FoundryRecipeCategory.CATEGORY; }
  @Override public IRecipeWrapper getRecipeWrapper(MeltingRecipe recipe) { return new FoundryRecipeWrapper(recipe); }
  @Override public boolean isRecipeValid(MeltingRecipe recipe) { return true; }
}
