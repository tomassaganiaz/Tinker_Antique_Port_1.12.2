package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.tools.Kama;
import java.util.List;

public class TacticalKama extends Kama {
    public TacticalKama(){ super(); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=0.5f; return t; }
    @Override public float damagePotential(){ return 0.85f; }
    @Override public double attackSpeed(){ return 1.5; }
}
