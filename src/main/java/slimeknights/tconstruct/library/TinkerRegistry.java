package slimeknights.tconstruct.library;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import slimeknights.mantle.client.CreativeTab;
import slimeknights.tconstruct.library.materials.IMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Shard;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * Fachada de los registries de Tinkers' Construct.
 * <p>
 * La implementación (estado + lógica) vive en {@link MaterialRegistry}, {@link ToolRegistry},
 * {@link ModifierRegistry}, {@link SmelteryRegistry} y {@link DryingRegistry}. Esta clase solo
 * expone la API pública estática delegando en ellos, para no romper los call sites existentes.
 */
public final class TinkerRegistry {

  // the logger for the library
  public static final Logger log = Util.getLogger("API");

  private TinkerRegistry() {
  }

  /*---------------------------------------------------------------------------
  | CREATIVE TABS                                                             |
  ---------------------------------------------------------------------------*/
  public static CreativeTab tabGeneral = new CreativeTab("TinkerGeneral", new ItemStack(Items.SLIME_BALL));
  public static CreativeTab tabTools = new CreativeTab("TinkerTools", new ItemStack(Items.IRON_PICKAXE));
  public static CreativeTab tabParts = new CreativeTab("TinkerToolParts", new ItemStack(Items.STICK));
  public static CreativeTab tabSmeltery = new CreativeTab("TinkerSmeltery", new ItemStack(Item.getItemFromBlock(Blocks.STONEBRICK)));
  public static CreativeTab tabWorld = new CreativeTab("TinkerWorld", new ItemStack(Item.getItemFromBlock(Blocks.SLIME_BLOCK)));
  public static CreativeTab tabGadgets = new CreativeTab("TinkerGadgets", new ItemStack(Blocks.TNT));

  /*---------------------------------------------------------------------------
  | MATERIALS & TRAITS & STATS                                                |
  ---------------------------------------------------------------------------*/
  public static void addMaterial(Material material, IMaterialStats stats, ITrait trait) {
    MaterialRegistry.addMaterial(material, stats, trait);
  }

  public static void addMaterial(Material material, ITrait trait) {
    MaterialRegistry.addMaterial(material, trait);
  }

  public static void addMaterial(Material material, IMaterialStats stats) {
    MaterialRegistry.addMaterial(material, stats);
  }

  public static void addMaterial(Material material) {
    MaterialRegistry.addMaterial(material);
  }

  public static Material getMaterial(String identifier) {
    return MaterialRegistry.getMaterial(identifier);
  }

  public static Collection<Material> getAllMaterials() {
    return MaterialRegistry.getAllMaterials();
  }

  public static void removeHiddenMaterials() {
    MaterialRegistry.removeHiddenMaterials();
  }

  public static Collection<Material> getAllMaterialsWithStats(String statType) {
    return MaterialRegistry.getAllMaterialsWithStats(statType);
  }

  public static void addTrait(ITrait trait) {
    MaterialRegistry.addTrait(trait);
  }

  public static void addMaterialStats(String materialIdentifier, IMaterialStats stats) {
    MaterialRegistry.addMaterialStats(materialIdentifier, stats);
  }

  public static void addMaterialStats(Material material, IMaterialStats stats, IMaterialStats... stats2) {
    MaterialRegistry.addMaterialStats(material, stats, stats2);
  }

  public static void addMaterialStats(Material material, IMaterialStats stats) {
    MaterialRegistry.addMaterialStats(material, stats);
  }

  public static void addMaterialTrait(String materialIdentifier, ITrait trait, String stats) {
    MaterialRegistry.addMaterialTrait(materialIdentifier, trait, stats);
  }

  public static void addMaterialTrait(Material material, ITrait trait, String stats) {
    MaterialRegistry.addMaterialTrait(material, trait, stats);
  }

  public static boolean checkMaterialTrait(Material material, ITrait trait, String stats) {
    return MaterialRegistry.checkMaterialTrait(material, trait, stats);
  }

  public static ITrait getTrait(String identifier) {
    return MaterialRegistry.getTrait(identifier);
  }

  /*---------------------------------------------------------------------------
  | TOOLS & WEAPONS & CRAFTING                                                |
  ---------------------------------------------------------------------------*/
  public static void registerTool(ToolCore tool) {
    ToolRegistry.registerTool(tool);
  }

  public static java.util.Set<ToolCore> getTools() {
    return ToolRegistry.getTools();
  }

  public static void registerToolPart(IToolPart part) {
    ToolRegistry.registerToolPart(part);
  }

  public static java.util.Set<IToolPart> getToolParts() {
    return ToolRegistry.getToolParts();
  }

  public static void registerToolCrafting(ToolCore tool) {
    ToolRegistry.registerToolCrafting(tool);
  }

  public static void registerToolStationCrafting(ToolCore tool) {
    ToolRegistry.registerToolStationCrafting(tool);
  }

