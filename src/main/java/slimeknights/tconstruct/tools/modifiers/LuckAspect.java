package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.modifiers.FreeFirstModifierAspect;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.modifiers.MultiAspect;

/** Aspecto de nivel del modificador Luck. */
public class LuckAspect extends MultiAspect {

  public LuckAspect(IModifier parent) {
    super(parent, 0x5a82e2, ModLuck.maxLevel, ModLuck.baseCount, 1);

    freeModifierAspect = new FreeFirstModifierAspect(parent, 1);
  }

  @Override
  protected int getMaxForLevel(int level) {
    return (countPerLevel * level * (level + 1)) / 2; // sum(n)
  }

  public int getLevel(int current) {
    int i = 0;
    while(current >= getMaxForLevel(i + 1)) {
      i++;
    }
    return i;
  }
}