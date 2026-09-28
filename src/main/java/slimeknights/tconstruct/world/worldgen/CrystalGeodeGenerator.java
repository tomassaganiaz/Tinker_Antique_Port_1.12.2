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
    int dim = world.provider.getDimension();
    // Overworld: geodas de slime verde/azul (EARTH/SKY). Nether: magma + knightmetal (ICHOR/KNIGHTMETAL).
    // End: ender. En 1.20.1 cada geoda genera su cristal; aquí se reparte por dimensión.
    if(dim == 1) {
      generateGeode(world, chunkX, chunkZ, random, BlockCrystalCluster.CrystalType.ENDER);
    }
    else if(dim == -1) {
      if(random.nextInt(400) == 0) {
        generateGeode(world, chunkX, chunkZ, random,
            random.nextBoolean() ? BlockCrystalCluster.CrystalType.ICHOR : BlockCrystalCluster.CrystalType.KNIGHTMETAL);
      }
    }
    else {
      if(random.nextInt(350) != 0) {
        return;
      }
      BlockCrystalCluster.CrystalType type = random.nextBoolean()
          ? BlockCrystalCluster.CrystalType.EARTH : BlockCrystalCluster.CrystalType.SKY;
      generateGeode(world, chunkX, chunkZ, random, type);
    }
  }

  private void generateGeode(World world, int chunkX, int chunkZ, Random rand, BlockCrystalCluster.CrystalType type) {
    int x = chunkX * 16 + rand.nextInt(16);
    int z = chunkZ * 16 + rand.nextInt(16);
    int y = 8 + rand.nextInt(40);
    generateGeode(world, new BlockPos(x, y, z), rand, type);
  }

  private void generateGeode(World world, BlockPos center, Random rand, BlockCrystalCluster.CrystalType type) {
    int radius = 4 + rand.nextInt(3);
    IBlockState crystalState = TinkerWorld.crystalCluster.getDefaultState().withProperty(BlockCrystalCluster.TYPE, type);
    IBlockState buddingState = TinkerWorld.buddingCrystal.getDefaultState();

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
            // capa exterior: cristales; interior: budding (permite que crezcan más)
            boolean interior = d2 <= (radius - 1) * (radius - 1);
            world.setBlockState(pos, interior ? buddingState : crystalState, 2);
          }
        }
      }
    }
  }
}
