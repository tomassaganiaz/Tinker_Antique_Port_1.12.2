package slimeknights.tconstruct.tactical.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tactical.traits.TraitNunchakuCombo;
import slimeknights.tconstruct.tools.TinkerTools;
import java.util.List;

public class TinkerNunchaku extends SwordCore {
    public static final TraitNunchakuCombo COMBO = new TraitNunchakuCombo();
    public TinkerNunchaku() {
        super(new PartMaterialType(TinkerTools.toughToolRod, MaterialTypes.HEAD),
              new PartMaterialType(TinkerTools.toughToolRod, MaterialTypes.HEAD),
              new PartMaterialType(TinkerTools.toughBinding, MaterialTypes.HANDLE));
        addCategory(Category.WEAPON);
    }
    @Override public float damagePotential() { return 0.52f; }
    @Override public double attackSpeed() { return 2.1d; }
    @Override public float knockback() { return 0.3f; }
    @Override public boolean dealDamage(net.minecraft.item.ItemStack stack, EntityLivingBase attacker, Entity target, float damage) {
        boolean hit = super.dealDamage(stack, attacker, target, damage);
        if (hit && target instanceof EntityLivingBase) {
            float yaw = attacker.rotationYaw * 0.017453292F;
            target.addVelocity(-MathHelper.sin(yaw)*0.28, 0.04, MathHelper.cos(yaw)*0.28);
            target.velocityChanged = true;
        }
        return hit;
    }
    @Override protected ToolNBT buildTagData(List<Material> mats) {
        HeadMaterialStats h1 = mats.get(0).getStatsOrUnknown(MaterialTypes.HEAD);
        HeadMaterialStats h2 = mats.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
        HandleMaterialStats h = mats.get(2).getStatsOrUnknown(MaterialTypes.HANDLE);
        ToolNBT t = new ToolNBT(); t.head(h1,h2); t.handle(h); return t;
    }
    @Override public int[] getRepairParts() { return new int[]{0,1}; }
}
