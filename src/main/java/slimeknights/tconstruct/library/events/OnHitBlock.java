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


public class OnHitBlock extends ProjectileEvent {
  public final float speed;
  public final BlockPos pos;
  public final IBlockState blockState;

  public OnHitBlock(EntityProjectileBase projectile, float speed, BlockPos pos, IBlockState blockState) {
    super(projectile);
    this.speed = speed;
    this.pos = pos;
    this.blockState = blockState;
  }

  public static void fireEvent(EntityProjectileBase projectile, float speed, BlockPos pos, IBlockState blockState) {
    MinecraftForge.EVENT_BUS.post(new OnHitBlock(projectile, speed, pos, blockState));
  }
}
