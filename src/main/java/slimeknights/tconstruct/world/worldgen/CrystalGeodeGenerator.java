package slimeknights.tconstruct.world.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockCrystalCluster;

import java.util.Random;

public class CrystalGeodeGenerator implements IWorldGenerator {

  public static final CrystalGeodeGenerator INSTANCE = new CrystalGeodeGenerator();

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if(world.provider.getDimension() == 1 || world.provider.getDimension() == -1) {
      return;
    }
    if(random.nextInt(350) != 0) {
      return;
    }
    int x = chunkX * 16 + random.nextInt(16);
    int z = chunkZ * 16 + random.nextInt(16);
    int y = 8 + random.nextInt(40);
    BlockPos pos = new BlockPos(x, y, z);
    generateGeode(world, pos, random);
  }

  private void generateGeode(World world, BlockPos center, Random rand) {
    int radius = 4 + rand.nextInt(3);
    BlockCrystalCluster.CrystalType type = BlockCrystalCluster.CrystalType.values()[rand.nextInt(BlockCrystalCluster.CrystalType.values().length)];
    IBlockState state = TinkerWorld.crystalCluster.getDefaultState().withProperty(BlockCrystalCluster.TYPE, type);

    for(int dx = -radius; dx <= radius; dx++) {
      for(int dy = -radius; dy <= radius; dy++) {
        for(int dz = -radius; dz <= radius; dz++) {
          int d2 = dx * dx + dy * dy + dz * dz;
          if(d2 > radius * radius || d2 < (radius - 2) * (radius - 2)) {
            continue;
          }
          BlockPos pos = center.add(dx, dy, dz);
          if(!world.isBlockLoaded(pos)) {
            continue;
          }
          IBlockState cur = world.getBlockState(pos);
          if(cur.getBlock().isReplaceableOreGen(cur, world, pos, s -> s.getMaterial().isSolid())) {
            world.setBlockState(pos, state, 2);
          }
        }
      }
    }
  }
}
