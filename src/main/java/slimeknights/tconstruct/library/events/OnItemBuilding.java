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


public class OnItemBuilding extends TinkerEvent {

  public NBTTagCompound tag;
  public final ImmutableList<Material> materials;
  public final TinkersItem tool;

  public OnItemBuilding(NBTTagCompound tag, ImmutableList<Material> materials, TinkersItem tool) {
    this.tag = tag;
    this.materials = materials;
    this.tool = tool;
  }

  public static OnItemBuilding fireEvent(NBTTagCompound tag, ImmutableList<Material> materials, TinkersItem tool) {
    OnItemBuilding event = new OnItemBuilding(tag, materials, tool);
    MinecraftForge.EVENT_BUS.post(event);
    return event;
  }
}
