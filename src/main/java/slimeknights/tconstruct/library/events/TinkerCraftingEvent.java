package slimeknights.tconstruct.library.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * These events are fired when the player crafts something using tinker blocks.
 * E.g. a pickaxe, a pattern, a toolpart,...
 * The events can be cancelled to prevent crafting. When doing so, it is advertised to give a localized
 * message to display to the player.
 */
@Cancelable
public class TinkerCraftingEvent extends TinkerEvent {

  private final ItemStack itemStack;
  private final EntityPlayer player;
  private String message;

  protected TinkerCraftingEvent(ItemStack itemStack, EntityPlayer player, String message) {
    this.itemStack = itemStack;
    this.player = player;

    message += "\n" + TextFormatting.ITALIC + "by " + Loader.instance().activeModContainer().getName();
    this.message = message;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public String getMessage() {
    return message;
  }

  public EntityPlayer getPlayer() {
    return player;
  }

  public void setCanceled(String localizedMessage) {
    this.message = localizedMessage;
    setCanceled(true);
  }
}