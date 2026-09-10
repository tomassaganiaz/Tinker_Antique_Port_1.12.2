package slimeknights.tconstruct.plugin.jei.foundry;

import mezz.jei.api.IGuiHelper;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.plugin.jei.smelting.SmeltingRecipeCategory;

public class FoundryRecipeCategory extends SmeltingRecipeCategory {
  public static String CATEGORY = Util.prefix("foundry");
  public FoundryRecipeCategory(IGuiHelper helper) { super(helper); }
  @Override
  public String getUid() { return CATEGORY; }
  @Override
  public String getTitle() { return Util.translate("gui.jei.foundry.title"); }
}
