package slimeknights.tconstruct.foundry.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import slimeknights.tconstruct.common.block.BlockInventoryTinkers;
import slimeknights.tconstruct.foundry.tileentity.TileMelter;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Single block melter: smelts one item at a time into a small tank using solid fuel. */
public class BlockMelter extends BlockInventoryTinkers {

  public static final net.minecraft.block.properties.PropertyBool ACTIVE = net.minecraft.block.properties.PropertyBool.create("active");

  public BlockMelter() {
    super(Material.ROCK);

    this.setCreativeTab(TinkerRegistry.tabSmeltery);
    this.setHardness(3F);
    this.setResistance(20F);
    this.setSoundType(SoundType.METAL);
    this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
  }

  @Nonnull
  @Override
  protected net.minecraft.block.state.BlockStateContainer createBlockState() {
    return new net.minecraft.block.state.BlockStateContainer(this, ACTIVE);
  }

  @Override
  public int getMetaFromState(net.minecraft.block.state.IBlockState state) {
    return state.getValue(ACTIVE) ? 1 : 0;
  }

  @Nonnull
  @Override
  public net.minecraft.block.state.IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(ACTIVE, (meta & 1) != 0);
  }

  @Nonnull
  @Override
  public net.minecraft.block.state.IBlockState getActualState(@Nonnull net.minecraft.block.state.IBlockState state, net.minecraft.world.IBlockAccess worldIn, net.minecraft.util.math.BlockPos pos) {
    net.minecraft.tileentity.TileEntity te = worldIn.getTileEntity(pos);
    if(te instanceof TileMelter) {
      boolean heating = ((TileMelter) te).isHeating;
      return state.withProperty(ACTIVE, heating);
    }
    return state;
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileMelter();
  }
}