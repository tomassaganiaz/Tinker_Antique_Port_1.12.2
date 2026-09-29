package slimeknights.tconstruct.tools.armor;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import slimeknights.tconstruct.tools.armor.item.ArmorCore;

public class ArmorDispenserBehavior extends BehaviorDefaultDispenseItem {

  public static void registerAll() {
    ArmorDispenserBehavior behavior = new ArmorDispenserBehavior();
    for(ArmorCore tool : allArmor()) {
      if(tool != null) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(tool, behavior);
    }
  }

  private static java.util.List<ArmorCore> allArmor() {
    java.util.List<ArmorCore> tools = new java.util.ArrayList<>();
    java.util.Collections.addAll(tools, TinkerArmor.helmet, TinkerArmor.chestplate, TinkerArmor.leggings, TinkerArmor.boots);
    return tools;
  }

  @Override
  protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
    if(!(stack.getItem() instanceof ArmorCore)) return super.dispenseStack(source, stack);
    EntityEquipmentSlot slot = ((ArmorCore) stack.getItem()).armorType;
    BlockPos pos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
    for(EntityLivingBase entity : source.getWorld().getEntitiesWithinAABB(EntityLivingBase.class,
        new AxisAlignedBB(pos), e -> e instanceof EntityPlayer || e.getItemStackFromSlot(slot).isEmpty())) {
      ItemStack current = entity.getItemStackFromSlot(slot);
      if(current.isEmpty() && ((ArmorCore) stack.getItem()).isValidArmor(stack, slot, entity)) {
        entity.setItemStackToSlot(slot, stack.splitStack(1));
        return stack;
      }
    }
    return super.dispenseStack(source, stack);
  }
}
