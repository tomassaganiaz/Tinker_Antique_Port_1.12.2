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


public class ToolPartReplaceEvent extends TinkerCraftingEvent {

  private final NonNullList<ItemStack> toolParts;

  private ToolPartReplaceEvent(ItemStack itemStack, EntityPlayer player, NonNullList<ItemStack> toolParts) {
    super(itemStack, player, Util.translate("gui.error.craftevent.replace.default"));
    this.toolParts = toolParts;
  }

  public NonNullList<ItemStack> getToolParts() {
    return toolParts;
  }

  public static void fireEvent(ItemStack itemStack, EntityPlayer player, NonNullList<ItemStack> toolParts) throws TinkerGuiException {
    ToolPartReplaceEvent toolPartReplaceEvent = new ToolPartReplaceEvent(itemStack, player, toolParts);
    if(MinecraftForge.EVENT_BUS.post(toolPartReplaceEvent)) {
      throw new TinkerGuiException(toolPartReplaceEvent.getMessage());
    }
  }
}
