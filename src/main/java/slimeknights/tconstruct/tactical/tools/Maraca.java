package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;
import java.util.List;

public class Maraca extends SwordCore {
    public Maraca(){ super(PartMaterialType.extra(TinkerTools.largePlate), new PartMaterialType(TinkerTools.toughBinding, MaterialTypes.HANDLE), PartMaterialType.handle(TinkerTools.toolRod)); addCategory(Category.WEAPON);}
    @Override public float damagePotential(){return 0.7f;}
    @Override public double attackSpeed(){return 1.8d;}
    @Override protected ToolNBT buildTagData(List<Material> m){ ToolNBT d=buildDefaultTag(m); HandleMaterialStats b=m.get(1).getStatsOrUnknown(MaterialTypes.HANDLE); d.durability=Math.max(1, Math.round(d.durability*Math.max(0.1f,b.modifier))); return d;}
    @Override public int[] getRepairParts(){return new int[]{0};}
    @Override public boolean dealDamage(ItemStack s, EntityLivingBase a, Entity t, float d){ boolean h=super.dealDamage(s,a,t,d); if(h && t instanceof EntityLivingBase && a.world.rand.nextFloat()<0.25f){ ((EntityLivingBase)t).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 60,0)); if(a.world.rand.nextFloat()<0.15f) ((EntityLivingBase)t).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,80,1)); } return h; }
}
