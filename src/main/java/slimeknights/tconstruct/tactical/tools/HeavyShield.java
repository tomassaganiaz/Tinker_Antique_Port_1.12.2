package slimeknights.tconstruct.tactical.tools;

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
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;
import java.util.List;
import javax.annotation.Nonnull;

public class HeavyShield extends TinkerToolCore {
    public HeavyShield() {
        super(PartMaterialType.head(TinkerTools.signHead), PartMaterialType.extra(TinkerTools.largePlate), PartMaterialType.handle(TinkerTools.toughToolRod));
        addCategory(Category.WEAPON);
        addPropertyOverride(new net.minecraft.util.ResourceLocation("blocking"), (s,w,e)-> e!=null&&e.isHandActive()&&e.getActiveItemStack()==s?1:0);
    }
    @Override protected ToolNBT buildTagData(List<Material> m){ HeadMaterialStats h=m.get(0).getStatsOrUnknown(MaterialTypes.HEAD); ExtraMaterialStats e=m.get(1).getStatsOrUnknown(MaterialTypes.EXTRA); HandleMaterialStats ha=m.get(2).getStatsOrUnknown(MaterialTypes.HANDLE); ToolNBT d=new ToolNBT(); d.head(h); d.extra(e); d.handle(ha); return d;}
    @Override public float damagePotential(){return 0.62f;}
    @Override public double attackSpeed(){return 0.85d;}
    @Nonnull @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(!p.getCooldownTracker().hasCooldown(this)){p.setActiveHand(h); return new ActionResult<>(EnumActionResult.SUCCESS,s);} return new ActionResult<>(EnumActionResult.FAIL,s);}
    @Nonnull @Override public EnumAction getItemUseAction(ItemStack s){return EnumAction.BLOCK;}
    @Override public int getMaxItemUseDuration(ItemStack s){return 72000;}
    @Override public int[] getRepairParts(){return new int[]{0};}
}
