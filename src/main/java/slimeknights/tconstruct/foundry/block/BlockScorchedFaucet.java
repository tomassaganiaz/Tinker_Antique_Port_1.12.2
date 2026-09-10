package slimeknights.tconstruct.foundry.block;
import javax.annotation.Nonnull;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.smeltery.block.BlockFaucet;
import slimeknights.tconstruct.smeltery.tileentity.TileFaucet;
public class BlockScorchedFaucet extends BlockFaucet {
  @Nonnull @Override public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) { return new TileFaucet(); }
}
