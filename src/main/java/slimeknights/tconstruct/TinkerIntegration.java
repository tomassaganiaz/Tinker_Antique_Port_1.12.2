package slimeknights.tconstruct;

import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.Logger;

import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;

// Takes care of adding all the generic-ish materials
@Pulse(id = TinkerIntegration.PulseId, forced = true)
public class TinkerIntegration extends TinkerPulse {

  public static final String PulseId = "TinkerIntegration";
  static final Logger log = Util.getLogger(PulseId);

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    integrate(TinkerMaterials.wood, "plankWood");
    integrate(TinkerMaterials.stone);
    integrate(TinkerMaterials.flint);
    integrate(TinkerMaterials.cactus);
    integrate(TinkerMaterials.bone);
    integrate(TinkerMaterials.obsidian, TinkerFluids.obsidian);
    integrate(TinkerMaterials.prismarine);
    integrate(TinkerMaterials.endstone);
    integrate(TinkerMaterials.paper);
    integrate(TinkerMaterials.sponge);
    integrate(TinkerMaterials.firewood);
    // tier 1 de TC3 que faltaban: chorus (fruta de chorus) y wool (cabeza de flecha/binding)
    integrate(TinkerMaterials.chorus);
    integrate(TinkerMaterials.wool);

    // tier 4 de TC3: aleaciones (solo colado), hueso ardiente (crafteable con el item del fork)
    // y ancient, que depende del Nether Backport igual que las vides
    if(isSmelteryLoaded()) {
      integrate(TinkerMaterials.queensslime, TinkerFluids.queensslime, "Queensslime").toolforge();
      integrate(TinkerMaterials.hepatizon, TinkerFluids.hepatizon, "Hepatizon").toolforge();
      integrate(TinkerMaterials.netherite, TinkerFluids.netherite, "Netherite").toolforge();
    }
    integrate(TinkerMaterials.blazingbone);
    if(Loader.isModLoaded("nb")) {
      integrate(TinkerMaterials.ancient);
    }

    // Aleaciones de compat de TC3 (tier 3-4): fluido propio, sin lingote en el oredict -> solo colado
    if(isSmelteryLoaded()) {
      integrate(TinkerMaterials.constantan, TinkerFluids.constantan);
      integrate(TinkerMaterials.invar, TinkerFluids.invar);
      integrate(TinkerMaterials.pewter, TinkerFluids.pewter);
      integrate(TinkerMaterials.nicrosil, TinkerFluids.nicrosil);
    }

    // Materiales de compat con lingote en mods instalados: el oredict hace el gate automáticamente
    // ojo: aquí el gate es el oredict completo (ingot…), no el sufijo, porque estos materiales no tienen fluido
    integrate(TinkerMaterials.ironwood, "ingotIronwood");   // Twilight Forest
    integrate(TinkerMaterials.steeleaf, "ingotSteeleaf");   // Twilight Forest
    integrate(TinkerMaterials.fiery, "ingotFiery");         // Twilight Forest
    integrate(TinkerMaterials.dragonscale, "dragonScale");  // Ice and Fire

    // Compuestos de TC3 fabricados en la smeltery (items propios del fork)
    integrate(TinkerMaterials.slimeskin, "slimeskin");
    integrate(TinkerMaterials.nahuatl);
    integrate(TinkerMaterials.darkthread, TinkerFluids.darkthread);
    integrate(TinkerMaterials.jeweledhide, TinkerFluids.jeweledhide);
    integrate(TinkerMaterials.blazewood);
    integrate(TinkerMaterials.bamboo, "blockCactus");
    integrate(TinkerMaterials.cheese, "ingotCheese");  // nuestro lingote seco

    // Aleaciones de TC3 (tier 3): fluido propio pero sin lingote de oredict, así que van sin
    // oreSuffix. Eso significa que no hay fundido/vaciado de lingotes ni bloque de toolforge:
    // se cuelan las piezas directamente desde la smeltery.
    integrate(TinkerMaterials.slimesteel, TinkerFluids.slimesteel, "Slimesteel");
    integrate(TinkerMaterials.cinderslime, TinkerFluids.cinderslime, "Cinderslime");
    integrate(TinkerMaterials.rosegold, TinkerFluids.rosegold, "Rosegold");
    integrate(TinkerMaterials.amethystbronze, TinkerFluids.amethystbronze, "AmethystBronze");

