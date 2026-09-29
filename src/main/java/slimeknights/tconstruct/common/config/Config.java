package slimeknights.tconstruct.common.config;

import com.google.common.collect.Sets;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.pulsar.config.ForgeCFG;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.utils.RecipeUtil;

import java.util.Collections;
import java.util.Set;

// NOTE: Be careful when changing any old config names as they can break certain addons or mods that add integration!
public final class Config {

  public static ForgeCFG pulseConfig = new ForgeCFG("TinkerModules", "Modules");
  public static Config instance = new Config();
  public static Logger log = Util.getLogger("Config");

  private Config() {
  }

  public static boolean forceRegisterAll = false; // enables all common items, even if their module is not present
  public static boolean registerAllCommonMetals = true; // enables all common metals (copper, tin, aluminum, bronze, and steel)
  
  // Tools and general
  public static boolean spawnWithBook = true;
  public static boolean reuseStencil = true;
  public static boolean craftCastableMaterials = false;
  public static boolean chestsKeepInventory = true;
  public static boolean autosmeltlapis = false;
  public static boolean obsidianAlloy = true;
  public static boolean steelAlloy = true;
  public static boolean claycasts = true;
  public static boolean castableBricks = true;
  public static boolean enchantability = true;
  public static boolean leatherDryingRecipe = true;
  public static boolean gravelFlintRecipe = true;
  public static double oreToIngotRatio = 2;
  public static int despawnProjectile = 1200;
  public static boolean matchVanillaSlimeblock = false;
  public static boolean limitPiggybackpack = false;
  public static boolean clearGlassSilkTouch = true;
  public static boolean drainGaseousFluids = true;
  public static int maxSmelteryItemRenders = -1;
  public static int netherOresMiningLevel = 4;
  public static boolean deconstructTools = true;
  public static int deconstructXPRequirement = 0;
  public static int deconstructLevelRequirement = 0;
  public static int heatItemsTickrateSmeltery = 4;
  public static int heatItemsTickrateSearedFurnace = 4;
  public static int liquidTransferRate = 6;
  public static boolean vanillaToolBreaking = false;
  public static boolean oldMattockAndKama = false;
  public static boolean jeiGuidebookButton = false;
  public static boolean repairToolsOnAnvils = false;

