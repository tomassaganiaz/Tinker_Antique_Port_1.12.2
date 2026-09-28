package slimeknights.tconstruct.library;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.materials.ProjectileMaterialStats;
import slimeknights.tconstruct.library.traits.ITrait;

import static slimeknights.tconstruct.library.TinkerRegistry.log;

/** Registro de materiales, sus stats, traits e integraciones. */
public final class MaterialRegistry {

  private MaterialRegistry() {
  }

  // Identifier to Material mapping. Hashmap so we can look it up directly without iterating
  private static final Map<String, Material> materials = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<String, ITrait> traits = new Object2ObjectOpenHashMap<>();
  // traceability information who registered what. Used to find errors.
  private static final Map<String, ModContainer> materialRegisteredByMod = new Object2ObjectOpenHashMap<>();
  private static final Map<String, Map<String, ModContainer>> statRegisteredByMod = new Object2ObjectOpenHashMap<>();
  private static final Map<String, Map<String, ModContainer>> traitRegisteredByMod = new Object2ObjectOpenHashMap<>();
  // contains all cancelled materials, allows us to eat calls regarding the material silently
  private static final Set<String> cancelledMaterials = new ObjectOpenHashSet<>();
  private static final List<MaterialIntegration> materialIntegrations = new ArrayList<>();

  public static void addMaterial(Material material, IMaterialStats stats, ITrait trait) {
    addMaterial(material, stats);
    addMaterialTrait(material.identifier, trait, null);
  }

  public static void addMaterial(Material material, ITrait trait) {
    addMaterial(material);
    addMaterialTrait(material.identifier, trait, null);
  }

  public static void addMaterial(Material material, IMaterialStats stats) {
    addMaterial(material);
    addMaterialStats(material.identifier, stats);
  }

  public static void addMaterial(Material material) {
    // ensure material identifiers are safe
    if(CharMatcher.whitespace().matchesAnyOf(material.getIdentifier())) {
      log.fatal("Could not register material \"{}\": Material identifier must not contain any spaces.", material.identifier);
      return;
    }
    if(CharMatcher.javaUpperCase().matchesAnyOf(material.getIdentifier())) {
      log.fatal("Could not register material \"{}\": Material identifier must be completely lowercase.", material.identifier);
      return;
    }

    // duplicate material
    if(materials.containsKey(material.identifier)) {
      ModContainer currentMod = Loader.instance().activeModContainer();
      String currentModId = currentMod != null ? currentMod.getModId() : "unknown";
      ModContainer registeredMod = getTrace(material);
      String registeredModId = registeredMod != null ? registeredMod.getModId() : "unknown";
      // compare priorities based on config
      int currentPriority = getModPriority(currentModId);
      int registeredPriority = getModPriority(registeredModId);
      if(currentPriority < registeredPriority) {
        // current mod has higher priority (lower index), replace existing material
        log.warn("Replacing material \"{}\" with material from {} ({})",
                material.identifier, currentMod.getName(), currentModId);
        // remove existing material
        materials.remove(material.identifier);
        materialRegisteredByMod.remove(material.identifier);
      } else {
        // existing mod has higher priority, reject new material
        log.fatal("Could not register material \"{}\" from {} ({}): It was already registered by {} ({})",
                material.identifier, currentMod.getName(), currentModId, registeredMod.getName(), registeredModId);
        return;
      }
    }

    MaterialEvent.MaterialRegisterEvent event = new MaterialEvent.MaterialRegisterEvent(material);

    if(MinecraftForge.EVENT_BUS.post(event)) {
      // event cancelled
      log.trace("Addition of material \"{}\" cancelled by event", material.getIdentifier());
      cancelledMaterials.add(material.getIdentifier());
      return;
    }

    // ignored material
    if(Arrays.stream(Config.materialIgnore).anyMatch(mat -> mat.equals(material.getIdentifier()))) {
      log.trace("Addition of material \"{}\" ignored by config", material.getIdentifier());
      cancelledMaterials.add(material.getIdentifier());
      return;
    }

    // register material
    materials.put(material.identifier, material);
    putMaterialTrace(material.identifier);
  }

