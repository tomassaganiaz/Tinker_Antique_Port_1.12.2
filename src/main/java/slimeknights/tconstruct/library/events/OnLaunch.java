package slimeknights.tconstruct.library.events; 
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import javax.annotation.Nullable;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;


public class OnLaunch extends ProjectileEvent {
  @Nullable
  public final ItemStack launcher;

  @Nullable
  public final EntityLivingBase shooter;

  public OnLaunch(Entity projectile, ItemStack launcher, EntityLivingBase shooter) {
    super(projectile);
    this.launcher = launcher;
    this.shooter = shooter;
  }

  public static boolean fireEvent(Entity projectile, ItemStack launcher, EntityLivingBase shooter) {
    return !MinecraftForge.EVENT_BUS.post(new OnLaunch(projectile, launcher, shooter));
  }
}
