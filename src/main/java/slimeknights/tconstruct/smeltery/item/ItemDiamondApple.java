package slimeknights.tconstruct.smeltery.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import slimeknights.tconstruct.library.TinkerRegistry;

public class ItemDiamondApple extends ItemFood {

  public ItemDiamondApple() {
    super(4, 1.2F, false);
    setAlwaysEdible();
    setCreativeTab(TinkerRegistry.tabSmeltery);
  }

  @Override
  protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
    super.onFoodEaten(stack, worldIn, player);
    if(!worldIn.isRemote) {
      player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
      player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 1200, 0));
    }
  }
}
