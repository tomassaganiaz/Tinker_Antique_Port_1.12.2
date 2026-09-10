package slimeknights.tconstruct.tools.common.tileentity;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.TagUtil;
import java.util.ArrayList;
import java.util.List;
import slimeknights.mantle.common.IInventoryGui;

public class TileNewToolStation extends TileToolStation implements IInventoryGui {

  public List<Material> getSelectedMaterials() {
    List<Material> mats = new ArrayList<>();
    for(int i = 0; i < getSizeInventory(); i++) {
      if(!getStackInSlot(i).isEmpty()) {
        String id = TagUtil.getBaseMaterialsTagList(getStackInSlot(i).getTagCompound()).toString();
      }
    }
    return mats;
  }

  @Override
  public Container createContainer(InventoryPlayer inv, World world, BlockPos pos) {
    return new slimeknights.tconstruct.tools.common.inventory.ContainerNewToolStation(inv, this);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public GuiContainer createGui(InventoryPlayer inv, World world, BlockPos pos) {
    return new slimeknights.tconstruct.tools.common.client.GuiNewToolStation(inv, world, pos, this);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) { super.readFromNBT(tag); }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tag) { return super.writeToNBT(tag); }
}
