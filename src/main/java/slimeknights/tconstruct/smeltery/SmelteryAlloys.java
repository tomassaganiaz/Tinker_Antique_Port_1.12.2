package slimeknights.tconstruct.smeltery;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;

import static slimeknights.tconstruct.shared.TinkerFluids.*;

/** Registro de todas las aleaciones de la smeltery. */
public final class SmelteryAlloys {

  private SmelteryAlloys() {
  }

  public static void register() {
    if(!TConstruct.pulseManager.isPulseLoaded(TinkerSmeltery.PulseId)) {
      return;
    }

    // 1 bucket lava + 1 bucket water = 2 ingots = 1 block obsidian
    // 1000 + 1000 = 288
    // 125 + 125 = 36
    if(Config.obsidianAlloy) {
      TinkerRegistry.registerAlloy(new FluidStack(obsidian, 36),
                                   new FluidStack(FluidRegistry.WATER, 125),
                                   new FluidStack(FluidRegistry.LAVA, 125));
    }

    // 1 bucket water + 4 seared ingot + 4 mud bricks = 1 block hardened clay
    // 1000 + 288 + 576 = 576
    // 250 + 72 + 144 = 144
    TinkerRegistry.registerAlloy(new FluidStack(clay, 144),
                                 new FluidStack(FluidRegistry.WATER, 250),
                                 new FluidStack(searedStone, 72),
                                 new FluidStack(dirt, 144));

    // 1 iron ingot + 1 purple slime ball + seared stone in molten form = 1 knightslime ingot
    // 144 + 250 + 288 = 144
    TinkerRegistry.registerAlloy(new FluidStack(knightslime, 72),
                                 new FluidStack(iron, 72),
                                 new FluidStack(purpleSlime, 125),
                                 new FluidStack(searedStone, 144));

    // i iron ingot + 1 blood... unit thingie + 1/3 gem = 1 pigiron
    // 144 + 99 + 222 = 144
    TinkerRegistry.registerAlloy(new FluidStack(pigIron, 144),
                                 new FluidStack(iron, 144),
                                 new FluidStack(blood, 40),
                                 new FluidStack(clay, 72));

    // Manyullyn: 3 cobalto + 1 chatarra de netherite = 4 manyullyn (única receta)
    TinkerRegistry.registerAlloy(new FluidStack(manyullyn, 576),
                                 new FluidStack(cobalt, 432),
                                 new FluidStack(scrap, 144));

    // netherita (Future MC + Nether Backport): 4 chatarra + 4 oro = 1 lingote, como en vanilla.
    // Inerte sin chatarra en el tanque, así que no necesita gate por mod.
    TinkerRegistry.registerAlloy(new FluidStack(netherite, 144),
                                 new FluidStack(scrap, 576),
                                 new FluidStack(gold, 576));

    // Alternativa de TC3 para el hierro de cerdo: 1 hierro + sangre + miel (en vez de arcilla).
    // Mismo rendimiento que la receta clásica para que ninguna de las dos sea "la buena".
    TinkerRegistry.registerAlloy(new FluidStack(pigIron, 144),
                                 new FluidStack(iron, 144),
                                 new FluidStack(blood, 40),
                                 new FluidStack(honey, 250));

    // aleaciones de TC3 (tier 3). No tienen lingote propio: se cuelan las piezas directamente.
    // slimesteel: 1 hierro + 1 bola de slime azul + 1 ladrillo seared (= 250 mB en TC3) = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(slimesteel, 288),
                                 new FluidStack(iron, 144),
                                 new FluidStack(blueslime, 250),
                                 new FluidStack(searedStone, 250));

    // rose gold: 1 cobre + 1 oro = 2 lingotes (misma proporción que en TC3)
    TinkerRegistry.registerAlloy(new FluidStack(rosegold, 288),
                                 new FluidStack(copper, 144),
                                 new FluidStack(gold, 144));

    // amethyst bronze: 1 cobre + 1 amatista = 1 lingote (TC3 es 2->1, se respeta aunque sea "con pérdida")
    TinkerRegistry.registerAlloy(new FluidStack(amethystbronze, 144),
                                 new FluidStack(copper, 144),
                                 new FluidStack(amethyst, 144));

