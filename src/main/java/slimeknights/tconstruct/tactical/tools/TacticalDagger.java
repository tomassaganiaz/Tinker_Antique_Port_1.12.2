package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.melee.item.Rapier;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;
import java.util.List;

public class TacticalDagger extends Rapier {
    public TacticalDagger(){ super(); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=0.6f; return t; }
    @Override public float damagePotential(){ return 0.6f; }
    @Override public double attackSpeed(){ return 2.4; }
}
