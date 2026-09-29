package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.utils.Tags;

/** Aqua Affinity (casco): minar a velocidad normal bajo el agua. */
public class ModAquaAffinity extends ModArmorProtectionBase {
  public ModAquaAffinity() {
    super("aqua_affinity", 0x66FFCC, Tags.AQUA_AFFINITY, 1.0f, true, 1);
  }
}
