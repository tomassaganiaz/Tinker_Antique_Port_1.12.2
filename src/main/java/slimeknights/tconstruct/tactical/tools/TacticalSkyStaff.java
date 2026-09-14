package slimeknights.tconstruct.tactical.tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
public class TacticalSkyStaff extends CraftsmanStaff{ public TacticalSkyStaff(){super();} @Override public float damagePotential(){return 0.4f;} @Override public double attackSpeed(){return 1.5;} @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(!w.isRemote) p.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 40, 1)); p.setActiveHand(h); return new ActionResult<>(EnumActionResult.SUCCESS,s);} }
