package slimeknights.tconstruct.shared;

import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.Logger;

import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.common.CommonProxy;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.fluid.FluidColored;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.fluid.FluidNonColored;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.block.BlockLiquidSlime;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import slimeknights.tconstruct.smeltery.block.BlockTinkerFluid;
import slimeknights.tconstruct.library.fluid.FluidFactory;
import slimeknights.tconstruct.tools.TinkerMaterials;

@Pulse(id = TinkerFluids.PulseId, pulsesRequired = TinkerSmeltery.PulseId, forced = true)
public class TinkerFluids extends TinkerPulse {

  public static final String PulseId = "TinkerFluids";
  static final Logger log = Util.getLogger(PulseId);

  @SidedProxy(clientSide = "slimeknights.tconstruct.shared.FluidsClientProxy", serverSide = "slimeknights.tconstruct.common.CommonProxy")
  public static CommonProxy proxy;

  // The fluids. Note that just because they exist doesn't mean that they're registered!
  public static FluidMolten searedStone;
  public static FluidMolten scorchedStone;
  public static FluidMolten obsidian;
  public static FluidMolten clay;
  public static FluidMolten dirt;
  public static FluidMolten calcium;
  public static FluidMolten iron;
  public static FluidMolten gold;
  public static FluidMolten pigIron;
  public static FluidMolten cobalt;
  public static FluidMolten ardite;
  public static FluidMolten manyullyn;
  public static FluidMolten knightslime;
  public static FluidMolten emerald;
  public static FluidMolten diamond;
  public static FluidMolten glass;
  public static FluidColored venom;
  public static FluidColored blood;
  public static FluidNonColored blazingBlood;
  public static FluidColored milk;
  public static FluidColored greenSlime;
  public static FluidColored blueslime;
  public static FluidColored purpleSlime;

  public static FluidMolten alubrass;
  public static FluidMolten alumite;

  // Mod Integration fluids
  public static FluidMolten brass;
  public static FluidMolten copper;
  public static FluidMolten tin;
  public static FluidMolten bronze;
  public static FluidMolten zinc;
  public static FluidMolten lead;
  public static FluidMolten nickel;
  public static FluidMolten silver;
  public static FluidMolten electrum;
  public static FluidMolten steel;
  public static FluidMolten aluminum;
  // tier 3 de TC3: aleaciones propias. No tienen lingote propio en el fork, se cuelan solo en la smeltery
  public static FluidMolten slimesteel;
  public static FluidMolten rosegold;
  public static FluidMolten amethystbronze;
  public static FluidMolten amethyst;
  // tier 4 de TC3: aleaciones propias (sin lingote en el fork) e insumos de esas aleaciones
  public static FluidMolten queensslime;
  public static FluidMolten hepatizon;
  public static FluidMolten magma;
  public static FluidMolten quartz;
  // aleaciones de compat de TC3 (tier 3-4): mismos fluidos del fork salvo el níquel, que ya existía
  public static FluidMolten constantan;
  public static FluidMolten invar;
  public static FluidMolten pewter;
  public static FluidMolten nicrosil;
  public static FluidMolten darkthread;
  public static FluidMolten jeweledhide;
  public static FluidMolten cinderslime;
  public static FluidColored ichor;
  // insumos "de libro" para recetas alternativas: chatarra de netherite (Nether Backport) y miel (Future MC)
    public static FluidMolten scrap;
    public static FluidMolten honey;
    public static FluidMolten netherite;

  static {
    setupFluids();
  }

