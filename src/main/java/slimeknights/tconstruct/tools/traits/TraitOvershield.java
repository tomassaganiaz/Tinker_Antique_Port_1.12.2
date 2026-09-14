package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitOvershield extends AbstractTrait {
  public TraitOvershield() {
    super("overshield", 0x82c873);
  }
  @Override
  public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
    if(world.isRemote) return;
    if(player.ticksExisted % 200 == 0 && !player.isPotionActive(MobEffects.ABSORPTION)) {
      player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0, true, false));
    }
  }
}
