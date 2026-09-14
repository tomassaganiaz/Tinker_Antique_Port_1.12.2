package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.ranged.item.Shuriken;

public class TacticalShuriken extends Shuriken {
    public TacticalShuriken(){ super(); }
    @Override public float damagePotential(){ return 0.9f; }
    @Override public double attackSpeed(){ return 1.8; }
}
