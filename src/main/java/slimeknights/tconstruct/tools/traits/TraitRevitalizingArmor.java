package slimeknights.tconstruct.tools.traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitRevitalizingArmor extends AbstractTrait {
  public TraitRevitalizingArmor() {
    super("revitalizing_armor", 0x5a96dc);
  }
  @Override
  public void onArmorTick(ItemStack tool, World world, EntityPlayer player) {
    if(world.isRemote) return;
    if(player.ticksExisted % 180 != 0) return;
    if(player.getHealth() < player.getMaxHealth()) {
      player.heal(0.5f);
    }
  }
}
