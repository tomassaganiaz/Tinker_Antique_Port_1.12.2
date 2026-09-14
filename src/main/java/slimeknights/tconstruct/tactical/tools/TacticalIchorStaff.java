package slimeknights.tconstruct.tactical.tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
public class TacticalIchorStaff extends CraftsmanStaff{ public TacticalIchorStaff(){super();} @Override public float damagePotential(){return 0.5f;} @Override public double attackSpeed(){return 1.3;} @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(!w.isRemote){ p.heal(4f); p.getFoodStats().addStats(2,0.5f); s.damageItem(2,p);} return new ActionResult<>(EnumActionResult.SUCCESS,s);} }
