package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.tools.Mattock;
import java.util.List;

public class TacticalPickadze extends Mattock {
    public TacticalPickadze(){ super(); addCategory(Category.WEAPON); }
    @Override public ToolNBT buildTagData(List<Material> m){ ToolNBT t=super.buildTagData(m); t.attack+=0.8f; return t; }
    @Override public float damagePotential(){ return 0.95f; }
    @Override public double attackSpeed(){ return 1.3; }
}
