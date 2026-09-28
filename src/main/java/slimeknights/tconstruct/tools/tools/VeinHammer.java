package slimeknights.tconstruct.tools.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.tinkering.Category;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/** Martillo de veta: mina todo un filón del mismo bloque (máx 12, como TC3 vein_aoe max_distance 2). */
public class VeinHammer extends Hammer {

  public static final float DURABILITY_MODIFIER = 2.6f;
  private static final int MAX_VEIN = 12;

  public VeinHammer() {
    addCategory(Category.WEAPON);
  }

  @Override
  public float damagePotential() {
    return 1.25f;
  }

  @Override
  public double attackSpeed() {
    return 0.85d;
  }

  @Override
  public ImmutableList<BlockPos> getAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin) {
    // vea: mismo bloque conectado (sin diagonales), hasta MAX_VEIN bloques
    ImmutableList.Builder<BlockPos> vein = ImmutableList.builder();
    IBlockState target = world.getBlockState(origin);
    Set<BlockPos> visited = new HashSet<>();
    Queue<BlockPos> queue = new ArrayDeque<>();
    queue.add(origin);
    visited.add(origin);
    while(!queue.isEmpty() && visited.size() < MAX_VEIN) {
      BlockPos current = queue.poll();
      vein.add(current);
      if(visited.size() >= MAX_VEIN) break;
      for(EnumFacing face : EnumFacing.values()) {
        BlockPos next = current.offset(face);
        if(!visited.contains(next) && world.getBlockState(next).getBlock() == target.getBlock()) {
          visited.add(next);
          queue.add(next);
        }
      }
    }
    return vein.build();
  }

  @Override
  public float getRepairModifierForPart(int index) {
    return index == 1 ? DURABILITY_MODIFIER : DURABILITY_MODIFIER * 0.6f;
  }
}