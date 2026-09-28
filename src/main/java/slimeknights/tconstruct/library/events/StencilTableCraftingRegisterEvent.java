package slimeknights.tconstruct.library.events;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/** Register a stencil table addition. */
@Cancelable
public class StencilTableCraftingRegisterEvent extends TinkerRegisterEvent<ItemStack> {

  public StencilTableCraftingRegisterEvent(ItemStack stencil) {
    super(stencil);
  }
}