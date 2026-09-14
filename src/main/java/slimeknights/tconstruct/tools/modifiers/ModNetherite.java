package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

public class ModNetherite extends ToolModifier {
  public ModNetherite() {
    super("netherite", 0x443A3B);
    addAspects(new ModifierAspect.DataAspect(this), new ModifierAspect.LevelAspect(this, 1), new ModifierAspect.FreeUpgradeAspect(1));
  }
  @Override
  public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
    NBTTagCompound tag = TagUtil.getToolTag(rootCompound);
    boolean isArmor = tag.hasKey(Tags.DEFENSE);
    if(isArmor) {
      tag.setFloat(Tags.DEFENSE, tag.getFloat(Tags.DEFENSE) + 2.0f);
      tag.setFloat(Tags.TOUGHNESS, tag.getFloat(Tags.TOUGHNESS) + 1.0f);
    } else {
      tag.setFloat(Tags.ATTACK, tag.getFloat(Tags.ATTACK) + 1.0f);
    }
    TagUtil.setToolTag(rootCompound, tag);
  }
  @Override
  public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
    return getLeveledTooltip(modifierTag, detailed);
  }
}
