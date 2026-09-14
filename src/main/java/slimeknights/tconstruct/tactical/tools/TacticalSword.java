package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.melee.item.BroadSword;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;
import java.util.List;

public class TacticalSword extends BroadSword {
    public TacticalSword(){ super(); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=0.7f; return t; }
    @Override public float damagePotential(){ return 1.05f; }
    @Override public double attackSpeed(){ return 1.6; }
}
