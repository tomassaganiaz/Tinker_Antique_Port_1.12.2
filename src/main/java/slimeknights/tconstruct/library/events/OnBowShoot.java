package slimeknights.tconstruct.library.events; 
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ranged.BowCore;


public class OnBowShoot extends TinkerToolEvent {

  public final EntityPlayer entityPlayer;
  public final BowCore bowCore;
  public final ItemStack ammo;
  public final int useTime;
  private float baseInaccuracy;

  public int projectileCount = 1;
  public boolean consumeAmmoPerProjectile = true;
  public boolean consumeDurabilityPerProjectile = true;
  public float bonusInaccuracy = 0;

  public OnBowShoot(ItemStack bow, ItemStack ammo, EntityPlayer entityPlayer, int useTime, float baseInaccuracy) {
    super(bow);
    this.bowCore = (BowCore) bow.getItem();
    this.ammo = ammo;
    this.entityPlayer = entityPlayer;
    this.useTime = useTime;
    this.baseInaccuracy = baseInaccuracy;
  }

  public static OnBowShoot fireEvent(ItemStack bow, ItemStack ammo, EntityPlayer entityPlayer, int useTime, float baseInaccuracy) {
    OnBowShoot event = new OnBowShoot(bow, ammo, entityPlayer, useTime, baseInaccuracy);
    MinecraftForge.EVENT_BUS.post(event);
    return event;
  }

  public void setProjectileCount(int projectileCount) {
    this.projectileCount = projectileCount;
  }

  public void setConsumeAmmoPerProjectile(boolean consumeAmmoPerProjectile) {
    this.consumeAmmoPerProjectile = consumeAmmoPerProjectile;
  }

  public void setConsumeDurabilityPerProjectile(boolean consumeDurabilityPerProjectile) {
    this.consumeDurabilityPerProjectile = consumeDurabilityPerProjectile;
  }

  public void setBonusInaccuracy(float bonusInaccuracy) {
    this.bonusInaccuracy = bonusInaccuracy;
  }

  public float getBaseInaccuracy() {
    return baseInaccuracy;
  }

  public void setBaseInaccuracy(float baseInaccuracy) {
    this.baseInaccuracy = baseInaccuracy;
  }
}
