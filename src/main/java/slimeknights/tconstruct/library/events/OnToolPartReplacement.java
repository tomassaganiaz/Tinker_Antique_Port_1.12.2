package slimeknights.tconstruct.library.events; 
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import java.util.List;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.TinkersItem;


public class OnToolPartReplacement extends TinkerEvent {

  /** The items in the tool station. Can be manipulated. */
  public NonNullList<ItemStack> replacementParts;
  public ItemStack toolStack;

  public OnToolPartReplacement(NonNullList<ItemStack> replacementParts, ItemStack toolStack) {
    this.replacementParts = replacementParts;
    this.toolStack = toolStack.copy();
  }

  public static boolean fireEvent(NonNullList<ItemStack> replacementParts, ItemStack toolStack) {
    return !MinecraftForge.EVENT_BUS.post(new OnToolPartReplacement(replacementParts, toolStack));
  }
}