  public static void setupFluids() {
    // buuuuckeeeeet
    FluidRegistry.enableUniversalBucket();

    // Fluids for integration, getting registered by TinkerIntegration
    iron = FluidFactory.metal(TinkerMaterials.iron.getIdentifier(), 0xa81212);
    iron.setTemperature(769);

    gold = FluidFactory.metal("gold", 0xf6d609);
    gold.setTemperature(532);
    gold.setRarity(EnumRarity.RARE);

    pigIron = FluidFactory.metal(TinkerMaterials.pigiron);
    pigIron.setTemperature(600);
    pigIron.setRarity(EnumRarity.EPIC);

    cobalt = FluidFactory.metal(TinkerMaterials.cobalt);
    cobalt.setTemperature(950);
    cobalt.setRarity(EnumRarity.RARE);

    ardite = FluidFactory.metal(TinkerMaterials.ardite);
    ardite.setTemperature(860);
    ardite.setRarity(EnumRarity.RARE);

    manyullyn = FluidFactory.metal(TinkerMaterials.manyullyn);
    manyullyn.setTemperature(1000);
    manyullyn.setRarity(EnumRarity.RARE);

    knightslime = FluidFactory.metal(TinkerMaterials.knightslime);
    knightslime.setTemperature(520);
    knightslime.setRarity(EnumRarity.EPIC);

    alubrass = FluidFactory.metal(TinkerMaterials.alubrass);
    alubrass.setTemperature(500);
    
    alumite = FluidFactory.metal(TinkerMaterials.alumite);
    alumite.setTemperature(900);
    alumite.setRarity(EnumRarity.RARE);

    // Mod Integration fluids
    brass = FluidFactory.metal("brass", 0xede38b);
    brass.setTemperature(470);

    copper = FluidFactory.metal(TinkerMaterials.copper);
    copper.setTemperature(542);

    tin = FluidFactory.metal("tin", 0xc1cddc);
    tin.setTemperature(350);

    bronze = FluidFactory.metal(TinkerMaterials.bronze);
    bronze.setTemperature(475);

    zinc = FluidFactory.metal("zinc", 0xd3efe8);
    zinc.setTemperature(375);

    lead = FluidFactory.metal(TinkerMaterials.lead);
    lead.setTemperature(400);

    nickel = FluidFactory.metal("nickel", 0xc8d683);
    nickel.setTemperature(727);

    silver = FluidFactory.metal(TinkerMaterials.silver);
    silver.setTemperature(480);
    silver.setRarity(EnumRarity.RARE);

    electrum = FluidFactory.metal(TinkerMaterials.electrum);
    electrum.setTemperature(500);
    electrum.setRarity(EnumRarity.EPIC);

    steel = FluidFactory.metal(TinkerMaterials.steel);
    steel.setTemperature(681);

    aluminum = FluidFactory.metal("aluminum", 0xefe0d5);
    aluminum.setTemperature(330);

    // aleaciones de TC3 (tier 3). Se crean aquí, en el bloque estático, para que estén
    // disponibles ya en preinit y MaterialIntegration pueda enlazarlas con el material.
    slimesteel = FluidFactory.metal("slimesteel", 0x9dc0cc);
    slimesteel.setTemperature(800);
    slimesteel.setRarity(EnumRarity.RARE);

    rosegold = FluidFactory.metal("rosegold", 0xd08a7a);
    rosegold.setTemperature(600);
    rosegold.setRarity(EnumRarity.RARE);

    amethystbronze = FluidFactory.metal("amethystbronze", 0xa87bbd);
    amethystbronze.setTemperature(700);
    amethystbronze.setRarity(EnumRarity.RARE);

    // la amatista solo es insumo de la aleación, así que no lleva material asociado
    amethyst = FluidFactory.metal("amethyst", 0xa86fd4);
    amethyst.setTemperature(700);

    // aleaciones de TC3 (tier 4)
    queensslime = FluidFactory.metal("queensslime", 0x7fd85f);
    queensslime.setTemperature(950);
    queensslime.setRarity(EnumRarity.EPIC);

    hepatizon = FluidFactory.metal("hepatizon", 0xb08d57);
    hepatizon.setTemperature(900);
    hepatizon.setRarity(EnumRarity.EPIC);

    // insumos de esas aleaciones: crema de magma y cuarzo, ambos existen en vanilla 1.12
    magma = FluidFactory.metal("magma", 0xff960d);
    magma.setTemperature(400);

    quartz = FluidFactory.metal("quartz", 0xe8e3d5);
    quartz.setTemperature(700);

    // aleaciones de compat de TC3
    constantan = FluidFactory.metal("constantan", 0xc98f5c);
    constantan.setTemperature(700);

    invar = FluidFactory.metal("invar", 0xb8b8a8);
    invar.setTemperature(800);

    pewter = FluidFactory.metal("pewter", 0x9aa3a8);
    pewter.setTemperature(500);

    nicrosil = FluidFactory.metal("nicrosil", 0x8c93a8);
    nicrosil.setTemperature(950);
    nicrosil.setRarity(EnumRarity.RARE);

    // compuesto de TC3 como aleación: obsidiana + ardita
    darkthread = FluidFactory.metal("darkthread", 0x5a4672);
    darkthread.setTemperature(1000);

    // compuesto de TC3 como aleación: pigiron + knightslime
    jeweledhide = FluidFactory.metal("jeweledhide", 0xa86e98);
    jeweledhide.setTemperature(900);

    // metal de cinderslime: oro + slime ichor + ladrillo scorched
    cinderslime = FluidFactory.metal("cinderslime", 0xa5130c);
    cinderslime.setTemperature(950);
    cinderslime.setRarity(EnumRarity.RARE);

    // solo existen si sus mods están cargados; sin ellos el fluido se queda sin fuente
    scrap = FluidFactory.metal("scrap", 0x6b5a52);
    scrap.setTemperature(900);
    scrap.setRarity(EnumRarity.UNCOMMON);

    honey = FluidFactory.metal("honey", 0xe8a33c);
    honey.setTemperature(320);

    // netherite - material completo tier 4
    netherite = FluidFactory.metal("netherite", 0x443a3b);
    netherite.setTemperature(1250);
    netherite.setRarity(EnumRarity.EPIC);
  }

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {
    IForgeRegistry<Block> registry = event.getRegistry();

    if(isSmelteryLoaded()) {
      searedStone = FluidFactory.stone("stone", 0x777777);
      searedStone.setTemperature(800);
      registerMoltenBlock(registry, searedStone);

      // tier 2 de TC3: la piedra scorched tiene su propio fluido, la funde la foundry
      scorchedStone = FluidFactory.stone("scorched_stone", 0x3a2f2c);
      scorchedStone.setTemperature(800);
      registerMoltenBlock(registry, scorchedStone);

      obsidian = FluidFactory.stone(TinkerMaterials.obsidian.getIdentifier(), 0x2c0d59);
      obsidian.setTemperature(1000);
      registerMoltenBlock(registry, obsidian);

      clay = FluidFactory.stone("clay", 0xc67453);
      clay.setTemperature(700);
      registerMoltenBlock(registry, clay);

      dirt = FluidFactory.stone("dirt", 0xa68564);
      dirt.setTemperature(500);
      registerMoltenBlock(registry, dirt);
      
      calcium = FluidFactory.stone("notmilk", 0xcbc6a5);
      calcium.setTemperature(800);
      registerMoltenBlockPrefixless(registry, calcium);

      emerald = FluidFactory.metal("emerald", 0x58e78e);
      emerald.setTemperature(999);
      registerMoltenBlock(registry, emerald);

      diamond = FluidFactory.metal("diamond", 0x8cf4e2);
      diamond.setTemperature(999);
      registerMoltenBlock(registry, diamond);

      glass = FluidFactory.metal("glass", 0xc0f5fe);
      glass.setTemperature(625);
      registerMoltenBlock(registry, glass);

      registerMoltenBlock(registry, amethyst);
      registerMoltenBlock(registry, magma);
      registerMoltenBlock(registry, quartz);
      registerMoltenBlock(registry, scrap);
      registerMoltenBlock(registry, honey);

      // venom for the insect god
      venom = FluidFactory.poison("venom", 0xb9b566);
      venom.setTemperature(336);
      registerClassicBlock(registry, venom);

      // blood for the blood god
      blood = FluidFactory.classic("blood", 0x540000);
      blood.setTemperature(336);
      registerClassicBlock(registry, blood);
      
      // even more blood for the blood god
      blazingBlood = FluidFactory.blaze("blazing_blood");
      blazingBlood.setTemperature(1800);
      blazingBlood.setViscosity(6000);
      blazingBlood.setDensity(3500);
      blazingBlood.setLuminosity(14);
      blazingBlood.setRarity(EnumRarity.UNCOMMON);
      registerMoltenBlockPrefixless(registry, blazingBlood);
    }

    milk = FluidFactory.milk("milk", 0xffffff);
    milk.setTemperature(320);
    registerClassicBlock(registry, milk);

    if(isWorldLoaded() || isSmelteryLoaded()) {
      greenSlime = FluidFactory.slime("greenslime", 0x82c873);
      greenSlime.setTemperature(370);
      greenSlime.setViscosity(1600);
      greenSlime.setDensity(1600);
      registerBlock(registry, new BlockLiquidSlime(greenSlime, net.minecraft.block.material.Material.WATER), greenSlime.getName());
      
      blueslime = FluidFactory.slime("blueslime", 0xef67f0f5);
      blueslime.setTemperature(370);
      blueslime.setViscosity(1600);
      blueslime.setDensity(1600);
      registerBlock(registry, new BlockLiquidSlime(blueslime, net.minecraft.block.material.Material.WATER), blueslime.getName());
      
      purpleSlime = FluidFactory.slime("purpleslime", 0xefd236ff);
      purpleSlime.setTemperature(370);
      purpleSlime.setViscosity(1600);
      purpleSlime.setDensity(1600);
      registerBlock(registry, new BlockLiquidSlime(purpleSlime, net.minecraft.block.material.Material.WATER), purpleSlime.getName());

      ichor = FluidFactory.slime("ichor", 0xefea7c2c);
      ichor.setTemperature(370);
      ichor.setViscosity(1600);
      ichor.setDensity(1600);
      registerBlock(registry, new BlockLiquidSlime(ichor, net.minecraft.block.material.Material.WATER), ichor.getName());
    }
  }

