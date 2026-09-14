package slimeknights.tconstruct.tools.modifiers;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import slimeknights.mantle.util.RecipeMatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReinforcementRecipeMatch extends RecipeMatch {
  private final List<ItemStack> inputs;

  public ReinforcementRecipeMatch(ItemStack... inputs) {
    super(1, 0);
    List<ItemStack> copies = new ArrayList<>();
    for(ItemStack input : inputs) copies.add(input.copy());
    this.inputs = copies;
  }

  @Override
  public List<ItemStack> getInputs() {
    return ImmutableList.copyOf(inputs);
  }

  @Override
  public Optional<Match> matches(NonNullList<ItemStack> inventory) {
    List<ItemStack> matched = new ArrayList<>();
    Set<Integer> available = new HashSet<>();
    for(int i = 0; i < inventory.size(); i++) {
      if(!inventory.get(i).isEmpty()) available.add(i);
    }

    for(ItemStack input : inputs) {
      Integer matchedSlot = null;
      for(Integer slot : available) {
        ItemStack stack = inventory.get(slot);
        if(stack.getCount() >= input.getCount()
            && ItemStack.areItemsEqual(input, stack)
            && ItemStack.areItemStackTagsEqual(input, stack)) {
          matchedSlot = slot;
          break;
        }
      }
      if(matchedSlot == null) return Optional.empty();

      ItemStack consumed = inventory.get(matchedSlot).copy();
      consumed.setCount(input.getCount());
      matched.add(consumed);
      available.remove(matchedSlot);
    }
    return Optional.of(new Match(matched, amountMatched));
  }
}