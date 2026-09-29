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


public class ToolModifyEvent extends TinkerCraftingEvent {
  private final List<IModifier> modifiers;
  private final ItemStack toolBeforeModification;

  protected ToolModifyEvent(ItemStack itemStack, EntityPlayer player, ItemStack toolBeforeModification) {
    super(itemStack, player, Util.translate("gui.error.craftevent.modifier.default"));
    this.toolBeforeModification = toolBeforeModification;

    List<IModifier> modifiers = TinkerUtil.getModifiers(itemStack);
    modifiers.removeAll(TinkerUtil.getModifiers(toolBeforeModification));

    this.modifiers = ImmutableList.copyOf(modifiers);
  }

  public List<IModifier> getModifiers() {
    return modifiers;
  }

  public ItemStack getToolBeforeModification() {
    return toolBeforeModification;
  }

  public static void fireEvent(ItemStack itemStack, EntityPlayer player, ItemStack toolBeforeModification) throws TinkerGuiException {
    ToolModifyEvent toolModifyEvent = new ToolModifyEvent(itemStack, player, toolBeforeModification);
    if(MinecraftForge.EVENT_BUS.post(toolModifyEvent)) {
      throw new TinkerGuiException(toolModifyEvent.getMessage());
    }
  }
}
