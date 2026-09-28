package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.modifiers.FreeUpgradeAspect;
import slimeknights.tconstruct.library.modifiers.FreeAbilityAspect;
import slimeknights.tconstruct.library.utils.TagUtil;

public abstract class ModArmorProtectionBase extends ToolModifier {
  private final String tag;
  private final float amount;
  public ModArmorProtectionBase(String id, int color, String tag, float amount) {
    this(id, color, tag, amount, false, 4);
  }
  public ModArmorProtectionBase(String id, int color, String tag, float amount, boolean isAbility) {
    this(id, color, tag, amount, isAbility, 4);
  }
  public ModArmorProtectionBase(String id, int color, String tag, float amount, int maxLevel) {
    this(id, color, tag, amount, false, maxLevel);
  }
  public ModArmorProtectionBase(String id, int color, String tag, float amount, boolean isAbility, int maxLevel) {
    super(id, color);
    this.tag = tag;
    this.amount = amount;
    if(isAbility) addAspects(new DataAspect(this), new LevelAspect(this, maxLevel), new FreeAbilityAspect(1));
    else addAspects(new DataAspect(this), new LevelAspect(this, maxLevel), new FreeUpgradeAspect(1));
  }
  @Override public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {
    ModifierNBT data = ModifierNBT.readTag(tag);
    int lvl = data.level == 0 ? 1 : data.level;
    NBTTagCompound t = TagUtil.getToolTag(root);
    t.setFloat(this.tag, t.getFloat(this.tag) + amount * lvl);
    TagUtil.setToolTag(root, t);
  }
}
