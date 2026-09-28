package slimeknights.tconstruct.tools.common.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;

/** Hacha arrojadiza: proyectil pesado con trayectoria y retroceso tipo hacha (TC3 throwing_axe). */
public class EntityThrowingAxe extends EntityProjectileBase {

  public int spin = 0;

  public EntityThrowingAxe(World world) {
    super(world);
  }

  public EntityThrowingAxe(World world, double d, double d1, double d2) {
    super(world, d, d1, d2);
  }

  public EntityThrowingAxe(World world, EntityPlayer player, float speed, float inaccuracy, ItemStack stack, ItemStack launchingStack) {
    super(world, player, speed, inaccuracy, 1f, stack, launchingStack);
  }

  @Override
  protected void init() {
    setSize(0.4f, 0.4f);
    this.bounceOnNoDamage = false;
  }

  @Override
  public double getGravity() {
    return 0.06d;
  }

  @Override
  public double getSlowdown() {
    return 0.04f;
  }

  @Override
  protected void playHitEntitySound() {
  }

  @Override
  public void readSpawnData(io.netty.buffer.ByteBuf data) {
    super.readSpawnData(data);
    spin = rand.nextInt(360);
  }
}