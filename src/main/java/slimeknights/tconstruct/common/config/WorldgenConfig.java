package slimeknights.tconstruct.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import static slimeknights.tconstruct.common.config.Config.*;

/** Carga la categoria "Worldgen" del config. */
public final class WorldgenConfig {

  private WorldgenConfig() {
  }

  public static void loadConfig(Configuration configFile) {
    Property prop;

      String cat = "worldgen";
      Worldgen = configFile.getCategory(cat);

      // Slime Islands
      prop = configFile.get(cat, "generateSlimeIslands", genSlimeIslands);
      prop.setComment("If true, slime islands will generate.");
      genSlimeIslands = prop.getBoolean();

      prop = configFile.get(cat, "generateIslandsInSuperflat", genIslandsInSuperflat);
      prop.setComment("If true, slime islands generate in superflat worlds.");
      genIslandsInSuperflat = prop.getBoolean();

      prop = configFile.get(cat, "slimeIslandRate", slimeIslandsRate);
      prop.setComment("One in every X chunks will contain a slime island.");
      slimeIslandsRate = prop.getInt();

      prop = configFile.get(cat, "magmaIslandRate", magmaIslandsRate);
      prop.setComment("One in every X chunks will contain a magma island in the nether.");
      magmaIslandsRate = prop.getInt();

      configFile.renameProperty(cat, "slimeIslandBlacklist", "slimeIslandDimensions");

      prop = configFile.get(cat, "slimeIslandDimensions", slimeIslandBlacklist);
      prop.setComment("List of dimensions in which to enable or disable generation of slime islands.");
      slimeIslandBlacklist = prop.getIntList();

      prop = configFile.get(cat, "slimeIslandDimensionsIsBlacklist", slimeIslandDimensionsIsBlacklist);
      prop.setComment("Whether the list of slime island dimensions behaves as a blacklist or a whitelist.");
      slimeIslandDimensionsIsBlacklist = prop.getBoolean();

      prop = configFile.get(cat, "slimeIslandsOnlyGenerateInSurfaceWorlds", slimeIslandsOnlyGenerateInSurfaceWorlds);
      prop.setComment("If false, slime islands only generate in dimensions which are of type surface. This means they won't generate in modded cave dimensions like the Deep Dark. Note that the name of this property is inverted: It must be set to false to prevent slime islands from generating in non-surface dimensions.");
      slimeIslandsOnlyGenerateInSurfaceWorlds = prop.getBoolean();

      // Slime Pools
      prop = configFile.get(cat, "generateSlimePools", genSlimePools);
      prop.setComment("If true, slime pools will generate.");
      genSlimePools = prop.getBoolean();

      prop = configFile.get(cat, "slimePoolRate", slimePoolRate);
      prop.setComment("One in every X chunks will contain a slime pool.");
      slimePoolRate = prop.getInt();

      prop = configFile.get(cat, "slimePoolHeightMax", slimePoolHeightMax);
      prop.setComment("Maximum Y level for slime pool generation.");
      slimePoolHeightMax = prop.getInt();

      prop = configFile.get(cat, "slimePoolDimensions", slimePoolDimensions);
      prop.setComment("List of dimensions in which to enable or disable generation of slime pools.");
      slimePoolDimensions = prop.getIntList();

      prop = configFile.get(cat, "slimePoolDimensionsIsBlacklist", slimePoolDimensionsIsBlacklist);
      prop.setComment("Whether the list of slime pool dimensions behaves as a blacklist or a whitelist.");
      slimePoolDimensionsIsBlacklist = prop.getBoolean();

      prop = configFile.get(cat, "slimePoolsOnlyGenerateInSurfaceWorlds", slimePoolsOnlyGenerateInSurfaceWorlds);
      prop.setComment("If false, slime pools only generate in dimensions which are of type surface. This means they won't generate in modded cave dimensions like the Deep Dark. Note that the name of this property is inverted: It must be set to false to prevent slime pools from generating in non-surface dimensions.");
      slimePoolsOnlyGenerateInSurfaceWorlds = prop.getBoolean();

      // Villages
      prop = configFile.get(cat, "generateVillageStructures", genVillageStructures);
      prop.setComment("If true, Smelteries and Tool Workshops will generate in villages.");
      genVillageStructures = prop.getBoolean();

      // Ores
      prop = configFile.get(cat, "genCobalt", genCobalt);
      prop.setComment("If true, cobalt ore will generate in the nether.");
      genCobalt = prop.getBoolean();

      prop = configFile.get(cat, "genArdite", genArdite);
      prop.setComment("If true, ardite ore will generate in the nether.");
      genArdite = prop.getBoolean();

      prop = configFile.get(cat, "genCopper", genCopper);
      prop.setComment("If true, copper ore will generate in the overworld.");
      genCopper = prop.getBoolean();

      prop = configFile.get(cat, "genTin", genTin);
      prop.setComment("If true, tin ore will generate in the overworld.");
      genTin = prop.getBoolean();

      prop = configFile.get(cat, "genAluminum", genAluminum);
      prop.setComment("If true, aluminum ore will generate in the overworld.");
      genAluminum = prop.getBoolean();

      prop = configFile.get(cat, "cobaltRate", cobaltRate);
      prop.setComment("Approximate cobalt ore generation per chunk.");
      cobaltRate = prop.getInt();

      prop = configFile.get(cat, "arditeRate", arditeRate);
      prop.setComment("Approximate ardite ore generation per chunk.");
      arditeRate = prop.getInt();

      prop = configFile.get(cat, "copperRate", copperRate);
      prop.setComment("Approximate copper ore generation per chunk.");
      copperRate = prop.getInt();

      prop = configFile.get(cat, "copperHeightMin", copperHeightMin);
      prop.setComment("Minimum Y level for copper ore generation.");
      copperHeightMin = prop.getInt();

      prop = configFile.get(cat, "copperHeightMax", copperHeightMax);
      prop.setComment("Maximum Y level for copper ore generation.");
      copperHeightMax = prop.getInt();

      prop = configFile.get(cat, "tinRate", tinRate);
      prop.setComment("Approximate tin ore generation per chunk.");
      tinRate = prop.getInt();

      prop = configFile.get(cat, "tinHeightMin", tinHeightMin);
      prop.setComment("Minimum Y level for tin ore generation.");
      tinHeightMin = prop.getInt();

      prop = configFile.get(cat, "tinHeightMax", tinHeightMax);
      prop.setComment("Maximum Y level for tin ore generation.");
      tinHeightMax = prop.getInt();

      prop = configFile.get(cat, "aluminumRate", aluminumRate);
      prop.setComment("Approximate aluminum ore generation per chunk.");
      aluminumRate = prop.getInt();

      prop = configFile.get(cat, "aluminumHeightMin", aluminumHeightMin);
      prop.setComment("Minimum Y level for aluminum ore generation.");
      aluminumHeightMin = prop.getInt();

prop = configFile.get(cat, "aluminumHeightMax", aluminumHeightMax);
      prop.setComment("Maximum Y level for aluminum ore generation.");
      aluminumHeightMax = prop.getInt();
  }
}
