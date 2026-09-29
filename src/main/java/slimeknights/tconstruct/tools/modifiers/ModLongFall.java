package slimeknights.tconstruct.tools.modifiers;

import slimeknights.tconstruct.library.utils.Tags;

/** Long Fall (botas): reduce aun mas el dano por caida (se suma a Feather Falling). */
public class ModLongFall extends ModArmorProtectionBase {
  public ModLongFall() {
    super("long_fall", 0xAADDFF, Tags.LONG_FALL, 1.0f, false, 3);
  }
}
