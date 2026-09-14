package slimeknights.tconstruct.tactical.tools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.tools.melee.item.BattleSign;
public class TacticalFlintAndBrick extends BattleSign{ public TacticalFlintAndBrick(){super();} @Override public float damagePotential(){return 0.3f;} @Override public double attackSpeed(){return 1.8;} @Override public EnumActionResult onItemUse(EntityPlayer p, World w, BlockPos pos, EnumHand h, EnumFacing f, float x,float y,float z){ BlockPos p2=pos.offset(f); if(w.isAirBlock(p2)){ w.setBlockState(p2, Blocks.FIRE.getDefaultState()); p.getHeldItem(h).damageItem(1,p); return EnumActionResult.SUCCESS; } return EnumActionResult.PASS; } @Override public boolean onBlockDestroyed(ItemStack s, World w, IBlockState st, BlockPos pos, net.minecraft.entity.EntityLivingBase e){ if(!w.isRemote && w.rand.nextFloat()<0.15f) w.setBlockState(pos, Blocks.FIRE.getDefaultState()); return super.onBlockDestroyed(s,w,st,pos,e);} }
