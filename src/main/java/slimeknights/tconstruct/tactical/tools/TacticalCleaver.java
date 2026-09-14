package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.melee.item.Cleaver;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;
import java.util.List;

public class TacticalCleaver extends Cleaver {
    public TacticalCleaver(){ super(); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=1.5f; t.durability=(int)(t.durability*1.25f); return t; }
    @Override public float damagePotential(){ return 1.35f; }
    @Override public double attackSpeed(){ return 0.65; }
}
