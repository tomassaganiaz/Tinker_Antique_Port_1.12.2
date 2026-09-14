package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.ranged.item.CrossBow;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolNBT;
import java.util.List;

public class TacticalCrossbow extends CrossBow {
    public TacticalCrossbow(){ super(); }
    @Override public float damagePotential(){ return 1.1f; }
    @Override public double attackSpeed(){ return 0.7; }
}
