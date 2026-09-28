package slimeknights.tconstruct.library.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * Fachada de los helpers de herramientas.
 * <p>
 * La implementación vive en {@link ToolStatsHelper} (stats), {@link ToolMiningHelper} (minado/AOE),
 * {@link ToolDurabilityHelper} (durabilidad) y {@link ToolCombatHelper} (combate). Esta clase solo
 * expone la API pública estática delegando en ellos, para no romper los call sites existentes.
 */
public final class ToolHelper {

  private ToolHelper() {
  }

  /*---------------------------------------------------------------------------
  | BASIC TOOL STATS                                                           |
  ---------------------------------------------------------------------------*/
  public static boolean hasCategory(ItemStack stack, Category category) {
    return ToolStatsHelper.hasCategory(stack, category);
  }

  public static int getDurabilityStat(ItemStack stack) {
    return ToolStatsHelper.getDurabilityStat(stack);
  }

  public static int getHarvestLevelStat(ItemStack stack) {
    return ToolStatsHelper.getHarvestLevelStat(stack);
  }

  public static float getMiningSpeedStat(ItemStack stack) {
    return ToolStatsHelper.getMiningSpeedStat(stack);
  }

  public static float getAttackStat(ItemStack stack) {
    return ToolStatsHelper.getAttackStat(stack);
  }

  public static float getActualAttack(ItemStack stack) {
    return ToolStatsHelper.getActualAttack(stack);
  }

  public static float getAttackSpeedStat(ItemStack stack) {
    return ToolStatsHelper.getAttackSpeedStat(stack);
  }

  public static float getActualAttackSpeed(ItemStack stack) {
    return ToolStatsHelper.getActualAttackSpeed(stack);
  }

  public static float getActualMiningSpeed(ItemStack stack) {
    return ToolStatsHelper.getActualMiningSpeed(stack);
  }

  public static int getFreeModifiers(ItemStack stack) {
    return ToolStatsHelper.getFreeModifiers(stack);
  }

  public static int getFortuneLevel(ItemStack stack) {
    return ToolStatsHelper.getFortuneLevel(stack);
  }

  public static List<ITrait> getTraits(ItemStack stack) {
    return ToolStatsHelper.getTraits(stack);
  }

  /*---------------------------------------------------------------------------
  | MINING & EFFECTIVENESS                                                     |
  ---------------------------------------------------------------------------*/
  public static float calcDigSpeed(ItemStack stack, IBlockState blockState) {
    return ToolMiningHelper.calcDigSpeed(stack, blockState);
  }

  public static boolean isToolEffective(ItemStack stack, IBlockState state) {
    return ToolMiningHelper.isToolEffective(stack, state);
  }

  public static boolean isToolEffective2(ItemStack stack, IBlockState state) {
    return ToolMiningHelper.isToolEffective2(stack, state);
  }

  public static boolean canHarvest(ItemStack stack, IBlockState state) {
    return ToolMiningHelper.canHarvest(stack, state);
  }

  public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth) {
    return ToolMiningHelper.calcAOEBlocks(stack, world, player, origin, width, height, depth);
  }

  public static ImmutableList<BlockPos> calcAOEBlocks(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height, int depth, int distance) {
    return ToolMiningHelper.calcAOEBlocks(stack, world, player, origin, width, height, depth, distance);
  }

  public static void breakExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
    ToolMiningHelper.breakExtraBlock(stack, world, player, pos, refPos);
  }

  public static void shearExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos, BlockPos refPos) {
    ToolMiningHelper.shearExtraBlock(stack, world, player, pos, refPos);
  }

  public static boolean shearBlock(ItemStack itemstack, World world, EntityPlayer player, BlockPos pos) {
    return ToolMiningHelper.shearBlock(itemstack, world, player, pos);
  }

  /*---------------------------------------------------------------------------
  | TOOL DURABILITY                                                            |
  ---------------------------------------------------------------------------*/
  public static int getCurrentDurability(ItemStack stack) {
    return ToolDurabilityHelper.getCurrentDurability(stack);
  }

  public static int getMaxDurability(ItemStack stack) {
    return ToolDurabilityHelper.getMaxDurability(stack);
  }

  public static void damageTool(ItemStack stack, int amount, EntityLivingBase entity) {
    ToolDurabilityHelper.damageTool(stack, amount, entity);
  }

  public static void healTool(ItemStack stack, int amount, EntityLivingBase entity) {
    ToolDurabilityHelper.healTool(stack, amount, entity);
  }

  public static boolean isBroken(ItemStack stack) {
    return ToolDurabilityHelper.isBroken(stack);
  }

  public static void breakTool(ItemStack stack, EntityLivingBase entity) {
    ToolDurabilityHelper.breakTool(stack, entity);
  }

  public static void unbreakTool(ItemStack stack) {
    ToolDurabilityHelper.unbreakTool(stack);
  }

  public static void repairTool(ItemStack stack, int amount) {
    ToolDurabilityHelper.repairTool(stack, amount);
  }

  public static void repairTool(ItemStack stack, int amount, EntityLivingBase entity) {
    ToolDurabilityHelper.repairTool(stack, amount, entity);
  }

  /*---------------------------------------------------------------------------
  | COMBAT                                                                    |
  ---------------------------------------------------------------------------*/
  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity) {
    return ToolCombatHelper.attackEntity(stack, tool, attacker, targetEntity);
  }

  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity, Entity projectileEntity) {
    return ToolCombatHelper.attackEntity(stack, tool, attacker, targetEntity, projectileEntity);
  }

  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity, Entity projectileEntity, boolean applyCooldown) {
    return ToolCombatHelper.attackEntity(stack, tool, attacker, targetEntity, projectileEntity, applyCooldown);
  }

  public static float calcCutoffDamage(float damage, float cutoff) {
    return ToolCombatHelper.calcCutoffDamage(damage, cutoff);
  }

  public static float getActualDamage(ItemStack stack, EntityLivingBase player) {
    return ToolCombatHelper.getActualDamage(stack, player);
  }

  public static void swingItem(int speed, EntityLivingBase entity) {
    ToolCombatHelper.swingItem(speed, entity);
  }

  public static ItemStack playerIsHoldingItemWith(EntityPlayer player, Predicate<ItemStack> predicate) {
    return ToolCombatHelper.playerIsHoldingItemWith(player, predicate);
  }
}