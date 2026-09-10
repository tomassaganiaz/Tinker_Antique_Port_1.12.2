package slimeknights.tconstruct.foundry.block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import slimeknights.tconstruct.foundry.tileentity.TileAlloyer;
public class BlockAlloyer extends BlockScorchedController {
  public BlockAlloyer() { super(); }
  @Nonnull @Override public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) { return new TileAlloyer(); }
}