  public static Material getMaterial(String identifier) {
    return materials.containsKey(identifier) ? materials.get(identifier) : Material.UNKNOWN;
  }

  public static Collection<Material> getAllMaterials() {
    return ImmutableList.copyOf(materials.values());
  }

  public static void removeHiddenMaterials() {
    materials.entrySet().removeIf(entry->entry.getValue().isHidden());
  }

  public static Collection<Material> getAllMaterialsWithStats(String statType) {
    ImmutableList.Builder<Material> mats = ImmutableList.builder();
    for(Material material : materials.values()) {
      if(material.hasStats(statType)) {
        mats.add(material);
      }
    }

    return mats.build();
  }

  public static void addTrait(ITrait trait) {
    // Trait might already have been registered since modifiers and materials share traits
    if(traits.containsKey(trait.getIdentifier())) {
      return;
    }

    traits.put(trait.getIdentifier(), trait);

    ModContainer activeMod = Loader.instance().activeModContainer();
    putTraitTrace(trait.getIdentifier(), trait, activeMod);
  }

  public static void addMaterialStats(String materialIdentifier, IMaterialStats stats) {
    if(cancelledMaterials.contains(materialIdentifier)) {
      return;
    }
    if(!materials.containsKey(materialIdentifier)) {
      log.fatal("Could not add Stats \"{}\" to \"{}\": Unknown Material", stats.getIdentifier(), materialIdentifier);
      return;
    }

    Material material = materials.get(materialIdentifier);
    addMaterialStats(material, stats);
  }

  public static void addMaterialStats(Material material, IMaterialStats stats, IMaterialStats... stats2) {
    addMaterialStats(material, stats);
    for(IMaterialStats stat : stats2) {
      addMaterialStats(material, stat);
    }
  }

  public static void addMaterialStats(Material material, IMaterialStats stats) {
    if(material == null) {
      log.fatal("Could not add Stats \"{}\": Material is null", stats.getIdentifier());
      return;
    }
    if(cancelledMaterials.contains(material.identifier)) {
      return;
    }

    String identifier = material.identifier;
    // duplicate stats
    if(material.getStats(stats.getIdentifier()) != null) {
      String registeredBy = "Unknown";
      Map<String, ModContainer> matReg = statRegisteredByMod.get(identifier);
      if(matReg != null) {
        registeredBy = matReg.get(stats.getIdentifier()).getName();
      }

      log.fatal("Could not add Stats to \"{}\": Stats of type \"{}\" were already registered by {}. Use the events to modify stats.", identifier, stats.getIdentifier(), registeredBy);
      return;
    }

    // ensure there are default stats present
    if(Material.UNKNOWN.getStats(stats.getIdentifier()) == null) {
      log.fatal("Could not add Stat of type \"{}\": Default Material does not have default stats for said type. Please add default-values to the default material \"unknown\" first.", stats
          .getIdentifier());
      return;
    }

    MaterialEvent.StatRegisterEvent<?> event = new MaterialEvent.StatRegisterEvent<>(material, stats);
    MinecraftForge.EVENT_BUS.post(event);

    // overridden stats from event
    if(event.getResult() == Event.Result.ALLOW) {
      stats = event.newStats;
    }

    material.addStats(stats);

    ModContainer activeMod = Loader.instance().activeModContainer();
    putStatTrace(identifier, stats, activeMod);

    if(Objects.equals(stats.getIdentifier(), MaterialTypes.HEAD) && !material.hasStats(MaterialTypes.PROJECTILE)) {
      addMaterialStats(material, new ProjectileMaterialStats());
    }
  }

  public static void addMaterialTrait(String materialIdentifier, ITrait trait, String stats) {
    if(cancelledMaterials.contains(materialIdentifier)) {
      return;
    }
    if(!materials.containsKey(materialIdentifier)) {
      log.fatal("Could not add Trait \"{}\" to \"{}\": Unknown Material", trait.getIdentifier(), materialIdentifier);
      return;
    }

    Material material = materials.get(materialIdentifier);
    addMaterialTrait(material, trait, stats);
  }

  public static void addMaterialTrait(Material material, ITrait trait, String stats) {
    if(checkMaterialTrait(material, trait, stats)) {
      material.addTrait(trait);
    }
  }

