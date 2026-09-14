package slimeknights.tconstruct.tactical.tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
public class TacticalEnderStaff extends CraftsmanStaff{ public TacticalEnderStaff(){super();} @Override public float damagePotential(){return 0.55f;} @Override public double attackSpeed(){return 1.2;} @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); RayTraceResult r=p.rayTrace(32,1f); if(r!=null && r.typeOfHit==RayTraceResult.Type.BLOCK && !w.isRemote){ net.minecraft.util.math.BlockPos pos=r.getBlockPos().up(); p.setPositionAndUpdate(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5); p.playSound(net.minecraft.init.SoundEvents.ENTITY_ENDERMEN_TELEPORT,1,1); s.damageItem(3,p);} return new ActionResult<>(EnumActionResult.SUCCESS,s);} }
