package slimeknights.tconstruct.tools.melee.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nonnull;
import java.util.List;

/** Pedernal y ladrillo: prende fuego al usar y quema al golpear. Porte de TC3 1.20.1 (flint_and_brick). */
public class FlintAndBrick extends TinkerToolCore {

  public FlintAndBrick() {
    super(PartMaterialType.head(TinkerTools.pickHead));

    addCategory(Category.WEAPON);
  }

  @Override
  public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    if(this.isInCreativeTab(tab)) {
      addDefaultSubItems(subItems, TinkerMaterials.searedstone);
    }
  }

  @Override
  public float damagePotential() {
    return 0.5f;
  }

  @Override
  public double attackSpeed() {
    return 1.8d;
  }

  // firestarter: clic derecho prende fuego
  @Nonnull
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if(ToolHelper.isBroken(stack)) {
      return EnumActionResult.PASS;
    }
    BlockPos target = pos.offset(facing);
    if(world.isAirBlock(target)) {
      world.setBlockState(target, net.minecraft.init.Blocks.FIRE.getDefaultState());
      if(!player.capabilities.isCreativeMode) {
        ToolHelper.damageTool(stack, 1, player);
      }
      return EnumActionResult.SUCCESS;
    }
    return EnumActionResult.PASS;
  }

  // fiery: quema al golpear
  @Override
  public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
    boolean hit = super.dealDamage(stack, player, entity, damage);
    if(hit && !ToolHelper.isBroken(stack)) {
      entity.setFire(4);
    }
    return hit;
  }

  @Override
  public ToolNBT buildTagData(List<Material> materials) {
    ToolNBT data = buildDefaultTag(materials);
    data.durability = 100;
    return data;
  }
}