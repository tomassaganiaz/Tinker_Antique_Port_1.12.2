package slimeknights.tconstruct.library.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.TinkerModifiers;

/** 
Calculo de velocidad de minado, efectividad y minado AOE.
 */
public final class 
ToolMiningHelper
 {

  private 
ToolMiningHelper
() {
  }

  public static float calcDigSpeed(ItemStack stack, IBlockState blockState) {
    if(blockState == null) {
      return 0f;
    }

    if(!stack.hasTagCompound()) {
      return 1f;
    }
    
    if(ToolDurabilityHelper.isBroken(stack)) {
      return 0.3f;
    }

    // check if the tool has the correct class and harvest level
    if(!canHarvest(stack, blockState)) {
      return 1f;
    }

    // calculate speed depending on stats
    NBTTagCompound tag = TagUtil.getToolTag(stack);
    float speed = tag.getFloat(Tags.MININGSPEED);

    if(stack.getItem() instanceof ToolCore) {
      speed *= ((ToolCore) stack.getItem()).miningSpeedModifier();
    }

    return speed;
  }

  public static boolean isToolEffective(ItemStack stack, IBlockState state) {
    // check material
    for(String type : stack.getItem().getToolClasses(stack)) {
      if(state.getBlock().isToolEffective(type, state)) {
        return true;
      }
    }

    return false;
  }

  public static boolean isToolEffective2(ItemStack stack, IBlockState state) {
    if(isToolEffective(stack, state)) {
      return true;
    }

    // this will be the only place besides fortify where a modifier is hardcoded. I promise. :L
    if(TinkerUtil.hasModifier(TagUtil.getTagSafe(stack), TinkerModifiers.modBlasting.getIdentifier()) && state.getMaterial().isToolNotRequired()) {
      // everything except fluids
      return !state.getMaterial().isLiquid();
    }

    return stack.getItem() instanceof ToolCore && ((ToolCore) stack.getItem()).isEffective(state);

  }

  public static boolean canHarvest(ItemStack stack, IBlockState state) {
    Block block = state.getBlock();

    // doesn't require a tool
    if(state.getMaterial().isToolNotRequired()) {
      return true;
    }

    String type = block.getHarvestTool(state);
    int level = block.getHarvestLevel(state);

    return stack.getItem().getHarvestLevel(stack, type, null, state) >= level;
  }

  public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth) {
    return calcAOEBlocks(stack, world, player, origin, width, height, depth, -1);
  }

  public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth, int distance) {
    // only works with toolcore because we need the raytrace call
    if(stack.isEmpty() || !(stack.getItem() instanceof ToolCore)) {
      return ImmutableList.of();
    }

    // find out where the player is hitting the block
    IBlockState state = world.getBlockState(origin);

    if(!isToolEffective2(stack, state)) {
      return ImmutableList.of();
    }

    if(state.getMaterial() == Material.AIR) {
      // what are you DOING?
      return ImmutableList.of();
    }

    // raytrace to get the side, but has to result in the same block
    RayTraceResult mop = ((ToolCore) stack.getItem()).rayTrace(world, player, true);
    if(mop == null || !origin.equals(mop.getBlockPos())) {
      mop = ((ToolCore) stack.getItem()).rayTrace(world, player, false);
    }
    EnumFacing sideHit;
    if(mop != null && origin.equals(mop.getBlockPos())) {
      sideHit = mop.sideHit;
    } else {
      // derive facing from player rotation as fallback for edge cases like very fast mining
      sideHit = player.getHorizontalFacing().getOpposite();
    }

    // fire event
    TinkerToolEvent.ExtraBlockBreak event = TinkerToolEvent.ExtraBlockBreak.fireEvent(stack, player, state, width, height, depth, distance);
    if(event.isCanceled()) {
      return ImmutableList.of();
    }
    width = event.width;
    height = event.height;
    depth = event.depth;
    distance = event.distance;

    // we know the block and we know which side of the block we're hitting. time to calculate the depth along the different axes
    int x, y, z;
    BlockPos start = origin;
    boolean mopValid = mop != null && origin.equals(mop.getBlockPos());
    switch(sideHit) {
      case DOWN:
      case UP:
        // x y depends on the angle we look?
        Vec3i vec = player.getHorizontalFacing().getDirectionVec();
        x = vec.getX() * height + vec.getZ() * width;
        y = sideHit.getAxisDirection().getOffset() * -depth;
        z = vec.getX() * width + vec.getZ() * height;
        start = start.add(-x / 2, 0, -z / 2);
        if(x % 2 == 0) {
          double hitX = mopValid ? mop.hitVec.x - mop.getBlockPos().getX() : 0.5d;
          if(x > 0 && hitX > 0.5d) start = start.add(1, 0, 0);
          else if(x < 0 && hitX < 0.5d) start = start.add(-1, 0, 0);
        }
        if(z % 2 == 0) {
          double hitZ = mopValid ? mop.hitVec.z - mop.getBlockPos().getZ() : 0.5d;
          if(z > 0 && hitZ > 0.5d) start = start.add(0, 0, 1);
          else if(z < 0 && hitZ < 0.5d) start = start.add(0, 0, -1);
        }
        break;
      case NORTH:
      case SOUTH:
        x = width;
        y = height;
        z = sideHit.getAxisDirection().getOffset() * -depth;
        start = start.add(-x / 2, -y / 2, 0);
        if(x % 2 == 0 && mopValid && mop.hitVec.x - mop.getBlockPos().getX() > 0.5d) {
          start = start.add(1, 0, 0);
        }
        if(y % 2 == 0 && mopValid && mop.hitVec.y - mop.getBlockPos().getY() > 0.5d) {
          start = start.add(0, 1, 0);
        }
        break;
      case WEST:
      case EAST:
        x = sideHit.getAxisDirection().getOffset() * -depth;
        y = height;
        z = width;
        start = start.add(0, -y / 2, -z / 2);
        if(y % 2 == 0 && mopValid && mop.hitVec.y - mop.getBlockPos().getY() > 0.5d) {
          start = start.add(0, 1, 0);
        }
        if(z % 2 == 0 && mopValid && mop.hitVec.z - mop.getBlockPos().getZ() > 0.5d) {
          start = start.add(0, 0, 1);
        }
        break;
      default:
        x = y = z = 0;
    }

    ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
    for(int xp = start.getX(); xp != start.getX() + x; xp += x / MathHelper.abs(x)) {
      for(int yp = start.getY(); yp != start.getY() + y; yp += y / MathHelper.abs(y)) {
        for(int zp = start.getZ(); zp != start.getZ() + z; zp += z / MathHelper.abs(z)) {
          // don't add the origin block
          if(xp == origin.getX() && yp == origin.getY() && zp == origin.getZ()) {
            continue;
          }
          if(distance > 0 && MathHelper.abs(xp - origin.getX()) + MathHelper.abs(yp - origin.getY()) + MathHelper.abs(
              zp - origin.getZ()) > distance) {
            continue;
          }
          BlockPos pos = new BlockPos(xp, yp, zp);
          if(isToolEffective2(stack, world.getBlockState(pos))) {
            builder.add(pos);
          }
        }
      }
    }

    return builder.build();
  }

  private static boolean canBreakExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
    // prevent calling that stuff for air blocks, could lead to unexpected behaviour since it fires events
    if(world.isAirBlock(pos)) {
      return false;
    }

    // check if the block can be broken, since extra block breaks shouldn't instantly break stuff like obsidian
    // or precious ores you can't harvest while mining stone
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    // only effective materials
    if(!isToolEffective2(stack, state)) {
      return false;
    }

    IBlockState refState = world.getBlockState(refPos);
    float refStrength = ForgeHooks.blockStrength(refState, player, world, refPos);
    float strength = ForgeHooks.blockStrength(state, player, world, pos);

    // only harvestable blocks that aren't impossibly slow to harvest
    if(!ForgeHooks.canHarvestBlock(block, player, world, pos) || refStrength / strength > 10f) {
      return false;
    }

    // From this point on it's clear that the player CAN break the block

    if(player.capabilities.isCreativeMode) {
      block.onBlockHarvested(world, pos, state, player);
      if(block.removedByPlayer(state, world, pos, player, false)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }

      // send update to client
      if(!world.isRemote) {
        TinkerNetwork.sendPacket(player, new SPacketBlockChange(world, pos));
      }
      return false;
    }
    return true;
  }

  public static void breakExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
    if(!canBreakExtraBlock(stack, world, player, pos, refPos)) {
      return;
    }

    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    // callback to the tool the player uses. Called on both sides. This damages the tool n stuff.
    stack.onBlockDestroyed(world, state, pos, player);

    // server sided handling
    if(!world.isRemote) {
      // send the blockbreak event
      int xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, pos);
      if(xp == -1) {
        return;
      }

      // serverside we reproduce ItemInWorldManager.tryHarvestBlock

      TileEntity tileEntity = world.getTileEntity(pos);
      // ItemInWorldManager.removeBlock
      if(block.removedByPlayer(state, world, pos, player, true)) { // boolean is if block can be harvested, checked above
        block.onBlockDestroyedByPlayer(world, pos, state);
        block.harvestBlock(world, player, pos, state, tileEntity, stack);
        block.dropXpOnBlockBreak(world, pos, xp);
      }

      // always send block update to client
      TinkerNetwork.sendPacket(player, new SPacketBlockChange(world, pos));
    }
    // client sided handling
    else {
      // clientside we do a "this clock has been clicked on long enough to be broken" call. This should not send any new packets
      // the code above, executed on the server, sends a block-updates that give us the correct state of the block we destroy.

      // following code can be found in PlayerControllerMP.onPlayerDestroyBlock
      world.playBroadcastSound(2001, pos, Block.getStateId(state));
      if(block.removedByPlayer(state, world, pos, player, true)) {
        block.onBlockDestroyedByPlayer(world, pos, state);
      }
      // callback to the tool
      stack.onBlockDestroyed(world, state, pos, player);

      if(stack.getCount() == 0 && stack == player.getHeldItemMainhand()) {
        ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
        player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      }

      // send an update to the server, so we get an update back
      //if(PHConstruct.extraBlockUpdates)
      NetHandlerPlayClient netHandlerPlayClient = Minecraft.getMinecraft().getConnection();
      assert netHandlerPlayClient != null;
      netHandlerPlayClient.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, Minecraft
          .getMinecraft().objectMouseOver.sideHit));
    }
  }

  public static void shearExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
    if(!canBreakExtraBlock(stack, world, player, pos, refPos)) {
      return;
    }
    // if we cannot shear the block, just run normal block break code
    if(!shearBlock(stack, world, player, pos)) {
      breakExtraBlock(stack, world, player, pos, refPos);
    }
  }

  public static boolean shearBlock(ItemStack itemstack, World world, EntityPlayer player, BlockPos pos) {
    // only serverside since it creates entities
    if(world.isRemote) {
      return false;
    }

    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    if(block instanceof IShearable) {
      IShearable target = (IShearable) block;
      if(target.isShearable(itemstack, world, pos)) {
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack);
        List<ItemStack> drops = target.onSheared(itemstack, world, pos, fortune);

        for(ItemStack stack : drops) {
          float f = 0.7F;
          double d = TConstruct.random.nextFloat() * f + (1.0F - f) * 0.5D;
          double d1 = TConstruct.random.nextFloat() * f + (1.0F - f) * 0.5D;
          double d2 = TConstruct.random.nextFloat() * f + (1.0F - f) * 0.5D;
          EntityItem entityitem = new EntityItem(player.getEntityWorld(), pos.getX() + d, pos.getY() + d1, pos.getZ() + d2, stack);
          entityitem.setDefaultPickupDelay();
          world.spawnEntity(entityitem);
        }

        itemstack.onBlockDestroyed(world, state, pos, player);
        //player.addStat(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);

        world.setBlockToAir(pos);

        return true;
      }
    }
    return false;
  }

}
