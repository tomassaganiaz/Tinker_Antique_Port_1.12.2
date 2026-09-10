package slimeknights.tconstruct.plugin.jei.melter;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class MelterRecipeWrapper implements IRecipeWrapper {
  private final MeltingRecipe recipe;
  public MelterRecipeWrapper(MeltingRecipe r) { this.recipe = r; }
  @Override public void getIngredients(IIngredients ingredients) {
    ingredients.setInputs(net.minecraft.item.ItemStack.class, recipe.input.getInputs());
    ingredients.setOutputs(FluidStack.class, ImmutableList.of(recipe.output));
  }
}
