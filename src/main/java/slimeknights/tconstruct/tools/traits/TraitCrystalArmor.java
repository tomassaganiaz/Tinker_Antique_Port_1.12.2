package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitCrystalArmor extends AbstractTrait {
  public TraitCrystalArmor() {
    super("crystal_armor", 0xa87bbd);
  }
  @Override
  public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
    if(world.isRemote) return;
    if(player.ticksExisted % 220 == 0 && player.getHealth() >= player.getMaxHealth() * 0.9f) {
      player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100, 0, true, false));
    }
  }
}
