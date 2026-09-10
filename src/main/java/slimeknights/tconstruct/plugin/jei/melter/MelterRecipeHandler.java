package slimeknights.tconstruct.plugin.jei.melter;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

public class MelterRecipeHandler implements IRecipeHandler<MeltingRecipe> {
  @Override public Class<MeltingRecipe> getRecipeClass() { return MeltingRecipe.class; }
  @Override public String getRecipeCategoryUid(MeltingRecipe recipe) { return MelterRecipeCategory.CATEGORY; }
  @Override public IRecipeWrapper getRecipeWrapper(MeltingRecipe recipe) { return new MelterRecipeWrapper(recipe); }
  @Override public boolean isRecipeValid(MeltingRecipe recipe) { return recipe.output != null && recipe.output.amount <= 576; }
}
