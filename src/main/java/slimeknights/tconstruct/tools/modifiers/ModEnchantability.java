package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;

import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.modifiers.SingleAspect;

/**
 * Hace la herramienta encantable con encantamientos vanilla (mesa de encantar y yunque).
 * Una vez aplicado, la herramienta ya no acepta más modificadores.
 */
public class ModEnchantability extends ToolModifier {

  public static final String ID = "enchantability";

  public ModEnchantability() {
    super(ID, 0x7a9cf5);
    addAspects(new SingleAspect(this), new DataAspect(this), new FreeUpgradeAspect(1));
  }

  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    // la encantabilidad se resuelve desde los overrides de Item (getItemEnchantability / isBookEnchantable)
  }

  @Override
  public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
    return getLeveledTooltip(modifierTag, detailed);
  }
}