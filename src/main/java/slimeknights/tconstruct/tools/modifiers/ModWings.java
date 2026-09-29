package slimeknights.tconstruct.tools.modifiers;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.DataAspect;
import slimeknights.tconstruct.library.modifiers.FreeAbilityAspect;
import slimeknights.tconstruct.library.modifiers.LevelAspect;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;

/**
 * Habilidad "wings" (ELYTRA) para el peto: permite planear al caer y renderiza la capa de alas
 * (maille_wings). Se aplica en el Tool Forge.
 */
public class ModWings extends ToolModifier {
  public static final String ID = "wings";

  public ModWings() {
    super(ID, 0xE8E8FF);
    addAspects(new DataAspect(this), new LevelAspect(this, 1), new FreeAbilityAspect(1));
  }

  @Override
  public void applyEffect(NBTTagCompound root, NBTTagCompound tag) {
    NBTTagCompound t = TagUtil.getToolTag(root);
    t.setFloat(Tags.WINGS, 1f);
    TagUtil.setToolTag(root, t);
  }
}
