package slimeknights.tconstruct.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Random;

/** Bloque budding de cristal: con el tiempo hace crecer cristales adyacentes (como la amatista de 1.17+). */
public class BlockBuddingCrystal extends Block {

  public BlockBuddingCrystal() {
    super(Material.ROCK);
    setHardness(1.5f);
    setResistance(10.0f);
    setSoundType(SoundType.GLASS);
    setLightLevel(0.6f);
    setTickRandomly(true);
    setCreativeTab(TinkerRegistry.tabWorld);
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
    if(world.isRemote || random.nextInt(5) != 0) {
      return;
    }
    // crece un cristal en un bloque adyacente vacío (a veces sobre uno existente del mismo tipo)
    EnumFacing facing = EnumFacing.VALUES[random.nextInt(EnumFacing.VALUES.length)];
    BlockPos target = pos.offset(facing);
    if(world.isAirBlock(target)) {
      // tipo de cristal heredado de un vecino ya crecido o aleatorio
      BlockCrystalCluster.CrystalType type = pickType(world, pos);
      world.setBlockState(target, TinkerWorld.crystalCluster.getDefaultState()
          .withProperty(BlockCrystalCluster.TYPE, type), 2);
    }
  }

  private BlockCrystalCluster.CrystalType pickType(World world, BlockPos pos) {
    for(EnumFacing facing : EnumFacing.VALUES) {
      IBlockState neighbor = world.getBlockState(pos.offset(facing));
      if(neighbor.getBlock() == TinkerWorld.crystalCluster) {
        return neighbor.getValue(BlockCrystalCluster.TYPE);
      }
    }
    BlockCrystalCluster.CrystalType[] types = BlockCrystalCluster.CrystalType.values();
    return types[world.rand.nextInt(types.length)];
  }
}