    // listed here so it's the first in the toolforge listing
    integrate(TinkerMaterials.iron, TinkerFluids.iron, "Iron").toolforge();
    integrate(TinkerMaterials.pigiron, TinkerFluids.pigIron, "Pigiron").toolforge();

    integrate(TinkerMaterials.knightslime, TinkerFluids.knightslime, "Knightslime").toolforge();
    integrate(TinkerMaterials.slime, "slimecrystalGreen");
    integrate(TinkerMaterials.blueslime, "slimecrystalBlue");
    integrate(TinkerMaterials.magmaslime, "slimecrystalMagma");

    // alubrass needs both copper and aluminum
    TinkerRegistry.integrate(new MaterialIntegration(TinkerMaterials.alubrass, TinkerFluids.alubrass, "Alubrass", "ingotCopper", "ingotAluminum")).toolforge();
    
    // alumite needs aluminum
    TinkerRegistry.integrate(new MaterialIntegration(TinkerMaterials.alumite, TinkerFluids.alumite, "Alumite", "ingotAluminum")).toolforge();


    integrate(TinkerMaterials.netherrack);
    integrate(TinkerMaterials.cobalt, TinkerFluids.cobalt, "Cobalt").toolforge();
    integrate(TinkerMaterials.ardite, TinkerFluids.ardite, "Ardite").toolforge();
    integrate(TinkerMaterials.manyullyn, TinkerFluids.manyullyn, "Manyullyn").toolforge();

    // special bone materials
    integrate(TinkerMaterials.bloodbone);

    // materials backed by fluids that TinkerFluids only creates during the block registry event
    // (smeltery pulse). The fluid is linked later in TinkerMaterials.setupMaterials (init), so the
    // integration itself is created without one: they are craftable + castable, not oredict-integrated.
    // la piedra scorched depende del pulse de la foundry (que requiere el de la smeltery)
    if(isScorchedLoaded()) {
      integrate(TinkerMaterials.scorchedstone);
    }
    if(isSmelteryLoaded()) {
      integrate(TinkerMaterials.searedstone);
      integrate(TinkerMaterials.glass);
      integrate(TinkerMaterials.clay);
      integrate(TinkerMaterials.enderslime);
      integrate(TinkerMaterials.blood);
    }

    // mod integrations
    integrate(TinkerMaterials.copper, TinkerFluids.copper, "Copper").toolforge();
    integrate(TinkerMaterials.bronze, TinkerFluids.bronze, "Bronze").toolforge();
    integrate(TinkerMaterials.lead, TinkerFluids.lead, "Lead").toolforge();
    integrate(TinkerMaterials.silver, TinkerFluids.silver, "Silver").toolforge();
    integrate(TinkerMaterials.electrum, TinkerFluids.electrum, "Electrum").toolforge();
    integrate(TinkerMaterials.steel, TinkerFluids.steel, "Steel").toolforge();
    integrate(TinkerMaterials.nickel, TinkerFluids.nickel, "Nickel").toolforge();

    // non-toolmaterial integration
    // gold has a material now, the fluid is created by TinkerFluids.setupFluids() so it's available this early
    integrate(TinkerMaterials.gold, TinkerFluids.gold, "Gold").toolforge();
    integrate(TinkerFluids.brass, "Brass").toolforge();
    integrate(TinkerMaterials.tin, TinkerFluids.tin, "Tin").toolforge();
    integrate(TinkerFluids.zinc, "Zinc").toolforge();
    integrate(TinkerMaterials.aluminum, TinkerFluids.aluminum, "Aluminum").toolforge();

    // bow stuff
    integrate(TinkerMaterials.leather); // leather es bowstring + binding, como en TC3
    integrate(TinkerMaterials.string);
    integrate(TinkerMaterials.slimevine_blue);
    integrate(TinkerMaterials.slimevine_purple);
    integrate(TinkerMaterials.vine); // vine is last because its oredict also catches slimevines

