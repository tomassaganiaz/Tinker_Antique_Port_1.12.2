package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitConductiveArmor extends AbstractTrait {
  public TraitConductiveArmor() {
    super("conductive_armor", 0xffc100);
  }
  @Override
  public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
    if(world.isRemote) return;
    if(world.isRainingAt(player.getPosition()) && player.ticksExisted % 140 == 0) {
      player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 120, 0, true, false));
    }
  }
}
