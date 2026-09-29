package slimeknights.tconstruct.library.events; 
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import java.util.List;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.TinkerGuiException;
import slimeknights.tconstruct.library.utils.TinkerUtil;


public class ToolPartCraftingEvent extends TinkerCraftingEvent {

  private ToolPartCraftingEvent(ItemStack itemStack, EntityPlayer player) {
    super(itemStack, player, Util.translate("gui.error.craftevent.toolpart.default"));
  }

  public static void fireEvent(ItemStack itemStack, EntityPlayer player) throws TinkerGuiException {
    ToolPartCraftingEvent toolPartCraftingEvent = new ToolPartCraftingEvent(itemStack, player);
    if(MinecraftForge.EVENT_BUS.post(toolPartCraftingEvent)) {
      throw new TinkerGuiException(toolPartCraftingEvent.getMessage());
    }
  }
}
