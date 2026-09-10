package slimeknights.tconstruct.tools.common.client;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.common.inventory.ContainerNewToolStation;
import slimeknights.tconstruct.tools.common.tileentity.TileNewToolStation;

public class GuiNewToolStation extends GuiToolStation {
  public GuiNewToolStation(InventoryPlayer inv, World world, BlockPos pos, TileNewToolStation tile) {
    super(inv, world, pos, tile);
  }
}