  public static java.util.Set<ToolCore> getToolStationCrafting() {
    return ToolRegistry.getToolStationCrafting();
  }

  public static void registerToolForgeCrafting(ToolCore tool) {
    ToolRegistry.registerToolForgeCrafting(tool);
  }

  public static java.util.Set<ToolCore> getToolForgeCrafting() {
    return ToolRegistry.getToolForgeCrafting();
  }

  public static void registerStencilTableCrafting(ItemStack stencil) {
    ToolRegistry.registerStencilTableCrafting(stencil);
  }

  public static List<ItemStack> getStencilTableCrafting() {
    return ToolRegistry.getStencilTableCrafting();
  }

  public static void setShardItem(Shard shard) {
    ToolRegistry.setShardItem(shard);
  }

  public static Shard getShard() {
    return ToolRegistry.getShard();
  }

  public static ItemStack getShard(Material material) {
    return ToolRegistry.getShard(material);
  }

  public static void addPatternForItem(Item item) {
    ToolRegistry.addPatternForItem(item);
  }

  public static void addCastForItem(Item item) {
    ToolRegistry.addCastForItem(item);
  }

  public static Collection<Item> getPatternItems() {
    return ToolRegistry.getPatternItems();
  }

  public static Collection<Item> getCastItems() {
    return ToolRegistry.getCastItems();
  }

  /*---------------------------------------------------------------------------
  | MODIFIERS                                                                 |
  ---------------------------------------------------------------------------*/
  public static void registerModifier(IModifier modifier) {
    ModifierRegistry.registerModifier(modifier);
  }

  public static void registerModifierAlias(IModifier modifier, String alias) {
    ModifierRegistry.registerModifierAlias(modifier, alias);
  }

  public static IModifier getModifier(String identifier) {
    return ModifierRegistry.getModifier(identifier);
  }

  public static Collection<IModifier> getAllModifiers() {
    return ModifierRegistry.getAllModifiers();
  }

  public static void registerHeadDropForAll(Class<? extends EntityLivingBase> clazz, ItemStack head) {
    ModifierRegistry.registerHeadDropForAll(clazz, head);
  }

  public static void registerHeadDrop(Class<? extends EntityLivingBase> clazz, Function<EntityLivingBase,ItemStack> callback) {
    ModifierRegistry.registerHeadDrop(clazz, callback);
  }

  public static void registerHeadDrop(Class<? extends EntityLivingBase> clazz, ItemStack head) {
    ModifierRegistry.registerHeadDrop(clazz, head);
  }

  public static Collection<ItemStack> getHeadDrops(EntityLivingBase entity) {
    return ModifierRegistry.getHeadDrops(entity);
  }

  public static ItemStack getHeadDrop(EntityLivingBase entity) {
    return ModifierRegistry.getHeadDrop(entity);
  }

  public static Map<Class<? extends EntityLivingBase>, Collection<ItemStack>> getAllSeveringRecipes() {
    return ModifierRegistry.getAllSeveringRecipes();
  }

  /*---------------------------------------------------------------------------
  | SMELTERY                                                                  |
  ---------------------------------------------------------------------------*/
  public static void registerMelting(Item item, Fluid fluid, int amount) {
    SmelteryRegistry.registerMelting(item, fluid, amount);
  }

  public static void registerMelting(Block block, Fluid fluid, int amount) {
    SmelteryRegistry.registerMelting(block, fluid, amount);
  }

  public static void registerMelting(ItemStack stack, Fluid fluid, int amount) {
    SmelteryRegistry.registerMelting(stack, fluid, amount);
  }

  public static void registerMelting(String oredict, Fluid fluid, int amount) {
    SmelteryRegistry.registerMelting(oredict, fluid, amount);
  }

  public static void registerMelting(MeltingRecipe recipe) {
    SmelteryRegistry.registerMelting(recipe);
  }

  public static MeltingRecipe getMelting(ItemStack stack) {
    return SmelteryRegistry.getMelting(stack);
  }

  public static List<MeltingRecipe> getAllMeltingRecipies() {
    return SmelteryRegistry.getAllMeltingRecipies();
  }

  public static void registerAlloy(FluidStack result, FluidStack... inputs) {
    SmelteryRegistry.registerAlloy(result, inputs);
  }

  public static void registerAlloy(AlloyRecipe recipe) {
    SmelteryRegistry.registerAlloy(recipe);
  }

  public static List<AlloyRecipe> getAlloys() {
    return SmelteryRegistry.getAlloys();
  }