  static String[] craftingStationBlacklistArray = new String[] {
      "de.ellpeck.actuallyadditions.mod.tile.TileEntityItemViewer"
  };
  static String[] orePreference = {
      "minecraft",
      "tconstruct",
      "thermalfoundation",
      "forestry",
      "immersiveengineering",
      "embers",
      "ic2"
  };
  public static Set<String> craftingStationBlacklist = Collections.emptySet();
  public static String[] oredictMeltingIgnore = {
          "dustRedstone",
          "plankWood",
          "stickWood",
          "stickTreatedWood",
          "string",
          "minecraft:chest:0"
  };
  public static String[] materialIgnore = {
  };
  public static String[] mobHeadDrops = {
          "minecraft:skeleton;true;minecraft:skull:0",
          "minecraft:stray;true;minecraft:skull:0",
          "minecraft:wither_skeleton;true;minecraft:skull:1",
          "minecraft:zombie;true;minecraft:skull:2",
          "minecraft:player;false;minecraft:skull:3",
          "minecraft:creeper;true;minecraft:skull:4",
          "minecraft:ender_dragon;true;minecraft:skull:5",
          "minecraft:snowman;false;minecraft:pumpkin",
          "minecraft:villager_golem;false;minecraft:pumpkin",
          "mod_lavacow:boneworm;false;minecraft:skull:0",
          "mod_lavacow:forsaken;true;minecraft:skull:0",
          "mod_lavacow:skeletonking;false;minecraft:skull:0",
          "mod_lavacow:soulworm;false;minecraft:skull:1",
          "mod_lavacow:scarecrow;false;mod_lavacow:scarecrowhead_common",
          "techguns:armysoldier;false;minecraft:skull:3",
          "techguns:bandit;false;minecraft:skull:3",
          "techguns:commando;false;minecraft:skull:3",
          "techguns:dictatordave;false;minecraft:skull:3",
          "techguns:psychosteve;false;minecraft:skull:3",
          "techguns:stormtrooper;false;minecraft:skull:3",
          "techguns:zombiefarmer;true;minecraft:skull:2",
          "techguns:zombieminer;true;minecraft:skull:2",
          "techguns:zombiepoliceman;true;minecraft:skull:2",
          "techguns:zombiesoldier;true;minecraft:skull:2",
          "thaumcraft:CultistCleric;false;minecraft:skull:3",
          "thaumcraft:CultistKnight;false;minecraft:skull:3",
          "thaumcraft:CultistLeader;false;minecraft:skull:3",
          "tropicraft:tropiskeleton;false;minecraft:skull:0",
          "tropicraft:tropicreeper;false;minecraft:skull:4"
  };
  public static String[] entityMelting = {
          "minecraft:blaze;true;blazing_blood;20",
          "minecraft:evocation_illager;true;emerald;6",
          "minecraft:guardian;true;stone;50",
          "minecraft:illusion_illager;true;emerald;6",
          "minecraft:skeleton;true;notmilk;20",
          "minecraft:skeleton_horse;true;notmilk;20",
          "minecraft:silverfish;true;stone;25",
          "minecraft:slime;false;greenslime;25",
          "minecraft:snowman;true;water;100",
          "minecraft:spider;true;venom;20",
          "minecraft:cave_spider;true;venom;20",
          "minecraft:stray;true;notmilk;20",
          "minecraft:villager;true;emerald;6",
          "minecraft:villager_golem;true;iron;18",
          "minecraft:vindication_illager;true;emerald;6",
          "minecraft:wither_skeleton;true;notmilk;20",
          "minecraft:zombie_pigman;true;gold;10",
          "tconstruct:blueslime;false;blueslime;25",
          "tconstruct:purpleslime;false;purpleslime;25",
          "battletowers:golem;false;stone;100",
          "mocreatures:Bee;false;venom;3",
          "mocreatures:BigGolem;false;stone;50",
          "mocreatures:CaveOgre;false;stone;50",
          "mocreatures:CaveScorpion;false;venom;20",
          "mocreatures:DarkManticore;false;venom;20",
          "mocreatures:DirtScorpion;false;venom;20",
          "mocreatures:FireManticore;false;blazing_blood;20",
          "mocreatures:FireOgre;false;blazing_blood;40",
          "mocreatures:FireScorpion;false;blazing_blood;20",
          "mocreatures:FrostManticore;false;venom;20",
          "mocreatures:FrostScorpion;false;venom;20",
          "mocreatures:GreenOgre;false;emerald;12",
          "mocreatures:HellRat;false;blazing_blood;20",
          "mocreatures:JellyFish;false;blueslime;6",
          "mocreatures:KomodoDragon;false;venom;20",
          "mocreatures:MiniGolem;false;ancient_silver;12",
          "mocreatures:PlainManticore;false;venom;20",
          "mocreatures:SilverSkeleton;false;ancient_silver;12",
          "mocreatures:Snail;false;greenslime;3",
          "mocreatures:Snake;false;venom;10",
          "mocreatures:StingRay;false;venom;10",
          "mocreatures:ToxicManticore;false;venom;20",
          "mocreatures:UndeadScorpion;false;venom;20",
          "mod_lavacow:boneworm;true;notmilk;20",
          "mod_lavacow:forsaken;true;notmilk;20",
          "mod_lavacow:grave_robber;false;emerald;6",
          "mod_lavacow:imp;false;blazing_blood;20",
          "mod_lavacow:lavacow;false;lava;20",
          "mod_lavacow:lilsludge;false;blueslime;25",
          "mod_lavacow:salamander;false;blazing_blood;40",
          "mod_lavacow:skeletonking;false;notmilk;40",
          "mod_lavacow:sludgelord;false;blueslime;25",
          "mod_lavacow:vespa;false;venom;40",
          "mod_lavacow:zombiemushroom;false;venom;20",
          "natura:babyheatscarspider;false;blazing_blood;20",
          "natura:heatscarspider;false;blazing_blood;40",
          "thaumcraft:Firebat;false;blazing_blood;5",
          "thaumcraft:Pech;true;gold;10",
          "tropicraft:tropiskeleton;true;notmilk;20"
  };
  public static String[] materialPriorities = {
          "tconstruct"
  };
  public static String[] entityJEIRendererTransformation = {
          "minecraft:ender_dragon;5.0"
  };
  public static String[] fluidIgnore = {};
  public static String[] incognitoModBlacklist = {
          "night_vision_armor",
          "potion_belt_armor",
          "soul_sight_armor",
          "travel_belt_armor",
          "travel_goggles_armor",
          "travel_sack_armor",
          "travel_slowfall_armor",
          "travel_sneak_armor"
  };

