package slimeknights.tconstruct.tactical.tools;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import slimeknights.tconstruct.tools.melee.item.FryPan;
public class TacticalMeltingPan extends FryPan{ public TacticalMeltingPan(){super();} @Override public float damagePotential(){return 0.7f;} @Override public double attackSpeed(){return 1.0;} @Override public boolean onBlockDestroyed(ItemStack s, World w, IBlockState st, BlockPos pos, net.minecraft.entity.EntityLivingBase e){ if(!w.isRemote){ ItemStack smelt=FurnaceRecipes.instance().getSmeltingResult(new ItemStack(st.getBlock(),1,st.getBlock().getMetaFromState(st))); if(!smelt.isEmpty()){ w.spawnEntity(new EntityItem(w, pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5, smelt.copy())); w.setBlockToAir(pos); return true; } } return super.onBlockDestroyed(s,w,st,pos,e);} }
