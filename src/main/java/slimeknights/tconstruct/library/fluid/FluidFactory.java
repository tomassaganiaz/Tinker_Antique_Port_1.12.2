package slimeknights.tconstruct.library.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;

/** Factoría de fluidos: crea y registra los distintos tipos de fluido del mod. */
public final class FluidFactory {

  private FluidFactory() {
  }

  public static FluidMolten metal(Material material) {
    return metal(material.getIdentifier(), material.materialTextColor);
  }

  public static FluidMolten metal(String name, int color) {
    return register(new FluidMolten(name, color));
  }

  public static FluidMolten liquid(String name, int color) {
    return register(new FluidMolten(name, color, FluidMolten.ICON_LiquidStill, FluidMolten.ICON_LiquidFlowing));
  }

  public static FluidMolten stone(String name, int color) {
    return register(new FluidMolten(name, color, FluidColored.ICON_StoneStill, FluidColored.ICON_StoneFlowing));
  }

  public static FluidNonColored blaze(String name) {
    return register(new FluidNonColored(name, FluidNonColored.ICON_BlazeStill, FluidNonColored.ICON_BlazeFlowing));
  }

  public static FluidColored slime(String name, int color) {
    return register(new FluidColored(name, color, FluidColored.ICON_SlimeStill, FluidColored.ICON_SlimeFlowing));
  }

  public static FluidColored poison(String name, int color) {
    return register(new FluidColored(name, color, FluidColored.ICON_PoisonStill, FluidColored.ICON_PoisonFlowing));
  }

  public static FluidColored classic(String name, int color) {
    return register(new FluidColored(name, color, FluidColored.ICON_LiquidStill, FluidColored.ICON_LiquidFlowing));
  }

  public static FluidColored milk(String name, int color) {
    return register(new FluidColored(name, color, FluidColored.ICON_MilkStill, FluidColored.ICON_MilkFlowing));
  }

  public static <T extends Fluid> T register(T fluid) {
    fluid.setUnlocalizedName(Util.prefix(fluid.getName()));
    FluidRegistry.registerFluid(fluid);

    return fluid;
  }
}