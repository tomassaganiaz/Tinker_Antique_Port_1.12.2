package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import java.util.List;
import javax.annotation.Nonnull;

public class Spear extends SwordCore {
    public Spear(){ super(PartMaterialType.head(TinkerTools.arrowHead), PartMaterialType.handle(TinkerTools.toughToolRod), PartMaterialType.extra(TinkerTools.handGuard)); addCategory(Category.WEAPON);}
    @Override public float damagePotential(){return 0.85f;}
    @Override public double attackSpeed(){return 1.1d;}
    @Override protected ToolNBT buildTagData(List<Material> m){ HeadMaterialStats h=m.get(0).getStatsOrUnknown(MaterialTypes.HEAD); HandleMaterialStats ha=m.get(1).getStatsOrUnknown(MaterialTypes.HANDLE); ExtraMaterialStats e=m.get(2).getStatsOrUnknown(MaterialTypes.EXTRA); ToolNBT d=new ToolNBT(); d.head(h); d.handle(ha); d.extra(e); return d;}
    @Override public int[] getRepairParts(){return new int[]{0};}
    @Nonnull @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(ToolHelper.isBroken(s)) return new ActionResult<>(EnumActionResult.FAIL,s); p.setActiveHand(h); return new ActionResult<>(EnumActionResult.SUCCESS,s);}
    @Nonnull @Override public EnumAction getItemUseAction(ItemStack s){return EnumAction.BOW;}
    @Override public int getMaxItemUseDuration(ItemStack s){return 72000;}
    @Override public boolean dealDamage(ItemStack s, EntityLivingBase a, Entity t, float d){ boolean hit=super.dealDamage(s,a,t,d); if(hit && a instanceof EntityPlayer && !a.world.isRemote){ a.addVelocity(a.getLookVec().x*0.35,0.05,a.getLookVec().z*0.35); a.velocityChanged=true; } return hit; }
}
