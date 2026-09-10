package slimeknights.tconstruct.plugin.jei.melter;

import mezz.jei.api.IGuiHelper;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.plugin.jei.smelting.SmeltingRecipeCategory;

public class MelterRecipeCategory extends SmeltingRecipeCategory {
  public static String CATEGORY = Util.prefix("melter");
  public MelterRecipeCategory(IGuiHelper helper) { super(helper); }
  @Override public String getUid() { return CATEGORY; }
  @Override public String getTitle() { return Util.translate("gui.jei.melter.title"); }
}