  // Worldgen
  public static boolean genSlimeIslands = true;
  public static boolean genIslandsInSuperflat = false;
  public static int slimeIslandsRate = 730; // Every x-th chunk will have a slime island. so 1 = every chunk, 100 = every 100th
  public static int magmaIslandsRate = 100; // Every x-th chunk will have a slime island. so 1 = every chunk, 100 = every 100th
  public static int[] slimeIslandBlacklist = new int[]{-1, 1};
  public static boolean slimeIslandDimensionsIsBlacklist = true;
  public static boolean slimeIslandsOnlyGenerateInSurfaceWorlds = true;
  public static boolean genSlimePools = false;
  public static int slimePoolRate = 30;
  public static int magmaPoolRate = 30;
  public static int slimePoolHeightMax = 64;
  public static int[] slimePoolDimensions = new int[]{-1, 1};
  public static boolean slimePoolDimensionsIsBlacklist = true;
  public static boolean slimePoolsOnlyGenerateInSurfaceWorlds = true;
  public static boolean genVillageStructures = true;

  public static boolean genCobalt = true;
  public static int cobaltRate = 20; // max. cobalt per chunk
  public static boolean genArdite = true;
  public static int arditeRate = 20; // max. ardite per chunk
  public static boolean genCopper = true;
  public static int copperRate = 8;
  public static int copperHeightMin = 20;
  public static int copperHeightMax = 60;
  public static boolean genTin = true;
  public static int tinRate = 8;
  public static int tinHeightMin = 0;
  public static int tinHeightMax = 40;
  public static boolean genAluminum = true;
  public static int aluminumRate = 8;
  public static int aluminumHeightMin = 0;
  public static int aluminumHeightMax = 64;

  // Clientside configs
  public static boolean renderTableItems = true;
  public static boolean renderInventoryNullLayer = true;
  public static boolean extraTooltips = true;
  public static boolean listAllTables = true;
  public static boolean listAllToolMaterials = true;
  public static boolean listAllPartMaterials = true;
  public static boolean enableForgeBucketModel = true; // enables the forge bucket model by default
  public static boolean dumpTextureMap = false; // requires debug module
  public static boolean testIMC = false; // requires debug module
  public static boolean temperatureCelsius = true;
  public static boolean disableAllParticles = false;
  public static boolean fancyJEIBeheadingAnimation = true;
  public static int minFluidHeight = 3;
  public static int columnsPartBuilder = 4;
  public static int columnsStencilTable = 4;
  public static int columnsToolStation = 5;

  /* Config File */

  static Configuration configFile;

  static ConfigCategory Modules;
  static ConfigCategory Gameplay;
  static ConfigCategory Worldgen;
  static ConfigCategory Experimental;
  static ConfigCategory ClientSide;
  
  public static void load(FMLPreInitializationEvent event) {
    configFile = new Configuration(event.getSuggestedConfigurationFile(), "0.4", false);

    MinecraftForge.EVENT_BUS.register(instance);

    syncConfig();
  }

  @SubscribeEvent
  public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
    if(event.getModID().equals(TConstruct.modID)) {
      syncConfig();
    }
  }


  public static boolean syncConfig() {
    // Modules
    {
      Modules = pulseConfig.getCategory();
    }
    GameplayConfig.loadConfig(configFile);
    WorldgenConfig.loadConfig(configFile);
    ExperimentalConfig.loadConfig(configFile);
    ClientSideConfig.loadConfig(configFile);

    // save changes if any
    boolean changed = false;
    if(configFile.hasChanged()) {
      configFile.save();
      changed = true;
    }
    if(pulseConfig.getConfig().hasChanged()) {
      pulseConfig.flush();
      changed = true;
    }
    return changed;
  }
}
