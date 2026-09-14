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

public class SwiftShield extends TinkerToolCore {
    public SwiftShield() {
        super(PartMaterialType.head(TinkerTools.panHead), PartMaterialType.handle(TinkerTools.largePlate));
        addCategory(Category.WEAPON);
        addPropertyOverride(new net.minecraft.util.ResourceLocation("blocking"), (s,w,e)-> e!=null&&e.isHandActive()&&e.getActiveItemStack()==s?1:0);
    }
    @Override protected ToolNBT buildTagData(List<Material> m){ HeadMaterialStats h=m.get(0).getStatsOrUnknown(MaterialTypes.HEAD); HandleMaterialStats ha=m.get(1).getStatsOrUnknown(MaterialTypes.HANDLE); ToolNBT d=new ToolNBT(); d.head(h); d.handle(ha); return d;}
    @Override public float damagePotential(){return 0.38f;}
    @Override public double attackSpeed(){return 1.4d;}
    @Override public void onUpdate(ItemStack s, World w, Entity e,int slot,boolean sel){ preventSlowDown(e,1.0f); super.onUpdate(s,w,e,slot,sel);}
    @Nonnull @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(!p.getCooldownTracker().hasCooldown(this)){p.setActiveHand(h); return new ActionResult<>(EnumActionResult.SUCCESS,s);} return new ActionResult<>(EnumActionResult.FAIL,s);}
    @Nonnull @Override public EnumAction getItemUseAction(ItemStack s){return EnumAction.BLOCK;}
    @Override public int getMaxItemUseDuration(ItemStack s){return 72000;}
}
