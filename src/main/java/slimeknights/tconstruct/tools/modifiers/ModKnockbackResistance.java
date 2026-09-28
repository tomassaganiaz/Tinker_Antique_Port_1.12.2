package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.utils.Tags;

public class ModKnockbackResistance extends ToolModifier {
  public ModKnockbackResistance() {
    super("knockback_resistance", 0x8CA1B7);
    addAspects(new DataAspect(this), new LevelAspect(this, 1), new FreeUpgradeAspect(1));
  }
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    NBTTagCompound tag = TagUtil.getToolTag(rootCompound);
    tag.setFloat(Tags.KNOCKBACK_RESISTANCE, tag.getFloat(Tags.KNOCKBACK_RESISTANCE) + 0.5f);
    TagUtil.setToolTag(rootCompound, tag);
  }
  @Override
  public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
    return getLeveledTooltip(modifierTag, detailed);
  }
}
