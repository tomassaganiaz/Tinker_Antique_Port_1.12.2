package slimeknights.tconstruct.tactical.worktable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockTacticalWorktable extends Block implements ITileEntityProvider {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public BlockTacticalWorktable(){
        super(Material.ROCK);
        setUnlocalizedName("tactical_worktable");
        setHardness(3.5F); setResistance(12F);
        setCreativeTab(TinkerRegistry.tabGeneral);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }
    @Override public boolean onBlockActivated(World w, BlockPos p, IBlockState s, EntityPlayer pl, EnumHand h, EnumFacing f, float x,float y,float z){
        if(!w.isRemote) pl.openGui(slimeknights.tconstruct.TConstruct.instance, TacticalGuiHandler.ID_WORKTABLE, w, p.getX(), p.getY(), p.getZ());
        return true;
    }
    @Override public IBlockState getStateForPlacement(World w, BlockPos p, EnumFacing f,float x,float y,float z,int m,EntityLivingBase pl,EnumHand h){ return getDefaultState().withProperty(FACING, pl.getHorizontalFacing().getOpposite());}
    @Override public IBlockState getStateFromMeta(int m){ return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(m));}
    @Override public int getMetaFromState(IBlockState s){ return s.getValue(FACING).getHorizontalIndex();}
    @Override protected BlockStateContainer createBlockState(){ return new BlockStateContainer(this, FACING);}
    @Override public boolean isOpaqueCube(IBlockState s){ return false;}
    @Override public boolean isFullCube(IBlockState s){ return false;}
    @Override public TileEntity createNewTileEntity(World w,int m){ return new TileTacticalWorktable();}
}
