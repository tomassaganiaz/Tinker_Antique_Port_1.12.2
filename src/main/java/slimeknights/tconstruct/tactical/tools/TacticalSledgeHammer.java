package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.tools.Hammer;
import java.util.List;

public class TacticalSledgeHammer extends Hammer {
    public TacticalSledgeHammer(){ super(); addCategory(Category.WEAPON); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.durability=(int)(t.durability*1.3f); t.attack+=2f; return t; }
    @Override public float damagePotential(){ return 1.4f; }
    @Override public double attackSpeed(){ return 0.6; }
    @Override public float knockback(){ return 1.8f; }
    @Override public boolean dealDamage(ItemStack s, EntityLivingBase a, Entity t, float d){ boolean h=super.dealDamage(s,a,t,d); if(h && t instanceof EntityLivingBase) ((EntityLivingBase)t).knockBack(a, 1.2f, a.posX - t.posX, a.posZ - t.posZ); return h; }
}
