package slimeknights.tconstruct.tools.armor.item;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.armor.ArmorNBT;

public class ItemPlateShield extends ToolCore {

  public ItemPlateShield(PartMaterialType... requiredComponents) {
    super(requiredComponents);
  }

  @Override
  public float damagePotential() { return 0f; }
  @Override
  public double attackSpeed() { return 1.0; }

  @Override
  public int[] getRepairParts() {
    return new int[]{0};
  }

  @Override
  public NBTTagCompound buildTag(List<Material> materials) {
    ArmorNBT data = new ArmorNBT();
    if(!materials.isEmpty()) {
      ArmorMaterialStats stats = materials.get(0).getStatsOrUnknown(ArmorMaterialStats.TYPE_SHIELD);
      data.stats(stats);
      data.modifiers = 3;
    }
    return data.get();
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.BLOCK; }
  @Override
  public int getMaxItemUseDuration(ItemStack stack) { return 72000; }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, net.minecraft.entity.player.EntityPlayer playerIn, EnumHand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    playerIn.setActiveHand(handIn);
    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
  }
  @Override
  public boolean isShield(ItemStack stack, EntityLivingBase entity) { return true; }
}
