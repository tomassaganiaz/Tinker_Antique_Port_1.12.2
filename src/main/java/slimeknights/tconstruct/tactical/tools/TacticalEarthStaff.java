package slimeknights.tconstruct.tactical.tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
public class TacticalEarthStaff extends CraftsmanStaff{ public TacticalEarthStaff(){super();} @Override public float damagePotential(){return 0.45f;} @Override public double attackSpeed(){return 1.4;} @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); BlockPos pos=new BlockPos(p.posX, p.posY-1, p.posZ); if(!w.isRemote && w.isAirBlock(pos.up()) && w.getBlockState(pos).isFullBlock()){ w.setBlockState(pos.up(), Blocks.DIRT.getDefaultState()); if(!p.capabilities.isCreativeMode) s.damageItem(1,p);} return new ActionResult<>(EnumActionResult.SUCCESS,s);} }
