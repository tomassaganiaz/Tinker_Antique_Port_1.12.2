package slimeknights.tconstruct.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import net.minecraftforge.common.ForgeModContainer;

import slimeknights.tconstruct.library.Util;

import static slimeknights.tconstruct.common.config.Config.*;

/** Carga la categoria "ClientSide" del config. */
public final class ClientSideConfig {

  private ClientSideConfig() {
  }

  public static void loadConfig(Configuration configFile) {
    Property prop;

      String cat = "clientside";
      ClientSide = configFile.getCategory(cat);

      // rename renderTableItems to renderInventoryInWorld
      configFile.renameProperty(cat, "renderTableItems", "renderInventoryInWorld");

      prop = configFile.get(cat, "renderInventoryInWorld", renderTableItems);
      prop.setComment("If true, all of Tinkers' blocks with contents (tables, basin, drying racks,...) will render their contents in the world.");
      renderTableItems = prop.getBoolean();

      prop = configFile.get(cat, "renderInventoryNullLayer", renderInventoryNullLayer);
      prop.setComment("If true, use a null render layer when building the models to render tables. Fixes an issue with chisel, but the config is provide in case it breaks something.");
      renderInventoryNullLayer = prop.getBoolean();

      prop = configFile.get(cat, "extraTooltips", extraTooltips);
      prop.setComment("If true, tools will show additional info in their tooltips.");
      extraTooltips = prop.getBoolean();

      prop = configFile.get(cat, "listAllTables", listAllTables);
      prop.setComment("If true, all variants of the different tables will be listed in creative. Set to false to only have the oak variant for all tables.");
      listAllTables = prop.getBoolean();

      configFile.renameProperty(cat, "listAllMaterials", "listAllToolMaterials");
      prop = configFile.get(cat, "listAllToolMaterials", listAllToolMaterials);
      prop.setComment("If true, all material variants of the different tools will be listed in creative. Set to false to only have the first found material for all tools (usually wood).");
      listAllToolMaterials = prop.getBoolean();

      prop = configFile.get(cat, "listAllPartMaterials", listAllToolMaterials); // property was split, so default to the value of tool materials
      prop.setComment("If true, all material variants of the different parts will be listed in creative. Set to false to only have the first found material for all parts (usually wood).");
      listAllPartMaterials = prop.getBoolean();

      prop = configFile.get(cat, "temperatureCelsius", temperatureCelsius);
      prop.setComment("If true, temperatures in the smeltery and in JEI will display in celsius. If false, they will use the internal units of Kelvin, which may be better for devs.");
      temperatureCelsius = prop.getBoolean();
      Util.setTemperaturePref(temperatureCelsius);

      prop = configFile.get(cat, "minFluidHeight", minFluidHeight);
      prop.setComment("Minimum fluid height to display in the smeltery, great for users that need an easier time to visually identify fluids in the smeltery interface. This can make the smeltery appear more full than it actually is, only touch this if you know what you're doing.");
      prop.setMinValue(3);
      prop.setMaxValue(8);
      minFluidHeight = prop.getInt();

      prop = configFile.get(cat, "enableForgeBucketModel", enableForgeBucketModel);
      prop.setComment("If true, tools will enable the forge bucket model on startup and then turn itself off. This is only there so that a fresh install gets the buckets turned on by default.");
      enableForgeBucketModel = prop.getBoolean();
      if(enableForgeBucketModel) {
        prop.set(false);
        ForgeModContainer.replaceVanillaBucketModel = true;
        Property forgeProp = ForgeModContainer.getConfig().getCategory(Configuration.CATEGORY_CLIENT).get("replaceVanillaBucketModel");
        if(forgeProp != null) {
          forgeProp.set(true);
          ForgeModContainer.getConfig().save();
        }
      }

      prop = configFile.get(cat, "dumpTextureMap", dumpTextureMap);
      prop.setComment("REQUIRES DEBUG MODULE. Will do nothing if debug module is disabled. If true the texture map will be dumped into the run directory, just like old forge did.");
      dumpTextureMap = prop.getBoolean();

      prop = configFile.get(cat, "columnsPartBuilder", columnsPartBuilder);
      prop.setComment("The column count of buttons in part builder GUIs.");
      columnsPartBuilder = prop.getInt();

      prop = configFile.get(cat, "columnsStencilTable", columnsStencilTable);
      prop.setComment("The column count of buttons in stencil table GUIs.");
      columnsStencilTable = prop.getInt();

      prop = configFile.get(cat, "columnsToolStation", columnsToolStation);
      prop.setComment("The column count of buttons in tool station GUIs.");
      columnsToolStation = prop.getInt();

      prop = configFile.get(cat, "disableAllParticles", disableAllParticles);
      prop.setComment("If true, disables all mod-specific particles to display.");
      disableAllParticles = prop.getBoolean();
      
      prop = configFile.get(cat, "fancyJEIBeheadingAnimation", fancyJEIBeheadingAnimation);
      prop.setComment("If true, the JEI tab for beheading will use a fancy animation.");
      fancyJEIBeheadingAnimation = prop.getBoolean();

      prop = configFile.get(cat, "entityJEIRendererScaleFactor", entityJEIRendererTransformation);
      prop.setComment("List of entity IDs that needs to be scaled when rendered in a GUI in the format 'modid:entity;scale'");
      entityJEIRendererTransformation = prop.getStringList();

      prop = configFile.get(cat, "incognitoModBlacklist", incognitoModBlacklist);
      prop.setComment("Modifiers that are still displayed despite an Incognito modifier being applied");
      incognitoModBlacklist = prop.getStringList();
  }
}
