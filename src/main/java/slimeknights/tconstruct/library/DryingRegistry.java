package slimeknights.tconstruct.library;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import slimeknights.tconstruct.library.events.DryingRackRegisterEvent;
import slimeknights.mantle.util.RecipeMatch;

import static slimeknights.tconstruct.library.TinkerRegistry.log;

/** 
Registro de recetas del drying rack.
 */
public final class 
DryingRegistry
 {

  private 
DryingRegistry
() {
  }

  private static List<DryingRecipe> dryingRegistry = new ObjectArrayList<>();

  public static List<DryingRecipe> getAllDryingRecipes() {
    return ImmutableList.copyOf(dryingRegistry);
  }

  public static void registerDryingRecipe(ItemStack input, ItemStack output, int time) {
    if(output.isEmpty() || input.isEmpty()) {
      return;
    }
    addDryingRecipe(new DryingRecipe(new RecipeMatch.Item(input, 1), output, time));
  }

  public static void registerDryingRecipe(Item input, ItemStack output, int time) {
    if(output.isEmpty() || input == null) {
      return;
    }

    ItemStack stack = new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE);
    addDryingRecipe(new DryingRecipe(new RecipeMatch.Item(stack, 1), output, time));
  }

  public static void registerDryingRecipe(Item input, Item output, int time) {
    if(output == null || input == null) {
      return;
    }

    ItemStack stack = new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE);
    addDryingRecipe(new DryingRecipe(new RecipeMatch.Item(stack, 1), new ItemStack(output), time));
  }

  public static void registerDryingRecipe(Block input, Block output, int time) {
    if(output == null || input == null) {
      return;
    }

    ItemStack stack = new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE);
    addDryingRecipe(new DryingRecipe(new RecipeMatch.Item(stack, 1), new ItemStack(output), time));
  }

  public static void registerDryingRecipe(String oredict, ItemStack output, int time) {
    if(output.isEmpty() || oredict == null) {
      return;
    }

    addDryingRecipe(new DryingRecipe(new RecipeMatch.Oredict(oredict, 1), output, time));
  }

  public static void addDryingRecipe(DryingRecipe recipe) {
    if(new DryingRackRegisterEvent(recipe).fire()) {
      dryingRegistry.add(recipe);
    }
    else {
      try {
        String input = recipe.input.getInputs().stream().findFirst().map(ItemStack::getUnlocalizedName).orElse("?");
        String output = recipe.getResult().getUnlocalizedName();
        log.debug("Registration of drying rack recipe for " + output + " from " + input + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging drying rack event", e);
      }
    }
  }

  public static int getDryingTime(ItemStack input) {
    for(DryingRecipe r : dryingRegistry) {
      if(r.matches(input)) {
        return r.getTime();
      }
    }

    return -1;
  }

  public static ItemStack getDryingResult(ItemStack input) {
    for(DryingRecipe r : dryingRegistry) {
      if(r.matches(input)) {
        return r.getResult();
      }
    }

    return ItemStack.EMPTY;
  }

}