    // tier 2 de TC3: las vides del Nether no existen en vanilla 1.12. Las aporta Unseen's
    // Nether Backport (modid "nb") y no tienen oredict, así que el gate es por mod cargado;
    // los ítems se resuelven por id en TinkerMaterials.setupMaterials (init).
    if(Loader.isModLoaded("nb")) {
      integrate(TinkerMaterials.weepingvine);
      integrate(TinkerMaterials.twistingvine);
    }

    integrate(TinkerMaterials.blaze);
    integrate(TinkerMaterials.reed);
    integrate(TinkerMaterials.ice);
    integrate(TinkerMaterials.endrod);

    // puntas de flecha y hueso necrótico (TC3 las restringe a ammo; el fork usa HeadMaterialStats)
    integrate(TinkerMaterials.necroticbone);
    integrate(TinkerMaterials.enderpearl);
    integrate(TinkerMaterials.gunpowder);
    integrate(TinkerMaterials.redstone);
    integrate(TinkerMaterials.amethyst, "gemAmethyst"); // solo si Deeper Depths está presente
    integrate(TinkerMaterials.quartz);
    integrate(TinkerMaterials.glowstone);
    integrate(TinkerMaterials.shulker);

    integrate(TinkerMaterials.feather);
    integrate(TinkerMaterials.slimeleaf_blue);
    integrate(TinkerMaterials.slimeleaf_orange);
    integrate(TinkerMaterials.slimeleaf_purple);
    integrate(TinkerMaterials.leaf); // leaf is last because its oredict also catches slimeleaves

    // TODO: should this just iterate through our materials?
    // this will not work for mods that register after Tinkers, so it might just be better for them to register it themselves
    for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
      integration.preInit();
    }
  }

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {
    // we always register blocks for all integrated fluids to prevent issues with missing blocks
    IForgeRegistry<Block> registry = event.getRegistry();
    for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
      integration.registerFluidBlock(registry);
    }
  }

  @SubscribeEvent
  public void registerRecipes(Register<IRecipe> event) {
    IForgeRegistry<IRecipe> registry = event.getRegistry();

    // call this here so we can get tool forge recipes, anywhere else is too late
    IMCIntegration.integrateSmeltery();

    // add the tool forge recipes from all integrations
    if(isToolsLoaded()) {
      for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
        integration.registerToolForgeRecipe(registry);
      }
    }
  }

  @SubscribeEvent
  public void registerModels(ModelRegistryEvent event) {
    for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
      integration.registerFluidModel();
    }
  }

  public static boolean isIntegrated(Fluid fluid) {
    String name = FluidRegistry.getFluidName(fluid);
    if(name != null) {
      for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
        if(integration.isIntegrated() && integration.fluid != null && name.equals(integration.fluid.getName())) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Handles main IMCs
   */
  @Subscribe
  public static void handleIMC(FMLInterModComms.IMCEvent event) {
    IMCIntegration.handleIMC(event);
  }

  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    for(MaterialIntegration integration : TinkerRegistry.getMaterialIntegrations()) {
      integration.integrate();
    }
    // called here since they are dependent on integrations
    TinkerSmeltery.registerAlloys();
    TinkerSmeltery.registerRecipeOredictMelting();

    // remove any materials that did not integrate
    TinkerRegistry.removeHiddenMaterials();
  }

  private static MaterialIntegration integrate(Material material) {
    return add(new MaterialIntegration(material));
  }

  private static MaterialIntegration integrate(Material material, Fluid fluid) {
    return add(new MaterialIntegration(material, fluid));
  }

  private static MaterialIntegration integrate(Material material, String oreRequirement) {
    MaterialIntegration materialIntegration = new MaterialIntegration(oreRequirement, material, null, null);
    materialIntegration.setRepresentativeItem(oreRequirement);
    return add(materialIntegration);
  }

  private static MaterialIntegration integrate(Material material, Fluid fluid, String oreSuffix) {
    return add(new MaterialIntegration(material, fluid, oreSuffix));
  }

  private static MaterialIntegration integrate(Fluid fluid, String oreSuffix) {
    return add(new MaterialIntegration(null, fluid, oreSuffix));
  }

  private static MaterialIntegration add(MaterialIntegration integration) {
    return TinkerRegistry.integrate(integration);
  }
}
