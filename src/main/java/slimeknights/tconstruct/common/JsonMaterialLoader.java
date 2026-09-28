package slimeknights.tconstruct.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class JsonMaterialLoader {
  private static final Logger log = Util.getLogger("JsonLoader");
  private static final Gson GSON = new Gson();
  private static final java.util.Map<String, JsonObject> CACHE = new java.util.HashMap<>();

  private static JsonObject getJson(String path, ClassLoader loader) {
    JsonObject cached = CACHE.get(path);
    if(cached != null) return cached;
    try (InputStream is = loader.getResourceAsStream(path)) {
      if(is == null) return null;
      JsonObject json = GSON.fromJson(new InputStreamReader(is), JsonObject.class);
      if(json != null) CACHE.put(path, json);
      return json;
    } catch(Exception e) {
      log.debug("Json load failed for " + path + ": " + e.getMessage());
      return null;
    }
  }

  public static void load() {
    int count = 0;
    ClassLoader loader = Loader.instance().activeModContainer().getMod().getClass().getClassLoader();
    for(Material mat : TinkerRegistry.getAllMaterials()) {
      String path = "assets/tconstruct/materials/" + mat.identifier + ".json";
      try {
        JsonObject json = getJson(path, loader);
        if(json == null) continue;
        if(json.has("stats")) {
          JsonObject stats = json.getAsJsonObject("stats");
          if(stats.has("head")) {
            JsonObject h = stats.getAsJsonObject("head");
            int dur = h.has("durability") ? h.get("durability").getAsInt() : 100;
            float speed = h.has("mining_speed") ? h.get("mining_speed").getAsFloat() : 1f;
            float atk = h.has("melee_attack") ? h.get("melee_attack").getAsFloat() : 1f;
            int lvl = 1;
            if(!mat.hasStats(HeadMaterialStats.TYPE)) {
              TinkerRegistry.addMaterialStats(mat, new HeadMaterialStats(dur, speed, atk, lvl));
              count++;
            }
          }
          if(stats.has("armor")) {
            JsonObject a = stats.getAsJsonObject("armor");
            int dur = a.has("durability") ? a.get("durability").getAsInt() : 100;
            float def = a.has("defense") ? a.get("defense").getAsFloat() : 1f;
            float tough = a.has("toughness") ? a.get("toughness").getAsFloat() : 0f;
            // a single "armor" block maps to every plating type (mirrors TinkerArmor.addMatStats)
            for(String type : new String[]{
                ArmorMaterialStats.TYPE_HELMET,
                ArmorMaterialStats.TYPE_CHESTPLATE,
                ArmorMaterialStats.TYPE_LEGGINGS,
                ArmorMaterialStats.TYPE_BOOTS,
                ArmorMaterialStats.TYPE_SHIELD,
                ArmorMaterialStats.TYPE_MAILLE}) {
              if(!mat.hasStats(type)) {
                int mult = ArmorMaterialStats.getDurabilityMultiplier(type);
                TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(type, dur * mult / 11, def, tough));
                count++;
              }
            }
          }
        }
      } catch(Exception e) {
        log.warn("Json load failed for " + mat.identifier + ": " + e.getMessage());
      }
    }
    log.info("JsonMaterialLoader: processed " + count + " json stats (compat with 1.20.1 data-packs)");
  }

  public static void loadDataPackStats() {
    // 1.20.1 compat: data/tconstruct/tinkering/materials/stats/*.json
    String base = "data/tconstruct/tinkering/materials/stats/";
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    int count = 0;
    for(Material mat : TinkerRegistry.getAllMaterials()) {
      JsonObject json = getJson(base + mat.identifier + ".json", loader);
      if(json != null && json.has("stats")) count++;
    }
    if(count > 0) log.info("Data-pack stats found: " + count + " (1.20.1 compat)");
  }
}
