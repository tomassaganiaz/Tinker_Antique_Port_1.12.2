package slimeknights.tconstruct.foundry;

import net.minecraftforge.fluids.Fluid;
import slimeknights.tconstruct.shared.TinkerFluids;

public final class FoundryTierHelper {

  private FoundryTierHelper() {}

  private static final java.util.Set<Fluid> TIER4_FLUIDS = new java.util.HashSet<>();
  private static final java.util.Set<String> TIER4_MATS = new java.util.HashSet<>();

  static {
    TIER4_FLUIDS.add(TinkerFluids.queensslime);
    TIER4_FLUIDS.add(TinkerFluids.hepatizon);
    TIER4_FLUIDS.add(TinkerFluids.manyullyn);
    TIER4_FLUIDS.add(TinkerFluids.knightslime);
    TIER4_FLUIDS.add(TinkerFluids.nicrosil);
    TIER4_FLUIDS.add(TinkerFluids.blood);
    TIER4_FLUIDS.add(TinkerFluids.netherite);
    TIER4_FLUIDS.add(TinkerFluids.jeweledhide);
    TIER4_FLUIDS.add(TinkerFluids.slimesteel);
    TIER4_FLUIDS.add(TinkerFluids.amethystbronze);
    TIER4_FLUIDS.add(TinkerFluids.rosegold);
    TIER4_FLUIDS.add(TinkerFluids.cinderslime);
    TIER4_MATS.add("queensslime");
    TIER4_MATS.add("hepatizon");
    TIER4_MATS.add("manyullyn");
    TIER4_MATS.add("knightslime");
    TIER4_MATS.add("fiery");
    TIER4_MATS.add("nicrosil");
    TIER4_MATS.add("blood");
    TIER4_MATS.add("ancient");
    TIER4_MATS.add("blazingbone");
    TIER4_MATS.add("shulker");
    TIER4_MATS.add("dragonscale");
    TIER4_MATS.add("enderslime");
    TIER4_MATS.add("endrod");
    TIER4_MATS.add("jeweledhide");
    TIER4_MATS.add("cinderslime");
    TIER4_MATS.add("blazewood");
    TIER4_MATS.add("netherite");
  }

  public static void alloyAlloysForTier(slimeknights.tconstruct.library.smeltery.SmelteryTank liquids, boolean isFoundry) {
    if(liquids.getFluidAmount() > liquids.getCapacity()) return;
    for(slimeknights.tconstruct.library.smeltery.AlloyRecipe recipe : slimeknights.tconstruct.library.TinkerRegistry.getAlloys()) {
      if(!recipe.isValid()) continue;
      boolean isTier4 = isTier4Fluid(recipe.getResult().getFluid());
      if(isFoundry != isTier4) continue;
      int matched = recipe.matches(liquids.getFluids());
      if(matched > 10) matched = 10;
      while(matched > 0) {
        for(net.minecraftforge.fluids.FluidStack liquid : recipe.getFluids()) {
          net.minecraftforge.fluids.FluidStack toDrain = liquid.copy();
          liquids.drain(toDrain, true);
        }
        net.minecraftforge.fluids.FluidStack toFill = slimeknights.tconstruct.library.utils.FluidUtil.getValidFluidStackOrNull(recipe.getResult().copy());
        int filled = liquids.fill(toFill, true);
        if(filled != recipe.getResult().amount) break;
        matched -= filled;
      }
    }
  }

  public static boolean isTier4Fluid(Fluid fluid) {
    return fluid != null && TIER4_FLUIDS.contains(fluid);
  }

  public static boolean isTier4Material(String materialId) {
    return materialId != null && TIER4_MATS.contains(materialId);
  }
}
