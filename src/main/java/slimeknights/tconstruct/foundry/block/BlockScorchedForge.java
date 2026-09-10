package slimeknights.tconstruct.foundry.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.tools.common.block.BlockToolForge;
import slimeknights.tconstruct.tools.common.tileentity.TileToolForge;

public class BlockScorchedForge extends BlockToolForge {

  public BlockScorchedForge() {
    super();
    this.setCreativeTab(TinkerRegistry.tabSmeltery);
    this.setSoundType(SoundType.METAL);
    this.setHardness(4F);
    this.setResistance(25F);
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileToolForge();
  }
}
