package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
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

public class Doppelhander extends TinkerToolCore {
    public Doppelhander(){ super(PartMaterialType.head(TinkerTools.swordBlade), new PartMaterialType(TinkerTools.toughBinding, MaterialTypes.HANDLE), PartMaterialType.extra(TinkerTools.largePlate), PartMaterialType.handle(TinkerTools.toughToolRod)); addCategory(Category.WEAPON); addPropertyOverride(new net.minecraft.util.ResourceLocation("blocking"),(s,w,e)-> e!=null&&e.isHandActive()&&e.getActiveItemStack()==s?1:0);}
    @Override protected ToolNBT buildTagData(List<Material> m){ HeadMaterialStats b=m.get(0).getStatsOrUnknown(MaterialTypes.HEAD); HandleMaterialStats bi=m.get(1).getStatsOrUnknown(MaterialTypes.HANDLE); ExtraMaterialStats p=m.get(2).getStatsOrUnknown(MaterialTypes.EXTRA); HandleMaterialStats h=m.get(3).getStatsOrUnknown(MaterialTypes.HANDLE); ToolNBT d=new ToolNBT(); d.head(b); d.extra(p); d.handle(h); d.durability=Math.max(1, Math.round(d.durability*Math.max(0.1f,bi.modifier))); return d;}
    @Override public float damagePotential(){return 1.1f;}
    @Override public double attackSpeed(){return 0.75d;}
    @Nonnull @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){ ItemStack s=p.getHeldItem(h); if(h!=EnumHand.MAIN_HAND) return new ActionResult<>(EnumActionResult.FAIL,s); p.setActiveHand(h); return new ActionResult<>(EnumActionResult.SUCCESS,s);}
    @Nonnull @Override public EnumAction getItemUseAction(ItemStack s){return EnumAction.BLOCK;}
    @Override public int getMaxItemUseDuration(ItemStack s){return 72000;}
    @Override public int[] getRepairParts(){return new int[]{0};}
    @Override public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit = super.dealDamage(stack, player, entity, damage);
        if(hit && player instanceof EntityPlayer && !player.world.isRemote) {
            for(EntityLivingBase e : player.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
                if(e != player && e != entity && !player.isOnSameTeam(e) && player.getDistanceSqToEntity(e) < 9.0D) {
                    e.knockBack(player, 0.4F, MathHelper.sin(player.rotationYaw*0.017453292F), -MathHelper.cos(player.rotationYaw*0.017453292F));
                    super.dealDamage(stack, player, e, damage*0.6f);
                }
            }
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
        }
        return hit;
    }
}
