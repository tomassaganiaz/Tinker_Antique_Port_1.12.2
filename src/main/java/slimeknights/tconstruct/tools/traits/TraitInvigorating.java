package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitInvigorating extends AbstractTrait {
  public TraitInvigorating() {
    super("invigorating", 0xf18ff0);
  }
  @Override
  public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
    if(world.isRemote) return;
    if(player.ticksExisted % 100 != 0) return;
    if(player.getHealth() < player.getMaxHealth() * 0.85f) {
      player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 80, 0, true, false));
    }
    if(player.getHealth() < player.getMaxHealth() && player.ticksExisted % 200 == 0) {
      player.heal(0.5f);
    }
  }
}
