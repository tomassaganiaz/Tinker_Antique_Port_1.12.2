package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.ranged.item.Shuriken;

public class TacticalJavelin extends Shuriken {
    public TacticalJavelin(){ super(); }
    @Override public float damagePotential(){ return 1.3f; }
    @Override public double attackSpeed(){ return 1.0; }
}
