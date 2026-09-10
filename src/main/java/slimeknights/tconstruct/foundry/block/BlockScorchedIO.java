package slimeknights.tconstruct.foundry.block;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nonnull;
import java.util.Locale;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.smeltery.block.BlockEnumSmeltery;
import slimeknights.tconstruct.smeltery.tileentity.TileDrain;

public class BlockScorchedIO extends BlockEnumSmeltery<BlockScorchedIO.ScorchedIOType> {

  public static final PropertyEnum<ScorchedIOType> TYPE = PropertyEnum.create("type", ScorchedIOType.class);
  public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

  public BlockScorchedIO() {
    super(TYPE, ScorchedIOType.class);
  }

  @Nonnull
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TYPE, FACING);
  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {
    int horIndex = (meta >> 2) & 0xF;
    return this.getDefaultState().withProperty(prop, fromMeta(meta & 0x3)).withProperty(FACING, EnumFacing.HORIZONTALS[horIndex % 4]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(prop).getMeta() | (state.getValue(FACING).getHorizontalIndex() << 2);
  }

  @Override
  public int damageDropped(IBlockState state) {
    return state.getValue(prop).getMeta();
  }

  @Nonnull
  @Override
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    return new TileDrain();
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    EnumFacing side = placer.getHorizontalFacing().getOpposite();
    return this.getDefaultState().withProperty(FACING, side);
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(worldIn, pos, null);
    if(fluidHandler == null) {
      return false;
    }
    ItemStack heldItem = playerIn.getHeldItem(hand);
    IItemHandler playerInventory = playerIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(heldItem, fluidHandler, playerInventory, Fluid.BUCKET_VOLUME, playerIn);
    if(result.isSuccess()) {
      playerIn.setHeldItem(hand, result.getResult());
      return true;
    }
    return FluidUtil.getFluidHandler(heldItem) != null;
  }

  public enum ScorchedIOType implements IStringSerializable, EnumBlock.IEnumMeta {
    DRAIN;

    public final int meta;
    ScorchedIOType() { meta = ordinal(); }
    @Override public String getName() { return this.toString().toLowerCase(Locale.US); }
    @Override public int getMeta() { return meta; }
  }
}
