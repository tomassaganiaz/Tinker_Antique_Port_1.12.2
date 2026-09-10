package slimeknights.tconstruct.foundry.block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import slimeknights.tconstruct.smeltery.block.BlockCasting;
import javax.annotation.Nonnull;
public class BlockFoundryCasting extends BlockCasting {
  public static final PropertyEnum<CastingType> TYPE = BlockCasting.TYPE;
  public BlockFoundryCasting() { super(); }
  @Nonnull @Override protected BlockStateContainer createBlockState() { return super.createBlockState(); }
  @Nonnull @Override public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) { return super.createNewTileEntity(worldIn, meta); }
}
