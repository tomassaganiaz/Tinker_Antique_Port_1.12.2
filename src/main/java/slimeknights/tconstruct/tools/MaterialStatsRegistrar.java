package slimeknights.tconstruct.tools;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

import static slimeknights.tconstruct.library.utils.HarvestLevels.COBALT;
import static slimeknights.tconstruct.library.utils.HarvestLevels.DIAMOND;
import static slimeknights.tconstruct.library.utils.HarvestLevels.IRON;
import static slimeknights.tconstruct.library.utils.HarvestLevels.OBSIDIAN;
import static slimeknights.tconstruct.library.utils.HarvestLevels.STONE;
import static slimeknights.tconstruct.tools.TinkerMaterials.*;

/** Registra las estadisticas (tool/bow/projectile) de los materiales definidos en TinkerMaterials. */
public final class MaterialStatsRegistrar {

  private MaterialStatsRegistrar() {
  }

  public static void registerToolStats() {
    // Stats:                                                   Durability, speed, attack, handle, extra, harvestlevel
    // natural resources/blocks
    TinkerRegistry.addMaterialStats(wood,
                                    new HeadMaterialStats(35, 2.00f, 2.00f, STONE),
                                    new HandleMaterialStats(1.00f, 25),
                                    new ExtraMaterialStats(15));

    TinkerRegistry.addMaterialStats(stone,
                                    new HeadMaterialStats(120, 4.00f, 3.00f, IRON),
                                    new HandleMaterialStats(0.50f, -50),
                                    new ExtraMaterialStats(20));
    TinkerRegistry.addMaterialStats(flint,
                                    new HeadMaterialStats(150, 5.00f, 2.90f, IRON),
                                    new HandleMaterialStats(0.60f, -60),
                                    new ExtraMaterialStats(40));
    TinkerRegistry.addMaterialStats(cactus,
                                    new HeadMaterialStats(210, 4.00f, 3.40f, IRON),
                                    new HandleMaterialStats(0.85f, 20),
                                    new ExtraMaterialStats(50));
    TinkerRegistry.addMaterialStats(bone,
                                    new HeadMaterialStats(200, 5.09f, 2.50f, IRON),
                                    new HandleMaterialStats(1.10f, 50),
                                    new ExtraMaterialStats(65));
    TinkerRegistry.addMaterialStats(obsidian,
                                    new HeadMaterialStats(139, 7.07f, 4.20f, COBALT),
                                    new HandleMaterialStats(0.90f, -100),
                                    new ExtraMaterialStats(90));
    TinkerRegistry.addMaterialStats(prismarine,
                                    new HeadMaterialStats(430, 5.50f, 6.20f, IRON),
                                    new HandleMaterialStats(0.60f, -150),
                                    new ExtraMaterialStats(100));
    TinkerRegistry.addMaterialStats(endstone,
                                    new HeadMaterialStats(420, 3.23f, 3.23f, OBSIDIAN),
                                    new HandleMaterialStats(0.85f, 0),
                                    new ExtraMaterialStats(42));
    TinkerRegistry.addMaterialStats(paper,
                                    new HeadMaterialStats(12, 0.51f, 0.05f, STONE),
                                    new HandleMaterialStats(0.10f, 5),
                                    new ExtraMaterialStats(15));
    TinkerRegistry.addMaterialStats(sponge,
                                    new HeadMaterialStats(1050, 3.02f, 0.00f, STONE),
                                    new HandleMaterialStats(1.20f, 250),
                                    new ExtraMaterialStats(250));

    // tier 1 (TC3): chorus is Head(180, 3.0, 1.0, STONE); wool is arrow head only, so it has no handle
    TinkerRegistry.addMaterialStats(chorus,
                                    new HeadMaterialStats(180, 3.00f, 1.00f, STONE),
                                    new HandleMaterialStats(1.10f, 20),
                                    new ExtraMaterialStats(60));

    TinkerRegistry.addMaterialStats(wool,
                                    new HeadMaterialStats(15, 1.00f, 0.00f, STONE),
                                    new ExtraMaterialStats(10));

    // bindings + ammo: leather es solo binding (TC3 no le da stats de cabeza); el resto son
    // puntas de flecha en TC3, aquí reciben Head porque el fork no restringe por pieza
    TinkerRegistry.addMaterialStats(leather, new ExtraMaterialStats(50));

    TinkerRegistry.addMaterialStats(necroticbone,
                                    new HeadMaterialStats(125, 4.00f, 2.25f, IRON),
                                    new HandleMaterialStats(0.70f, 0),
                                    new ExtraMaterialStats(60));

    TinkerRegistry.addMaterialStats(enderpearl, new HeadMaterialStats(60, 2.50f, 2.00f, STONE));
    TinkerRegistry.addMaterialStats(gunpowder, new HeadMaterialStats(30, 2.00f, 3.00f, STONE));
    TinkerRegistry.addMaterialStats(redstone, new HeadMaterialStats(35, 3.00f, 1.50f, STONE));
    TinkerRegistry.addMaterialStats(amethyst, new HeadMaterialStats(70, 4.50f, 3.50f, IRON));
    TinkerRegistry.addMaterialStats(quartz, new HeadMaterialStats(45, 4.00f, 2.50f, STONE));
    TinkerRegistry.addMaterialStats(glowstone, new HeadMaterialStats(40, 3.50f, 2.00f, STONE));
    TinkerRegistry.addMaterialStats(shulker, new HeadMaterialStats(90, 4.50f, 3.00f, IRON));

    // tier 2 de TC3 (vides del Nether): en TC3 solo tienen BINDING, sin cabeza ni mango
    TinkerRegistry.addMaterialStats(weepingvine, new ExtraMaterialStats(45));
    TinkerRegistry.addMaterialStats(twistingvine, new ExtraMaterialStats(45));

    // Slime
    TinkerRegistry.addMaterialStats(slime,
                                    new HeadMaterialStats(1000, 4.24f, 1.80f, STONE),
                                    new HandleMaterialStats(0.70f, 0),
                                    new ExtraMaterialStats(350));
    TinkerRegistry.addMaterialStats(blueslime,
                                    new HeadMaterialStats(780, 4.03f, 1.80f, STONE),
                                    new HandleMaterialStats(1.30f, -50),
                                    new ExtraMaterialStats(200));
    TinkerRegistry.addMaterialStats(knightslime,
                                    new HeadMaterialStats(1047, 7.5f, 3.25f, COBALT),
                                    new HandleMaterialStats(0.60f, 200),
                                    new ExtraMaterialStats(125));
    TinkerRegistry.addMaterialStats(magmaslime,
                                    new HeadMaterialStats(600, 2.1f, 7.00f, STONE),
                                    new HandleMaterialStats(0.85f, -200),
                                    new ExtraMaterialStats(150));

    // Nether
    TinkerRegistry.addMaterialStats(netherrack,
                                    new HeadMaterialStats(270, 4.50f, 3.00f, IRON),
                                    new HandleMaterialStats(0.85f, -150),
                                    new ExtraMaterialStats(75));
    TinkerRegistry.addMaterialStats(cobalt,
                                    new HeadMaterialStats(800, 6.50f, 2.25f, DIAMOND),
                                    new HandleMaterialStats(0.90f, 100),
                                    new ExtraMaterialStats(300));
    TinkerRegistry.addMaterialStats(ardite,
                                    new HeadMaterialStats(990, 3.50f, 3.60f, COBALT),
                                    new HandleMaterialStats(1.40f, -200),
                                    new ExtraMaterialStats(450));
    TinkerRegistry.addMaterialStats(manyullyn,
                                    new HeadMaterialStats(1250, 6.50f, 3.50f, COBALT),
                                    new HandleMaterialStats(0.55f, 200),
                                    new ExtraMaterialStats(50));
    TinkerRegistry.addMaterialStats(firewood,
                                    new HeadMaterialStats(550, 6.00f, 5.50f, STONE),
                                    new HandleMaterialStats(1.0f, -200),
                                    new ExtraMaterialStats(150));


    // Special Bone Materials
    TinkerRegistry.addMaterialStats(bloodbone,
            						new HeadMaterialStats(200, 5.09f, 2.50f, IRON),
            						new HandleMaterialStats(1.10f, 50),
            						new ExtraMaterialStats(65));

    // Metals
    TinkerRegistry.addMaterialStats(iron,
                                    new HeadMaterialStats(250, 6.00f, 2.00f, IRON),
                                    new HandleMaterialStats(0.85f, 60),
                                    new ExtraMaterialStats(50));
    TinkerRegistry.addMaterialStats(pigiron,
                                    new HeadMaterialStats(580, 6.00f, 2.50f, DIAMOND),
                                    new HandleMaterialStats(1.00f, 50),
                                    new ExtraMaterialStats(170));

    // Aleaciones de TC3 (tier 3): Head(1040, 6, DIAMOND, 2.5), Head(175, 9, GOLD→STONE, 1) y
    // Head(720, 7, DIAMOND, 1.5) respectivamente. El oro rosado conserva el nivel de picota del oro.
    TinkerRegistry.addMaterialStats(slimesteel,
                                    new HeadMaterialStats(1040, 6.00f, 2.50f, DIAMOND),
                                    new HandleMaterialStats(1.20f, 150),
                                    new ExtraMaterialStats(200));

    // Cinderslime (tier 4): oro + slime ichor + ladrillo scorched — acomodado a 1.20.1 1221/6.5/2.25
    TinkerRegistry.addMaterialStats(cinderslime,
                                    new HeadMaterialStats(1221, 6.50f, 2.25f, COBALT),
                                    new HandleMaterialStats(1.15f, 150),
                                    new ExtraMaterialStats(180));

    TinkerRegistry.addMaterialStats(rosegold,
                                    new HeadMaterialStats(175, 9.00f, 1.00f, STONE),
                                    new HandleMaterialStats(0.70f, 0),
                                    new ExtraMaterialStats(100));

    TinkerRegistry.addMaterialStats(amethystbronze,
                                    new HeadMaterialStats(720, 7.00f, 1.50f, DIAMOND),
                                    new HandleMaterialStats(1.05f, 100),
                                    new ExtraMaterialStats(120));

    // Tier 4 de TC3: Head(1650, 6, NETHERITE→COBALT, 2), Head(975, 8, NETHERITE, 2.5),
    // Head(530, 6, IRON, 3) y Head(745, 7, NETHERITE, 2.5). TC3 no le da mango ni binding a ancient.
    TinkerRegistry.addMaterialStats(queensslime,
                                    new HeadMaterialStats(1650, 6.00f, 2.00f, COBALT),
                                    new HandleMaterialStats(1.35f, 250),
                                    new ExtraMaterialStats(350));

    TinkerRegistry.addMaterialStats(hepatizon,
                                    new HeadMaterialStats(975, 8.00f, 2.50f, COBALT),
                                    new HandleMaterialStats(1.10f, 120),
                                    new ExtraMaterialStats(150));

    TinkerRegistry.addMaterialStats(blazingbone,
                                    new HeadMaterialStats(530, 6.00f, 3.00f, IRON),
                                    new HandleMaterialStats(0.85f, 60),
                                    new ExtraMaterialStats(90));

    TinkerRegistry.addMaterialStats(ancient, new HeadMaterialStats(745, 7.00f, 2.50f, COBALT));

    TinkerRegistry.addMaterialStats(netherite,
                                    new HeadMaterialStats(900, 8.0f, 4.0f, COBALT),
                                    new HandleMaterialStats(1.2f, 100),
                                    new ExtraMaterialStats(200));

    TinkerRegistry.addMaterialStats(scrap,
                                    new HeadMaterialStats(600, 6.5f, 3.0f, DIAMOND),
                                    new HandleMaterialStats(1.0f, 80),
                                    new ExtraMaterialStats(120));

    // Aleaciones de compat de TC3: Head(675, 7.5, DIAMOND, 1.75), Head(630, 5.5, DIAMOND, 2.5),
    // Head(316, 3.5, DIAMOND, 3) y Head(816, 6, NETHERITE, 3.16)
    TinkerRegistry.addMaterialStats(constantan,
                                    new HeadMaterialStats(675, 7.50f, 1.75f, DIAMOND),
                                    new HandleMaterialStats(1.05f, 80),
                                    new ExtraMaterialStats(120));

    TinkerRegistry.addMaterialStats(invar,
                                    new HeadMaterialStats(630, 5.50f, 2.50f, DIAMOND),
                                    new HandleMaterialStats(1.05f, 90),
                                    new ExtraMaterialStats(140));

    TinkerRegistry.addMaterialStats(pewter,
                                    new HeadMaterialStats(316, 3.50f, 3.00f, DIAMOND),
                                    new HandleMaterialStats(0.90f, 60),
                                    new ExtraMaterialStats(90));

    TinkerRegistry.addMaterialStats(nicrosil,
                                    new HeadMaterialStats(816, 6.00f, 3.16f, COBALT),
                                    new HandleMaterialStats(1.15f, 140),
                                    new ExtraMaterialStats(160));

    TinkerRegistry.addMaterialStats(necronium,
                                    new HeadMaterialStats(357, 4.0f, 2.75f, DIAMOND),
                                    new HandleMaterialStats(1.0f, 80),
                                    new ExtraMaterialStats(80));

    // Materiales de mods instalados (TC3: Head(512, 6.5, IRON, 2), Head(200, 8, DIAMOND, 3), Head(1024, 8, NETHERITE, 3.5))
    TinkerRegistry.addMaterialStats(ironwood,
                                    new HeadMaterialStats(512, 6.50f, 2.00f, IRON),
                                    new HandleMaterialStats(1.00f, 40),
                                    new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(steeleaf,
                                    new HeadMaterialStats(200, 8.00f, 3.00f, DIAMOND),
                                    new HandleMaterialStats(1.00f, 0),
                                    new ExtraMaterialStats(60));

    TinkerRegistry.addMaterialStats(fiery,
                                    new HeadMaterialStats(1024, 8.00f, 3.50f, COBALT),
                                    new HandleMaterialStats(1.10f, 120),
                                    new ExtraMaterialStats(150));

    // Knightmetal (TC3 1.20.1): Head(512, 8, 3, NETHERITE), Handle(+0.15 atk, +0.05 spd), Limb/Grip (no 1.12),
    // Plating armor (2/5/7/2, tough 2), Skull(220). El nivel NETHERITE se mapea a COBALT (máx 1.12).
    TinkerRegistry.addMaterialStats(knightmetal,
                                    new HeadMaterialStats(512, 8.00f, 3.00f, COBALT),
                                    new HandleMaterialStats(1.00f, 90),
                                    new ExtraMaterialStats(100));

    // Kobold: solo armadura (shell) en TC3. Magnetite/Knightly: punta de flecha + armadura.
    // El repair_amount de shell (450/435/300) se usa como factor de armadura; en el fork no
    // existe "shell", así que se registra como Extra (ligante de armadura) + Head para los que tienen punta.
    TinkerRegistry.addMaterialStats(kobold, new ExtraMaterialStats(80));
    TinkerRegistry.addMaterialStats(magnetite,
                                    new HeadMaterialStats(180, 5.50f, 4.00f, IRON),
                                    new ExtraMaterialStats(85));
    TinkerRegistry.addMaterialStats(knightly,
                                    new HeadMaterialStats(220, 6.50f, 3.50f, COBALT),
                                    new ExtraMaterialStats(90));

    // Compuestos de TC3 portados (stats de 1.20.1 adaptados a Head/Handle/Extra).
    // ichor: arrow_head + slime; slimewood: head(375/4/1 iron)+grip; treated_wood: head(300/3.5/1.5 stone);
    // plated_slimewood: head(595/5/2 diamond); osmium: head(500/4.5/2 iron); ancient_hide: solo binding/bowstring.
    TinkerRegistry.addMaterialStats(ichor,
                                    new HeadMaterialStats(175, 5.50f, 3.00f, DIAMOND),
                                    new ExtraMaterialStats(50));
    TinkerRegistry.addMaterialStats(ichorskin, new ExtraMaterialStats(70));
    TinkerRegistry.addMaterialStats(jadeite, new ExtraMaterialStats(90));
    TinkerRegistry.addMaterialStats(ancienthide, new ExtraMaterialStats(80));
    TinkerRegistry.addMaterialStats(osmium,
                                    new HeadMaterialStats(500, 4.50f, 2.00f, IRON),
                                    new HandleMaterialStats(1.00f, 80),
                                    new ExtraMaterialStats(90));
    TinkerRegistry.addMaterialStats(treatedwood,
                                    new HeadMaterialStats(300, 3.50f, 1.50f, STONE),
                                    new HandleMaterialStats(1.10f, 60),
                                    new ExtraMaterialStats(50));
    TinkerRegistry.addMaterialStats(platedslimewood,
                                    new HeadMaterialStats(595, 5.00f, 2.00f, DIAMOND),
                                    new HandleMaterialStats(1.05f, 80),
                                    new ExtraMaterialStats(70));
    TinkerRegistry.addMaterialStats(slimewood,
                                    new HeadMaterialStats(375, 4.00f, 1.00f, IRON),
                                    new HandleMaterialStats(1.10f, 50),
                                    new ExtraMaterialStats(60));

    // Turtle/Nautilus: solo armadura (shell) en TC3 → en el fork Extra (ligante de armadura).
    TinkerRegistry.addMaterialStats(turtle, new ExtraMaterialStats(75));
    TinkerRegistry.addMaterialStats(nautilus, new ExtraMaterialStats(70));

    // dragonScale solo es punta de flecha en TC3 (ARROW_HEAD), sin mango
    TinkerRegistry.addMaterialStats(dragonscale, new HeadMaterialStats(150, 5.50f, 4.50f, IRON));

    // Compuestos: nahuatl tiene cabeza/mango; el resto solo binding o asta (como en TC3)
    TinkerRegistry.addMaterialStats(nahuatl,
                                    new HeadMaterialStats(350, 4.50f, 3.00f, DIAMOND),
                                    new HandleMaterialStats(0.90f, 60),
                                    new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(slimeskin, new ExtraMaterialStats(60));
    TinkerRegistry.addMaterialStats(darkthread, new ExtraMaterialStats(140));
    TinkerRegistry.addMaterialStats(jeweledhide, new ExtraMaterialStats(250));
    TinkerRegistry.addMaterialStats(blazewood, new ExtraMaterialStats(120));

    // TC3: cheese solo tiene BOWSTRING y laces (111); bamboo no tiene cabeza ni mango
    TinkerRegistry.addMaterialStats(cheese, new ExtraMaterialStats(111));
    // TC3 solo da SlimeStats(200) a honey; en el fork se portan como ligante (Extra) y cuerda de arco pegajosa
    TinkerRegistry.addMaterialStats(honey, new ExtraMaterialStats(60));

    // Common Metals
    TinkerRegistry.addMaterialStats(copper,
                                    new HeadMaterialStats(210, 5.30f, 3.00f, IRON),
                                    new HandleMaterialStats(1.05f, 30),
                                    new ExtraMaterialStats(100));

    TinkerRegistry.addMaterialStats(bronze,
                                    new HeadMaterialStats(430, 6.80f, 3.50f, DIAMOND),
                                    new HandleMaterialStats(1.10f, 70),
                                    new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(tin,
                                    new HeadMaterialStats(150, 4.50f, 2.50f, STONE),
                                    new HandleMaterialStats(0.80f, 15),
                                    new ExtraMaterialStats(50));

    TinkerRegistry.addMaterialStats(aluminum,
                                    new HeadMaterialStats(180, 5.90f, 2.90f, IRON),
                                    new HandleMaterialStats(0.95f, 45),
                                    new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(lead,
                                    new HeadMaterialStats(434, 5.25f, 3.50f, IRON),
                                    new HandleMaterialStats(0.70f, -50),
                                    new ExtraMaterialStats(100));

    TinkerRegistry.addMaterialStats(silver,
                                    new HeadMaterialStats(250, 5.00f, 5.00f, IRON),
                                    new HandleMaterialStats(0.95f, 50),
                                    new ExtraMaterialStats(150));

    TinkerRegistry.addMaterialStats(electrum,
                                    new HeadMaterialStats(50, 12.00f, 3.00f, IRON),
                                    new HandleMaterialStats(1.10f, -25),
                                    new ExtraMaterialStats(250));

    TinkerRegistry.addMaterialStats(steel,
                                    new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
                                    new HandleMaterialStats(0.9f, 150),
                                    new ExtraMaterialStats(25));

    TinkerRegistry.addMaterialStats(nickel,
                                    new HeadMaterialStats(300, 6.00f, 3.00f, IRON),
                                    new HandleMaterialStats(0.90f, 40),
                                    new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(alubrass,
                                  new HeadMaterialStats(450, 7.00f, 3.50f, DIAMOND),
                                  new HandleMaterialStats(1.10f, 80),
                                  new ExtraMaterialStats(90));

    TinkerRegistry.addMaterialStats(alumite,
                                  new HeadMaterialStats(700, 8.00f, 5.50f, COBALT),
                                  new HandleMaterialStats(1.25f, 150),
                                  new ExtraMaterialStats(100));

    // Materials backed by fluids the fork already had
    TinkerRegistry.addMaterialStats(gold,
                                  new HeadMaterialStats(120, 12.00f, 2.00f, STONE),
                                  new HandleMaterialStats(0.70f, -50),
                                  new ExtraMaterialStats(250));

    TinkerRegistry.addMaterialStats(searedstone,
                                  new HeadMaterialStats(550, 6.50f, 4.00f, IRON),
                                  new HandleMaterialStats(0.80f, 40),
                                  new ExtraMaterialStats(60));

    // TC3: Head(120, 4.5, IRON, 2.5) y mango con 0.8 de durabilidad. TC3 no le da limb, así que sin arco.
    TinkerRegistry.addMaterialStats(scorchedstone,
                                  new HeadMaterialStats(120, 4.50f, 2.50f, IRON),
                                  new HandleMaterialStats(0.80f, 20),
                                  new ExtraMaterialStats(60));

    TinkerRegistry.addMaterialStats(glass,
                                  new HeadMaterialStats(65, 6.50f, 5.00f, IRON),
                                  new HandleMaterialStats(0.70f, -120),
                                  new ExtraMaterialStats(30));

    TinkerRegistry.addMaterialStats(clay,
                                  new HeadMaterialStats(150, 4.00f, 2.00f, STONE),
                                  new HandleMaterialStats(1.00f, 0),
                                  new ExtraMaterialStats(80));

    TinkerRegistry.addMaterialStats(enderslime,
                                  new HeadMaterialStats(750, 5.20f, 4.50f, OBSIDIAN),
                                  new HandleMaterialStats(0.80f, 150),
                                  new ExtraMaterialStats(300));

    TinkerRegistry.addMaterialStats(blood,
                                  new HeadMaterialStats(500, 6.50f, 6.50f, COBALT),
                                  new HandleMaterialStats(0.85f, 50),
                                  new ExtraMaterialStats(400));

    //TinkerRegistry.addMaterialStats(xu,         new ToolMaterialStats(97, 1.00f, 1.00f, 0.10f, 0.20f, DIAMOND));
  }

  public static void registerBowStats() {
    BowMaterialStats whyWouldYouMakeABowOutOfThis = new BowMaterialStats(0.2f, 0.4f, -1f);

    TinkerRegistry.addMaterialStats(wood, new BowMaterialStats(1f, 1f, 0));
    TinkerRegistry.addMaterialStats(stone, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(flint, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(cactus, new BowMaterialStats(1.05f, 0.9f, 0));
    TinkerRegistry.addMaterialStats(bone, new BowMaterialStats(0.95f, 1.15f, 0));
    TinkerRegistry.addMaterialStats(obsidian, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(prismarine, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(endstone, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(paper, new BowMaterialStats(1.5f, 0.4f, -2f));
    TinkerRegistry.addMaterialStats(sponge, new BowMaterialStats(1.15f, 0.75f, 0));


    // Slime
    TinkerRegistry.addMaterialStats(slime, new BowMaterialStats(0.85f, 1.3f, 0));
    TinkerRegistry.addMaterialStats(blueslime, new BowMaterialStats(1.05f, 1f, 0));
    TinkerRegistry.addMaterialStats(knightslime, new BowMaterialStats(0.4f, 2f, 2f));
    TinkerRegistry.addMaterialStats(magmaslime, new BowMaterialStats(1.1f, 1.05f, 1f));

    // Nether
    TinkerRegistry.addMaterialStats(netherrack, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(cobalt, new BowMaterialStats(0.75f, 1.3f, 3f));
    TinkerRegistry.addMaterialStats(ardite, new BowMaterialStats(0.45f, 0.8f, 1f));
    TinkerRegistry.addMaterialStats(manyullyn, new BowMaterialStats(0.65f, 1.2f, 4f));
    TinkerRegistry.addMaterialStats(firewood, new BowMaterialStats(1f, 1f, 0f));

    // Metals
    TinkerRegistry.addMaterialStats(iron, new BowMaterialStats(0.5f, 1.5f, 7f));
    TinkerRegistry.addMaterialStats(pigiron, new BowMaterialStats(0.6f, 1.4f, 7f));

    // Aleaciones de TC3: las tres tienen limb+grip; el oro rosado además es cuerda de arco
    TinkerRegistry.addMaterialStats(slimesteel, new BowMaterialStats(0.85f, 1.25f, 4f));
    TinkerRegistry.addMaterialStats(cinderslime, new BowMaterialStats(0.9f, 1.2f, 4f));
    TinkerRegistry.addMaterialStats(amethystbronze, new BowMaterialStats(0.7f, 1.35f, 3f));
    TinkerRegistry.addMaterialStats(rosegold, new BowMaterialStats(1.15f, 0.75f, 2f));

    // Tier 4 de TC3: las tres tienen limb+grip; ancient también (Limb(745, -0.05, 0.1, 0.1))
    TinkerRegistry.addMaterialStats(queensslime, new BowMaterialStats(0.9f, 1.15f, 5f));
    TinkerRegistry.addMaterialStats(hepatizon, new BowMaterialStats(1.25f, 0.9f, 4f));
    TinkerRegistry.addMaterialStats(blazingbone, new BowMaterialStats(1.1f, 0.7f, 3f));
    TinkerRegistry.addMaterialStats(ancient, new BowMaterialStats(0.95f, 1.1f, 4f));
    TinkerRegistry.addMaterialStats(netherite, new BowMaterialStats(0.85f, 1.2f, 5f));

    // Aleaciones de compat + materiales de mods: todas tienen limb+grip en TC3 salvo dragonScale
    TinkerRegistry.addMaterialStats(constantan, new BowMaterialStats(0.8f, 1.2f, 3f));
    TinkerRegistry.addMaterialStats(invar, new BowMaterialStats(0.75f, 1.3f, 4f));
    TinkerRegistry.addMaterialStats(pewter, new BowMaterialStats(1.05f, 0.9f, 2f));
    TinkerRegistry.addMaterialStats(nicrosil, new BowMaterialStats(0.85f, 1.5f, 5f));
    TinkerRegistry.addMaterialStats(tin, new BowMaterialStats(0.6f, 1.4f, 4f));
    TinkerRegistry.addMaterialStats(aluminum, new BowMaterialStats(0.5f, 1.5f, 6f));
    TinkerRegistry.addMaterialStats(nickel, new BowMaterialStats(0.85f, 1.1f, 2f));
    TinkerRegistry.addMaterialStats(ironwood, new BowMaterialStats(0.95f, 1.10f, 2f));
    TinkerRegistry.addMaterialStats(steeleaf, new BowMaterialStats(1.0f, 1.15f, 3f));
    TinkerRegistry.addMaterialStats(fiery, new BowMaterialStats(0.9f, 1.4f, 5f));
    TinkerRegistry.addMaterialStats(nahuatl, new BowMaterialStats(1.1f, 0.95f, 3f));
    TinkerRegistry.addMaterialStats(bamboo, new BowMaterialStats(1.1f, 0.95f, 1f)); // TC3: Limb(70, +0.1 draw, -0.05 vel, -0.05 acc)
    
    // Special Bone Materials
    TinkerRegistry.addMaterialStats(bloodbone, new BowMaterialStats(0.95f, 1.15f, 0));

    // Common Metals
    TinkerRegistry.addMaterialStats(copper, new BowMaterialStats(0.6f, 1.45f, 5f));
    TinkerRegistry.addMaterialStats(bronze, new BowMaterialStats(0.55f, 1.5f, 6f));
    TinkerRegistry.addMaterialStats(lead, new BowMaterialStats(0.4f, 1.3f, 3f));
    TinkerRegistry.addMaterialStats(silver, new BowMaterialStats(1.2f, 0.8f, 2f));
    TinkerRegistry.addMaterialStats(electrum, new BowMaterialStats(1.5f, 1f, 4f));
    TinkerRegistry.addMaterialStats(steel, new BowMaterialStats(0.4f, 2f, 9f));
    TinkerRegistry.addMaterialStats(alubrass, new BowMaterialStats(0.45f, 1.5f, 6f));
    TinkerRegistry.addMaterialStats(alumite, new BowMaterialStats(0.4f, 1.5f, 8f));

    // Materials backed by fluids the fork already had
    TinkerRegistry.addMaterialStats(gold, new BowMaterialStats(1.3f, 0.9f, 2f));
    TinkerRegistry.addMaterialStats(searedstone, new BowMaterialStats(0.8f, 1.1f, 1f));
    TinkerRegistry.addMaterialStats(enderslime, new BowMaterialStats(0.9f, 1.15f, 3f));
    TinkerRegistry.addMaterialStats(blood, new BowMaterialStats(0.7f, 1.3f, 5f));
    TinkerRegistry.addMaterialStats(glass, whyWouldYouMakeABowOutOfThis);
    TinkerRegistry.addMaterialStats(clay, whyWouldYouMakeABowOutOfThis);

    // tier 1 (TC3): chorus limbs, and reed stands in for bamboo (no bamboo in 1.12)
    TinkerRegistry.addMaterialStats(chorus, new BowMaterialStats(1.0f, 0.9f, 1f));
    TinkerRegistry.addMaterialStats(reed, new BowMaterialStats(1.1f, 0.95f, 0f));

    // bindings + ammo: necrotic bone tiene limb/grip en TC3, leather solo bowstring
    TinkerRegistry.addMaterialStats(necroticbone, new BowMaterialStats(0.9f, 1.05f, 2f));

    // Bowstring materials
    BowStringMaterialStats bowstring = new BowStringMaterialStats(1f);
    TinkerRegistry.addMaterialStats(rosegold, bowstring); // TC3 le da BOWSTRING además de limb
    TinkerRegistry.addMaterialStats(slimeskin, bowstring); // TC3: BOWSTRING + maille/cuirass
    TinkerRegistry.addMaterialStats(darkthread, bowstring); // TC3: BOWSTRING
    TinkerRegistry.addMaterialStats(jeweledhide, bowstring); // TC3: BOWSTRING
    TinkerRegistry.addMaterialStats(cheese, bowstring);      // TC3: BOWSTRING + laces
    TinkerRegistry.addMaterialStats(honey, bowstring);       // TC3: slime (pegajosa) → cuerda de arco
    TinkerRegistry.addMaterialStats(ancienthide, bowstring); // TC3: BOWSTRING
    TinkerRegistry.addMaterialStats(slimewood, bowstring);   // TC3: grip
    TinkerRegistry.addMaterialStats(leather, bowstring);
    TinkerRegistry.addMaterialStats(string, bowstring);
    TinkerRegistry.addMaterialStats(vine, bowstring);
    // TC3 tier 2: ambas vides del Nether son cuerda de arco además de binding
    TinkerRegistry.addMaterialStats(weepingvine, bowstring);
    TinkerRegistry.addMaterialStats(twistingvine, bowstring);
    TinkerRegistry.addMaterialStats(slimevine_blue, bowstring);
    TinkerRegistry.addMaterialStats(slimevine_purple, bowstring);
  }

  public static void registerProjectileStats() {
    // shaft
    TinkerRegistry.addMaterialStats(wood, new ArrowShaftMaterialStats(1f, 0));
    TinkerRegistry.addMaterialStats(bone, new ArrowShaftMaterialStats(0.9f, 5));
    TinkerRegistry.addMaterialStats(blaze, new ArrowShaftMaterialStats(0.8f, 3));
    TinkerRegistry.addMaterialStats(reed, new ArrowShaftMaterialStats(1.5f, 20));
    TinkerRegistry.addMaterialStats(ice, new ArrowShaftMaterialStats(0.95f, 0));
    TinkerRegistry.addMaterialStats(blazingbone, new ArrowShaftMaterialStats(0.75f, 4)); // TC3: ARROW_SHAFT
    TinkerRegistry.addMaterialStats(blazewood, new ArrowShaftMaterialStats(0.8f, 5)); // TC3: ARROW_SHAFT
    TinkerRegistry.addMaterialStats(bamboo, new ArrowShaftMaterialStats(1.2f, 8));    // TC3: ARROW_SHAFT
    TinkerRegistry.addMaterialStats(endrod, new ArrowShaftMaterialStats(0.7f, 1));
    // TC3 da asta a cactus y chorus además de wood/bone/bamboo (reed)
    TinkerRegistry.addMaterialStats(cactus, new ArrowShaftMaterialStats(0.85f, 2));
    TinkerRegistry.addMaterialStats(chorus, new ArrowShaftMaterialStats(0.9f, 3));

    // fletching
    TinkerRegistry.addMaterialStats(feather, new FletchingMaterialStats(1.0f, 1f));
    TinkerRegistry.addMaterialStats(leaf, new FletchingMaterialStats(0.5f, 1.5f));
    TinkerRegistry.addMaterialStats(paper, new FletchingMaterialStats(0.9f, 0.8f));
    FletchingMaterialStats slimeLeafStats = new FletchingMaterialStats(0.8f, 1.25f);
    TinkerRegistry.addMaterialStats(slimeleaf_purple, slimeLeafStats);
    TinkerRegistry.addMaterialStats(slimeleaf_blue, slimeLeafStats);
    TinkerRegistry.addMaterialStats(slimeleaf_orange, slimeLeafStats);
  }
}
