package slimeknights.tconstruct.tactical.tools;

import slimeknights.tconstruct.tools.ranged.item.LongBow;

public class TacticalLongbow extends LongBow {
    public TacticalLongbow(){ super(); }
    @Override public float damagePotential(){ return 1.0f; }
    @Override public double attackSpeed(){ return 0.9; }
}
