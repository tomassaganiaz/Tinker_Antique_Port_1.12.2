package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.utils.Tags;

/** Respiration (casco): respiración bajo el agua. */
public class ModRespiration extends ModArmorProtectionBase {
  public ModRespiration() {
    super("respiration", 0x66CCFF, Tags.RESPIRATION, 1.0f, true, 3);
  }
}