  public static void registerTableCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount) {
    SmelteryRegistry.registerTableCasting(output, cast, fluid, amount);
  }

  public static void registerTableCasting(ICastingRecipe recipe) {
    SmelteryRegistry.registerTableCasting(recipe);
  }

  public static ICastingRecipe getTableCasting(ItemStack cast, Fluid fluid) {
    return SmelteryRegistry.getTableCasting(cast, fluid);
  }

  public static List<ICastingRecipe> getAllTableCastingRecipes() {
    return SmelteryRegistry.getAllTableCastingRecipes();
  }

  public static void registerBasinCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount) {
    SmelteryRegistry.registerBasinCasting(output, cast, fluid, amount);
  }

  public static void registerBasinCasting(ICastingRecipe recipe) {
    SmelteryRegistry.registerBasinCasting(recipe);
  }

  public static ICastingRecipe getBasinCasting(ItemStack cast, Fluid fluid) {
    return SmelteryRegistry.getBasinCasting(cast, fluid);
  }

  public static List<ICastingRecipe> getAllBasinCastingRecipes() {
    return SmelteryRegistry.getAllBasinCastingRecipes();
  }

  public static void registerSmelteryFuel(FluidStack fluidStack, int fuelDuration) {
    SmelteryRegistry.registerSmelteryFuel(fluidStack, fuelDuration);
  }

  public static boolean isSmelteryFuel(FluidStack in) {
    return SmelteryRegistry.isSmelteryFuel(in);
  }

  public static int consumeSmelteryFuel(FluidStack in) {
    return SmelteryRegistry.consumeSmelteryFuel(in);
  }

  public static Collection<FluidStack> getSmelteryFuels() {
    return SmelteryRegistry.getSmelteryFuels();
  }

  public static void registerEntityMelting() {
    SmelteryRegistry.registerEntityMelting();
  }

  public static void registerEntityMeltingForAll(Class<? extends Entity> clazz, FluidStack liquid) {
    SmelteryRegistry.registerEntityMeltingForAll(clazz, liquid);
  }

  public static void registerEntityMelting(Class<? extends Entity> clazz, FluidStack liquid) {
    SmelteryRegistry.registerEntityMelting(clazz, liquid);
  }

  public static FluidStack getMeltingForEntity(Entity entity) {
    return SmelteryRegistry.getMeltingForEntity(entity);
  }

  public static Map<ResourceLocation, FluidStack> getAllEntityMeltingRecipes() {
    return SmelteryRegistry.getAllEntityMeltingRecipes();
  }

  /*---------------------------------------------------------------------------
  | DRYING RACK                                                               |
  ---------------------------------------------------------------------------*/
  public static List<DryingRecipe> getAllDryingRecipes() {
    return DryingRegistry.getAllDryingRecipes();
  }

  public static void registerDryingRecipe(ItemStack input, ItemStack output, int time) {
    DryingRegistry.registerDryingRecipe(input, output, time);
  }

  public static void registerDryingRecipe(Item input, ItemStack output, int time) {
    DryingRegistry.registerDryingRecipe(input, output, time);
  }

  public static void registerDryingRecipe(Item input, Item output, int time) {
    DryingRegistry.registerDryingRecipe(input, output, time);
  }

  public static void registerDryingRecipe(Block input, Block output, int time) {
    DryingRegistry.registerDryingRecipe(input, output, time);
  }

  public static void registerDryingRecipe(String oredict, ItemStack output, int time) {
    DryingRegistry.registerDryingRecipe(oredict, output, time);
  }

  public static void addDryingRecipe(DryingRecipe recipe) {
    DryingRegistry.addDryingRecipe(recipe);
  }

  public static int getDryingTime(ItemStack input) {
    return DryingRegistry.getDryingTime(input);
  }

  public static ItemStack getDryingResult(ItemStack input) {
    return DryingRegistry.getDryingResult(input);
  }

  /*---------------------------------------------------------------------------
  | MATERIAL INTEGRATION                                                      |
  ---------------------------------------------------------------------------*/
  public static MaterialIntegration integrate(Material material) {
    return MaterialRegistry.integrate(material);
  }

  public static MaterialIntegration integrate(Material material, Fluid fluid) {
    return MaterialRegistry.integrate(material, fluid);
  }

  public static MaterialIntegration integrate(Material material, String oreRequirement) {
    return MaterialRegistry.integrate(material, oreRequirement);
  }

  public static MaterialIntegration integrate(Material material, Fluid fluid, String oreSuffix) {
    return MaterialRegistry.integrate(material, fluid, oreSuffix);
  }

  public static MaterialIntegration integrate(Fluid fluid, String oreSuffix) {
    return MaterialRegistry.integrate(fluid, oreSuffix);
  }

  public static MaterialIntegration integrate(MaterialIntegration materialIntegration) {
    return MaterialRegistry.integrate(materialIntegration);
  }

  public static List<MaterialIntegration> getMaterialIntegrations() {
    return MaterialRegistry.getMaterialIntegrations();
  }

  /*---------------------------------------------------------------------------
  | TRACEABILITY                                                              |
  ---------------------------------------------------------------------------*/
  public static net.minecraftforge.fml.common.ModContainer getTrace(Material material) {
    return MaterialRegistry.getTrace(material);
  }
}