  @SubscribeEvent
  public void registerItems(Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

      if(isSmelteryLoaded()) {
      FluidRegistry.addBucketForFluid(searedStone);

      FluidRegistry.addBucketForFluid(scorchedStone);

      FluidRegistry.addBucketForFluid(obsidian);

      FluidRegistry.addBucketForFluid(clay);

      FluidRegistry.addBucketForFluid(dirt);
      
      FluidRegistry.addBucketForFluid(calcium);

      FluidRegistry.addBucketForFluid(emerald);

      FluidRegistry.addBucketForFluid(diamond);

      FluidRegistry.addBucketForFluid(glass);

      FluidRegistry.addBucketForFluid(amethyst);

      FluidRegistry.addBucketForFluid(magma);

      FluidRegistry.addBucketForFluid(quartz);

      FluidRegistry.addBucketForFluid(scrap);

      FluidRegistry.addBucketForFluid(honey);

      // venom for the insect god
      FluidRegistry.addBucketForFluid(venom);

      // blood for the blood god
      FluidRegistry.addBucketForFluid(blood);
      
      // even more blood for the blood god
      FluidRegistry.addBucketForFluid(blazingBlood);
    }

    if(isWorldLoaded() || isSmelteryLoaded()) {
      FluidRegistry.addBucketForFluid(greenSlime);
      FluidRegistry.addBucketForFluid(blueslime);
      FluidRegistry.addBucketForFluid(purpleSlime);
      FluidRegistry.addBucketForFluid(ichor);
    }
  }

  @SubscribeEvent
  public void registerModels(ModelRegistryEvent event) {
    proxy.registerModels();
  }

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit();
  }

  @Subscribe
  public void init(FMLInitializationEvent event) {
    proxy.init();
  }

  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit();
  }


  /** Registers a non-burning water based block for the fluid */
  public static BlockFluidBase registerClassicBlock(IForgeRegistry<Block> registry, Fluid fluid) {
    return registerBlock(registry, new BlockTinkerFluid(fluid, net.minecraft.block.material.Material.WATER), fluid.getName());
  }

  /** Registers a hot lava-based block for the fluid, prefix with molten_ */
  public static BlockMolten registerMoltenBlock(IForgeRegistry<Block> registry, Fluid fluid) {
    return registerBlock(registry, new BlockMolten(fluid), "molten_" + fluid.getName()); // molten_foobar prefix
  }

  /** Registers a hot lava-based block for the fluid, no prefix */
  public static BlockFluidBase registerMoltenBlockPrefixless(IForgeRegistry<Block> registry, Fluid fluid) {
    return registerBlock(registry, new BlockMolten(fluid), fluid.getName());
  }
}
