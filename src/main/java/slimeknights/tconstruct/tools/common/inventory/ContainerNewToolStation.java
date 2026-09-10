package slimeknights.tconstruct.tools.common.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.common.tileentity.TileNewToolStation;

public class ContainerNewToolStation extends ContainerToolStation {
  private final java.util.Map<ItemStack, Material> matCache = new java.util.WeakHashMap<>();
  private Material getMatCached(ItemStack stack) {
    Material m = matCache.get(stack);
    if(m == null) {
      m = TinkerUtil.getMaterialFromStack(stack);
      matCache.put(stack.copy(), m);
    }
    return m;
  }
  public ContainerNewToolStation(InventoryPlayer inv, TileNewToolStation tile) {
    super(inv, tile);
    for(int i = 0; i < 4; i++) {
      final int idx = i;
      Slot s = new Slot(tile, i, 30 + idx*20, 20) {
        @Override public boolean isItemValid(ItemStack stack) {
          Material mat = getMatCached(stack);
          if(selectedTool == null) return !mat.equals(Material.UNKNOWN);
          if(idx >= selectedTool.getRequiredComponents().size()) return false;
          return selectedTool.getRequiredComponents().get(idx).isValidMaterial(mat);
        }
      };
      s.slotNumber = i;
      inventorySlots.set(i, s);
      inventoryItemStacks.set(i, ItemStack.EMPTY);
    }
  }
  @Override public boolean canInteractWith(EntityPlayer playerIn) { return tile.isUsableByPlayer(playerIn); }

  @Override
  public void onCraftMatrixChanged(IInventory inv) {
    try {
      ItemStack result = buildToolDirect();
      if(!result.isEmpty()) {
        out.inventory.setInventorySlotContents(0, result);
        updateGUI();
        return;
      }
    } catch(Exception e) {}
    super.onCraftMatrixChanged(inv);
  }

  private ItemStack buildToolDirect() {
    if(selectedTool == null) {
      ToolCore fallback = null;
      for(ToolCore t : TinkerRegistry.getToolStationCrafting()) {
        if(t.getRequiredComponents().size() > 4) continue;
        String path = t.getRegistryName() != null ? t.getRegistryName().getResourcePath() : "";
        if(path.startsWith("plate_")) { selectedTool = t; break; }
        if(fallback == null) fallback = t;
      }
      if(selectedTool == null) selectedTool = fallback;
      if(selectedTool == null) return ItemStack.EMPTY;
    }
    List<Material> mats = new ArrayList<>();
    for(int i = 0; i < selectedTool.getRequiredComponents().size(); i++) {
      ItemStack s = tile.getStackInSlot(i);
      if(s.isEmpty()) return ItemStack.EMPTY;
      Material m = getMatCached(s);
      if(m == null || m == Material.UNKNOWN) return ItemStack.EMPTY;
      if(!selectedTool.getRequiredComponents().get(i).isValidMaterial(m)) return ItemStack.EMPTY;
      mats.add(m);
    }
    return selectedTool.buildItem(mats);
  }
}