  public static boolean checkMaterialTrait(Material material, ITrait trait, String stats) {
    if(material == null) {
      log.fatal("Could not add Trait \"{}\": Material is null", trait.getIdentifier());
      return false;
    }
    if(cancelledMaterials.contains(material.identifier)) {
      return false;
    }

    String identifier = material.identifier;
    // duplicate traits
    if(material.hasTrait(trait.getIdentifier(), stats)) {
      String registeredBy = "Unknown";
      Map<String, ModContainer> matReg = traitRegisteredByMod.get(identifier);
      if(matReg != null) {
        registeredBy = matReg.get(trait.getIdentifier()).getName();
      }

      log.fatal("Could not add Trait to \"{}\": Trait \"{}\" was already registered by {}", identifier, trait.getIdentifier(), registeredBy);
      return false;
    }

    MaterialEvent.TraitRegisterEvent<?> event = new MaterialEvent.TraitRegisterEvent<>(material, trait);
    if(MinecraftForge.EVENT_BUS.post(event)) {
      // cancelled
      log.trace("Trait {} on {} cancelled by event", trait.getIdentifier(), material.getIdentifier());
      return false;
    }

    addTrait(trait);

    return true;
  }

  public static ITrait getTrait(String identifier) {
    return traits.get(identifier);
  }

  public static MaterialIntegration integrate(Material material) {
    return integrate(new MaterialIntegration(material));
  }

  public static MaterialIntegration integrate(Material material, Fluid fluid) {
    return integrate(new MaterialIntegration(material, fluid));
  }

  public static MaterialIntegration integrate(Material material, String oreRequirement) {
    MaterialIntegration materialIntegration = new MaterialIntegration(oreRequirement, material, null, null);
    materialIntegration.setRepresentativeItem(oreRequirement);
    return integrate(materialIntegration);
  }

  public static MaterialIntegration integrate(Material material, Fluid fluid, String oreSuffix) {
    return integrate(new MaterialIntegration(material, fluid, oreSuffix));
  }

  public static MaterialIntegration integrate(Fluid fluid, String oreSuffix) {
    return integrate(new MaterialIntegration(null, fluid, oreSuffix));
  }

  public static MaterialIntegration integrate(MaterialIntegration materialIntegration) {
    MaterialEvent.IntegrationEvent event = new MaterialEvent.IntegrationEvent(materialIntegration.material, materialIntegration);
    if(MinecraftForge.EVENT_BUS.post(event)) {
      // cancelled
      log.debug("Registration of material integration for material " + materialIntegration.material + " has been cancelled by event");
    }
    else {
      materialIntegrations.add(materialIntegration);
    }

    return materialIntegration;
  }

  public static List<MaterialIntegration> getMaterialIntegrations() {
    return ImmutableList.copyOf(materialIntegrations);
  }

  static int getModPriority(String modId) {
    for(int i = 0; i < Config.materialPriorities.length; i++) {
      if(Config.materialPriorities[i].equals(modId)) {
        return i;
      }
    }
    // mods not in priority list have the lowest priority
    return Integer.MAX_VALUE;
  }

  static void putMaterialTrace(String materialIdentifier) {
    ModContainer activeMod = Loader.instance().activeModContainer();
    materialRegisteredByMod.put(materialIdentifier, activeMod);
  }

  static void putStatTrace(String materialIdentifier, IMaterialStats stats, ModContainer trace) {
    if(!statRegisteredByMod.containsKey(materialIdentifier)) {
      statRegisteredByMod.put(materialIdentifier, new HashMap<>());
    }
    statRegisteredByMod.get(materialIdentifier).put(stats.getIdentifier(), trace);
  }

  static void putTraitTrace(String materialIdentifier, ITrait trait, ModContainer trace) {
    if(!traitRegisteredByMod.containsKey(materialIdentifier)) {
      traitRegisteredByMod.put(materialIdentifier, new HashMap<>());
    }
    traitRegisteredByMod.get(materialIdentifier).put(trait.getIdentifier(), trace);
  }

  public static ModContainer getTrace(Material material) {
    return materialRegisteredByMod.get(material.identifier);
  }

}
