package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.tools.Scythe;
import slimeknights.tconstruct.library.materials.Material;
import java.util.List;

public class TacticalScythe extends Scythe {
    public TacticalScythe(){ super(); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=1.2f; return t; }
    @Override public float damagePotential(){ return 1.0f; }
    @Override public double attackSpeed(){ return 0.85; }
    @Override public boolean dealDamage(ItemStack s, EntityLivingBase a, Entity t, float d){
        boolean h=super.dealDamage(s,a,t,d);
        if(h && a.world.getEntitiesWithinAABB(EntityLivingBase.class, t.getEntityBoundingBox().grow(2,0.5,2)).size()>1){
            for(EntityLivingBase e: a.world.getEntitiesWithinAABB(EntityLivingBase.class, t.getEntityBoundingBox().grow(2,0.5,2))){
                if(e!=a && e!=t && !a.isOnSameTeam(e)) super.dealDamage(s,a,e, d*0.5f);
            }
        }
        return h;
    }
}
