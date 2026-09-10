package slimeknights.tconstruct.foundry.block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import slimeknights.tconstruct.foundry.tileentity.TileHeater;
public class BlockHeater extends BlockMelter {
  public BlockHeater() { super(); }
  @Nonnull @Override public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) { return new TileHeater(); }
}