    // queen's slime: 1 cobalto + 1 oro + 1 crema de magma = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(queensslime, 288),
                                 new FluidStack(cobalt, 144),
                                 new FluidStack(gold, 144),
                                 new FluidStack(magma, 250));

    // hepatizon: 2 cobre + 1 cobalto + 1 cuarzo = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(hepatizon, 288),
                                 new FluidStack(copper, 288),
                                 new FluidStack(cobalt, 144),
                                 new FluidStack(quartz, 144));

    // aleaciones de compat de TC3 (tier 3-4)
    // constantan: 1 cobre + 1 níquel = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(constantan, 288),
                                 new FluidStack(copper, 144),
                                 new FluidStack(nickel, 144));

    // invar: 2 hierro + 1 níquel = 3 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(invar, 432),
                                 new FluidStack(iron, 288),
                                 new FluidStack(nickel, 144));

    // pewter: 3 estaño + 1 plomo = 4 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(pewter, 576),
                                 new FluidStack(tin, 432),
                                 new FluidStack(lead, 144));

    // nicrosil (variante sin cromo de TC3): 2 níquel + 1 esmeralda + 1 cuarzo = 4 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(nicrosil, 576),
                                 new FluidStack(nickel, 288),
                                 new FluidStack(emerald, 144),
                                 new FluidStack(quartz, 144));

    // darkthread: 1 obsidiana + 1 ardita = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(darkthread, 288),
                                 new FluidStack(obsidian, 144),
                                 new FluidStack(ardite, 144));

    // jeweled hide: 1 pigiron + 1 knightslime = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(jeweledhide, 288),
                                 new FluidStack(pigIron, 144),
                                 new FluidStack(knightslime, 144));

    // cinderslime: 1 oro + 1 slime ichor + 1 ladrillo scorched = 2 lingotes
    TinkerRegistry.registerAlloy(new FluidStack(cinderslime, 288),
                                 new FluidStack(gold, 144),
                                 new FluidStack(ichor, 250),
                                 new FluidStack(scorchedStone, 72));

    // 3 ingots copper + 1 ingot tin = 4 ingots bronze
    if(TinkerIntegration.isIntegrated(bronze) &&
       TinkerIntegration.isIntegrated(copper) &&
       TinkerIntegration.isIntegrated(tin)) {
      TinkerRegistry.registerAlloy(new FluidStack(bronze, 4),
                                   new FluidStack(copper, 3),
                                   new FluidStack(tin, 1));
    }

    // 1 ingot gold + 1 ingot silver = 2 ingots electrum
    if(TinkerIntegration.isIntegrated(electrum) &&
       TinkerIntegration.isIntegrated(gold) &&
       TinkerIntegration.isIntegrated(silver)) {
      TinkerRegistry.registerAlloy(new FluidStack(electrum, 2),
                                   new FluidStack(gold, 1),
                                   new FluidStack(silver, 1));
    }

    // 1 ingot copper + 3 ingots aluminium = 4 ingots alubrass
    if(TinkerIntegration.isIntegrated(alubrass) &&
       TinkerIntegration.isIntegrated(copper) &&
       TinkerIntegration.isIntegrated(aluminum)) {
      TinkerRegistry.registerAlloy(new FluidStack(alubrass, 4),
                                   new FluidStack(copper, 1),
                                   new FluidStack(aluminum, 3));
    }

    // 2 ingots copper + 1 ingot zinc = 3 ingots brass
    if(TinkerIntegration.isIntegrated(brass) &&
       TinkerIntegration.isIntegrated(copper) &&
       TinkerIntegration.isIntegrated(zinc)) {
      TinkerRegistry.registerAlloy(new FluidStack(brass, 3),
                                   new FluidStack(copper, 2),
                                   new FluidStack(zinc, 1));
    }

    // 1 ingot aluminum + 1 ingot iron + 1 obsidian = 1 alumite
    // 144 + 144 + 288 = 144
    if(TinkerIntegration.isIntegrated(alumite) &&
       TinkerIntegration.isIntegrated(aluminum) &&
       TinkerIntegration.isIntegrated(iron)) {
      TinkerRegistry.registerAlloy(new FluidStack(alumite, 144),
                                   new FluidStack(aluminum, 144),
                                   new FluidStack(iron, 144),
                                   new FluidStack(obsidian, 288));
    }
  }
}
