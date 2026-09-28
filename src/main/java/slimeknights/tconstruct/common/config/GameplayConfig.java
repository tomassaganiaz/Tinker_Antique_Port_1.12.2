package slimeknights.tconstruct.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.google.common.collect.Sets;

import slimeknights.tconstruct.library.utils.RecipeUtil;

import static slimeknights.tconstruct.common.config.Config.*;

/** Carga la categoria "Gameplay" del config. */
public final class GameplayConfig {

  private GameplayConfig() {
  }

  public static void loadConfig(Configuration configFile) {
    Property prop;

      String cat = "gameplay";
      Gameplay = configFile.getCategory(cat);

      prop = configFile.get(cat, "spawnWithBook", spawnWithBook);
      prop.setComment("Players who enter the world for the first time get a Tinkers' Book.");
      spawnWithBook = prop.getBoolean();

      prop = configFile.get(cat, "reuseStencils", reuseStencil);
      prop.setComment("Allows to reuse stencils in the stencil table to turn them into other stencils.");
      reuseStencil = prop.getBoolean();

      prop = configFile.get(cat, "chestsKeepInventory", chestsKeepInventory);
      prop.setComment("Pattern and Part chests keep their inventory when harvested.");
      chestsKeepInventory = prop.getBoolean();

      prop = configFile.get(cat, "enableClayCasts", claycasts);
      prop.setComment("Adds single-use clay casts.");
      claycasts = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "allowBrickCasting", castableBricks);
      prop.setComment("Allows the creation of bricks from molten clay.");
      castableBricks = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "autosmeltFortuneInteraction", autosmeltlapis);
      prop.setComment("Fortune increases drops after harvesting a block with autosmelt.");
      autosmeltlapis = prop.getBoolean();

      prop = configFile.get(cat, "craftCastableMaterials", craftCastableMaterials);
      prop.setComment("Allows to craft all tool parts of all materials in the part builder, including materials that normally have to be cast with a smeltery.");
      craftCastableMaterials = prop.getBoolean();

      prop = configFile.get(cat, "registerAllItems", forceRegisterAll);
      prop.setComment("Enables all items, even if the Module needed to obtain them is not active.");
      forceRegisterAll = prop.getBoolean();
      prop.setRequiresMcRestart(true);
      
      prop = configFile.get(cat, "registerAllCommonMetals", registerAllCommonMetals);
      prop.setComment("Enables all common metals (copper, tin, aluminum, bronze, and steel) for ingots, nuggets, ores, and metal blocks. Disable if you want to rely on metals added by third party mods instead.");
      registerAllCommonMetals = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "obsidianAlloy", obsidianAlloy);
      prop.setComment("Allows the creation of obsidian in the smeltery, using a bucket of lava and water.");
      obsidianAlloy = prop.getBoolean();
      prop.setRequiresMcRestart(true);
      
      prop = configFile.get(cat, "steelAlloy", steelAlloy);
      prop.setComment("Allows the creation of steel by pouring Blazin' Blood on iron ingots or blocks on a casting table or basin. Note that this will always be disabled if the steel material added by Tinkers' Antique is also disabled.");
      obsidianAlloy = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "addLeatherDryingRecipe", leatherDryingRecipe);
      prop.setComment("Adds a recipe that allows you to get leather from drying cooked meat.");
      leatherDryingRecipe = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "addFlintRecipe", gravelFlintRecipe);
      prop.setComment("Adds a recipe that allows you to craft 3 gravel into a flint.");
      gravelFlintRecipe = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "oreToIngotRatio", oreToIngotRatio);
      prop.setComment("Determines the ratio of ore to ingot, or in other words how many ingots you get out of an ore. This ratio applies to all ores (including poor and dense). The ratio can be any decimal, including 1.5 and the like, but can't go below 1. THIS ALSO AFFECTS MELTING TEMPERATURE!");
      prop.setMinValue(1);
      oreToIngotRatio = prop.getDouble();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "matchVanillaSlimeblock", matchVanillaSlimeblock);
      prop.setComment("If true, requires slimeballs in the vanilla slimeblock recipe to match in color, otherwise gives a pink slimeblock.");
      matchVanillaSlimeblock = prop.getBoolean();
      prop.setRequiresMcRestart(true);

      prop = configFile.get(cat, "limitPiggybackpack", limitPiggybackpack);
      prop.setComment("If true, piggybackpacks can only pick up players and mobs that can be leashed in vanilla. If false any mob can be picked up.");
      limitPiggybackpack = prop.getBoolean();

      prop = configFile.get(cat, "clearGlassSilkTouch", clearGlassSilkTouch);
      prop.setComment("If true, clear glass can only be harvested with silk touch like regular glass.");
      clearGlassSilkTouch = prop.getBoolean();

      prop = configFile.get(cat, "despawnProjectile", despawnProjectile);
      prop.setComment("How many ticks projectiles are allowed on the ground until they despawn.");
      despawnProjectile = prop.getInt();

      prop = configFile.get(cat, "craftingStationBlacklist", craftingStationBlacklistArray);
      prop.setComment("Blacklist of registry names or TE classnames for the crafting station to connect to. Mainly for compatibility.");
      craftingStationBlacklistArray = prop.getStringList();
      craftingStationBlacklist = Sets.newHashSet(craftingStationBlacklistArray);

      prop = configFile.get(cat, "orePreference", orePreference);
      prop.setComment("Preferred mod ID for oredictionary outputs. Top most mod ID will be the preferred output ID, and if none is found the first output stack is used.");
      orePreference = prop.getStringList();
      RecipeUtil.setOrePreferences(orePreference);

      prop = configFile.get(cat, "oredictMeltingIgnore", oredictMeltingIgnore);
      prop.setComment("List of items to ignore when generating melting recipes from the crafting registry. For example, ignoring sticks allows metal pickaxes to melt down.\nFormat: oreName or modid:item[:meta]. If meta is unset, uses wildcard.");
      oredictMeltingIgnore = prop.getStringList();

      prop = configFile.get(cat, "testIMC", testIMC);
      prop.setComment("REQUIRES DEBUG MODULE. Tests all IMC integrations with dummy recipes. May significantly impact gameplay, so its advised you disable this outside of dev environments.");
      testIMC = prop.getBoolean();

      prop = configFile.get(cat, "materialIgnore", materialIgnore);
      prop.setComment("List of materials to ignore, effectively preventing registration.");
      materialIgnore = prop.getStringList();

      prop = configFile.get(cat, "fluidIgnore", fluidIgnore);
      prop.setComment("List of fluids to ignore, effectively preventing registration of melting and casting recipes.");
      fluidIgnore = prop.getStringList();

      prop = configFile.get(cat, "drainGaseousFluids", drainGaseousFluids);
      prop.setComment("If gaseous fluids are being transferable via faucets.");
      drainGaseousFluids = prop.getBoolean();

      prop = configFile.get(cat, "maxSmelteryItemRenders", maxSmelteryItemRenders);
      prop.setComment("Determines the maximum number of possible items to display before not rendering any to prevent substantial lag. 0 to disable rendering items in the smeltery entirely. -1 for the default, which is always rendering items.");
      maxSmelteryItemRenders = prop.getInt();

      prop = configFile.get(cat, "netherOresMiningLevel", netherOresMiningLevel);
      prop.setComment("The mining level for ardite and cobalt ores.");
      netherOresMiningLevel = prop.getInt();

      prop = configFile.get(cat, "deconstructTools", deconstructTools);
      prop.setComment("If tools can be deconstructed in tool stations and tool forges by putting them into output slots.");
      deconstructTools = prop.getBoolean();

      prop = configFile.get(cat, "deconstructXPRequirement", deconstructXPRequirement);
      prop.setComment("The XP requirement for deconstructing tools (if provided by Tinkers' Tool Leveling).");
      deconstructXPRequirement = prop.getInt();

      prop = configFile.get(cat, "deconstructLevelRequirement", deconstructLevelRequirement);
      prop.setComment("The level requirement for deconstructing tools (if provided by Tinkers' Tool Leveling).");
      deconstructLevelRequirement = prop.getInt();

      prop = configFile.get(cat, "heatItemsTickrateSmeltery", heatItemsTickrateSmeltery);
      prop.setComment("The tickrate at which items are heated and alloys are created in the smeltery. Defaults to every 4th tick.");
      heatItemsTickrateSmeltery = prop.getInt();

      prop = configFile.get(cat, "heatItemsTickrateSearedFurnace", heatItemsTickrateSearedFurnace);
      prop.setComment("The tickrate at which items are heated in the seared furnace. Defaults to every 4th tick.");
      heatItemsTickrateSearedFurnace = prop.getInt();

      prop = configFile.get(cat, "liquidTransferRate", liquidTransferRate);
      prop.setComment("How much liquid is transferred by faucets and channels per pouring operation.");
      liquidTransferRate = prop.getInt();

      prop = configFile.get(cat, "oldMattockAndKama", oldMattockAndKama);
      prop.setComment("Restores old Mattock and Kama behavior (Mattock usable as a hoe, Kama is not)");
      oldMattockAndKama = prop.getBoolean();

      prop = configFile.get(cat, "mobHeadDrops", mobHeadDrops);
      prop.setComment("List of mob head drops in the format 'modid:entity;subtypes;modid:item[:metadata][;max_quantity]'. Example: 'minecraft:skeleton;true;minecraft:skull:0' or 'minecraft:chicken;false;minecraft:feather;2'");
      mobHeadDrops = prop.getStringList();

      prop = configFile.get(cat, "entityMelting", entityMelting);
      prop.setComment("List of entity melting entries in the format 'modid:entity;subtypes;fluid;amount'.");
      entityMelting = prop.getStringList();

      prop = configFile.get(cat, "materialPriorities", materialPriorities);
      prop.setComment("List of mod IDs for material registration with descending priority. Highest mod ID wins!");
      materialPriorities = prop.getStringList();
  }
}
