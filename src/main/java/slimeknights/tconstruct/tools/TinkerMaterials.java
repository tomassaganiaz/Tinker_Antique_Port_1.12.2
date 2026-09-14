package slimeknights.tconstruct.tools;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.Logger;

import java.util.List;

import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.foundry.TinkerScorched;
import slimeknights.tconstruct.foundry.block.BlockScorched;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.tools.traits.TraitEnderference;
import slimeknights.tconstruct.tools.traits.TraitInsatiable;
import slimeknights.tconstruct.tools.traits.TraitMagnetic;
import slimeknights.tconstruct.tools.traits.TraitMomentum;
import slimeknights.tconstruct.tools.traits.TraitSharp;
import slimeknights.tconstruct.tools.traits.TraitSplintering;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.BlockSlimeGrass;

import static slimeknights.tconstruct.library.materials.MaterialTypes.HEAD;
import static slimeknights.tconstruct.library.materials.MaterialTypes.PROJECTILE;
import static slimeknights.tconstruct.library.materials.MaterialTypes.SHAFT;
import static slimeknights.tconstruct.library.utils.HarvestLevels.COBALT;
import static slimeknights.tconstruct.library.utils.HarvestLevels.DIAMOND;
import static slimeknights.tconstruct.library.utils.HarvestLevels.IRON;
import static slimeknights.tconstruct.library.utils.HarvestLevels.OBSIDIAN;
import static slimeknights.tconstruct.library.utils.HarvestLevels.STONE;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

/**
 * All the tool materials tcon supports.
 */
@Pulse(id = TinkerMaterials.PulseId, description = "All the tool materials added by TConstruct", pulsesRequired = TinkerTools.PulseId, forced = true)
public final class TinkerMaterials {

  static final String PulseId = "TinkerMaterials";
  static final Logger log = Util.getLogger(PulseId);

  public static final List<Material> materials = Lists.newArrayList();
  // not all listed materials are available by default. They enable when the needed material is present. See TinkerIntegration

  // natural resources/blocks
  public static final Material wood       = mat("wood", 0x8e661b);
  public static final Material stone      = mat("stone", 0x999999);
  public static final Material flint      = mat("flint", 0x696969);
  public static final Material cactus     = mat("cactus", 0x00a10f);
  public static final Material bone       = mat("bone", 0xede6bf);
  public static final Material obsidian   = mat("obsidian", 0x601cc4);
  public static final Material prismarine = mat("prismarine", 0x7edebc);
  public static final Material endstone   = mat("endstone", 0xe0d890);
  public static final Material paper      = mat("paper", 0xffffff);
  public static final Material sponge     = mat("sponge", 0xcacc4e);
  public static final Material firewood   = mat("firewood", 0xcc5300);
  public static final Material chorus     = mat("chorus", 0x9a5cc6);
  public static final Material wool       = mat("wool", 0xf5f5f5);

  // Slime
  public static final Material knightslime= mat("knightslime", 0xf18ff0);
  public static final Material slime      = mat("slime", 0x82c873);
  public static final Material blueslime  = mat("blueslime", 0x74c8c7);
  public static final Material magmaslime = mat("magmaslime", 0xff960d);

  // Metals
  public static final Material iron       = mat("iron", 0xcacaca);
  public static final Material pigiron    = mat("pigiron", 0xef9e9b);

  // Tier 4 de TC3: dos aleaciones (solo colado), el hueso ardiente (cuyo item ya existe en el
  // fork) y el material ancient, que se craftea con chatarra de netherite del Nether Backport
  public static final Material queensslime    = mat("queensslime", 0x7fd85f);
  public static final Material netherite      = mat("netherite", 0x443a3b);
  public static final Material scrap         = mat("scrap", 0x6b5a52);
  public static final Material hepatizon      = mat("hepatizon", 0xb08d57);
  public static final Material blazingbone    = mat("blazingbone", 0xffc100);
  public static final Material ancient        = mat("ancient", 0x4a4a4a);

  // Aleaciones de TC3 (tier 3): solo se cuelan, no tienen lingote propio en el fork
  public static final Material slimesteel     = mat("slimesteel", 0x9dc0cc);
  public static final Material cinderslime    = mat("cinderslime", 0xa5130c);

  // Aleaciones de compat de TC3 (tier 3-4): mismo patrón, se cuelan desde la smeltery
  public static final Material constantan     = mat("constantan", 0xc98f5c);
  public static final Material invar          = mat("invar", 0xb8b8a8);
  public static final Material pewter         = mat("pewter", 0x9aa3a8);
  public static final Material nicrosil       = mat("nicrosil", 0x8c93a8);
  public static final Material necronium      = mat("necronium", 0x1a1a2e);

  // Materiales de compat que sí tienen lingote en mods instalados (Twilight Forest, Ice and Fire, NuclearCraft)
  public static final Material ironwood       = mat("ironwood", 0x8b6b4a);
  public static final Material steeleaf       = mat("steeleaf", 0x6b8f4a);
  public static final Material fiery          = mat("fiery", 0xd4622a);
  public static final Material dragonscale    = mat("dragonscale", 0x2f6f5c);

  // Compuestos de TC3: items nuevos fabricados en la smeltery
  public static final Material slimeskin      = mat("slimeskin", 0x82c873);
  public static final Material nahuatl        = mat("nahuatl", 0xbe6e50);
  public static final Material darkthread     = mat("darkthread", 0x5a4672);
  public static final Material jeweledhide    = mat("jeweledhide", 0x5a96dc);
  public static final Material blazewood      = mat("blazewood", 0xf09628);

  // Bambú: no existe en vanilla 1.12 pero Future MC lo portea (oredict cropBamboo)
  public static final Material bamboo         = mat("bamboo", 0x7fa83f);
  // Queso: leche colada en molde de lingote + secadero (como en TC3)
  public static final Material cheese         = mat("cheese", 0xf2d16b);
  public static final Material rosegold       = mat("rosegold", 0xd08a7a);
  public static final Material amethystbronze = mat("amethystbronze", 0xa87bbd);

  // Nether Materials
  public static final Material netherrack = mat("netherrack", 0xb84f4f);
  public static final Material ardite     = mat("ardite", 0xd14210);
  public static final Material cobalt     = mat("cobalt", 0x2882d4);
  public static final Material manyullyn  = mat("manyullyn", 0xa15cf8);
  
  // Special Bone Materials
  public static final Material bloodbone  = mat("bloodbone", 0xc70000);

  // Common Metals + Alloys
  public static final Material copper     = mat("copper", 0xed9f07);
  public static final Material bronze     = mat("bronze", 0xe3bd68);
  public static final Material tin        = mat("tin", 0xc1cddc);
  public static final Material aluminum   = mat("aluminum", 0xefe0d5);
  public static final Material lead       = mat("lead", 0x4d4968);
  public static final Material silver     = mat("silver", 0xd1ecf6);
  public static final Material electrum   = mat("electrum", 0xe8db49);
  public static final Material steel      = mat("steel", 0xa7a7a7);
  public static final Material nickel     = mat("nickel", 0xc8d683);
  public static final Material alubrass   = mat("alubrass", 0xf0d467);
  public static final Material alumite    = mat("alumite", 0xffa7e9);

  // Materials backed by fluids the fork already registered (seared stone, glass, clay, blood, ender slime)
  public static final Material gold        = mat("gold", 0xf6d609);
  public static final Material searedstone = mat("searedstone", 0x777777);
  public static final Material scorchedstone = mat("scorchedstone", 0x3a2f2c);
  public static final Material glass       = mat("glass", 0xc0f5fe);
  public static final Material clay        = mat("clay", 0xc67453);
  public static final Material enderslime  = mat("enderslime", 0xd236ff);
  public static final Material blood       = mat("blood", 0x540000);

  // specul
  public static final Material xu;

  // bowstring materials
  public static final Material string    = mat("string", 0xeeeeee);
  public static final Material vine      = mat("vine", 0x40a10f);
  // tier 2 de TC3, aportadas por Unseen's Nether Backport (modid "nb"): no existen en vanilla 1.12
  public static final Material weepingvine  = mat("weepingvine", 0x9c2b2b);
  public static final Material twistingvine = mat("twistingvine", 0x2c8a7f);
  public static final Material slimevine_blue   = mat("slimevine_blue", 0x74c8c7);
  //public static final Material slimevine_orange = mat("slimevine_orange", 0xff960d);
  public static final Material slimevine_purple = mat("slimevine_purple", 0xc873c8);

  // additional arrow shaft
  public static final Material blaze     = mat("blaze", 0xffc100);
  public static final Material reed      = mat("reed", 0xaadb74);
  public static final Material ice       = mat("ice", 0x97d7e0);
  public static final Material endrod    = mat("endrod", 0xe8ffd6);

  // bindings + ammo (TC3: arrow heads, bindings y bowstrings; el fork no separa piezas de flecha)
  public static final Material leather      = mat("leather", 0xc65c35);
  public static final Material necroticbone = mat("necroticbone", 0x353535);
  public static final Material enderpearl   = mat("enderpearl", 0x2b9c8c);
  public static final Material gunpowder    = mat("gunpowder", 0x6b6b6b);
  public static final Material redstone     = mat("redstone", 0xd70000);
  public static final Material amethyst     = mat("amethyst", 0xa86fd4);
  public static final Material quartz       = mat("quartz", 0xe8e3d5);
  public static final Material glowstone    = mat("glowstone", 0xf5d76e);
  public static final Material shulker      = mat("shulker", 0x95679d);

  // fletching
  public static final Material feather   = mat("feather", 0xeeeeee);
  public static final Material leaf      = mat("leaf", 0x1d730c);
  public static final Material slimeleaf_blue   = mat("slimeleaf_blue", 0x74c8c7);
  public static final Material slimeleaf_orange = mat("slimeleaf_orange", 0xff960d);
  public static final Material slimeleaf_purple = mat("slimeleaf_purple", 0xc873c8);
  public static final Material leaves = mat("leaves", 0x1d730c);
  public static final Material venombone = mat("venombone", 0xc70000);
  public static final Material whitestone = mat("whitestone", 0xe0d890);
  public static final Material skyslimeVine = mat("skyslimeVine", 0x74c8c7);
  public static final Material earthslime = mat("earthslime", 0x82c873);
  public static final Material skyslime = mat("skyslime", 0x74c8c7);
  public static final Material slimeball = mat("slimeball", 0x82c873);
  public static final Material magma = mat("magma", 0xff960d);
  public static final Material enderslimeVine = mat("enderslimeVine", 0xc873c8);

  private static Material mat(String name, int color) {
    // make materials hidden by default, integration will make them visible if integrated
    Material mat = new Material(name, color, true);
    materials.add(mat);
    return mat;
  }

  static {
    xu = new Material("unstable", TextFormatting.WHITE);
  }

  @SubscribeEvent
  public void registerPotions(Register<Potion> event) {
    IForgeRegistry<Potion> registry = event.getRegistry();

    registry.registerAll(TraitEnderference.Enderference,
                         TraitInsatiable.Insatiable,
                         TraitMagnetic.Magnetic,
                         TraitMomentum.Momentum,
                         TraitSharp.DOT,
                         TraitSplintering.Splinter);
  }

  @Subscribe
  public void setupMaterialStats(FMLPreInitializationEvent event) {
    // stats need to be present before model loading/texture generation so we don't generate unneeded parts
    registerToolMaterialStats();
    registerBowMaterialStats();
    registerProjectileMaterialStats();
    // JSON data-pack compat (1.20.1) — loads data/tconstruct/tinkering/materials/stats/*.json if present
    try { slimeknights.tconstruct.common.JsonMaterialLoader.load(); } catch(Exception e) {}
    try { slimeknights.tconstruct.common.JsonMaterialLoader.loadDataPackStats(); } catch(Exception e) {}
  }

  @Subscribe
  public void setupMaterials(FMLInitializationEvent event) {
    // natural resources/blocks
    wood.setCraftable(true);
    wood.addItem("stickWood", 1, Material.VALUE_Shard);
    wood.addItem("plankWood", 1, Material.VALUE_Ingot);
    wood.addItem("logWood", 1, Material.VALUE_Ingot * 4);
    wood.addTrait(ecological);

    stone.setCraftable(true);
    stone.addItemIngot("cobblestone");
    stone.addItemIngot("stone");
    stone.setRepresentativeItem(new ItemStack(Blocks.COBBLESTONE));
    stone.addTrait(cheapskate, HEAD);
    stone.addTrait(cheap);

    flint.setCraftable(true);
    flint.addItem(Items.FLINT, 1, Material.VALUE_Ingot);
    flint.setRepresentativeItem(new ItemStack(Items.FLINT));
    flint.addTrait(crude2, HEAD);
    flint.addTrait(crude);

    cactus.setCraftable(true);
    cactus.addItemIngot("blockCactus");
    cactus.setRepresentativeItem(new ItemStack(Blocks.CACTUS));
    cactus.addTrait(prickly, HEAD);
    cactus.addTrait(spiky);

    obsidian.setFluid(TinkerFluids.obsidian);
    obsidian.setCraftable(true);
    obsidian.setCastable(true);
    obsidian.addItemIngot("obsidian");
    obsidian.setRepresentativeItem(new ItemStack(Blocks.OBSIDIAN));
    obsidian.addTrait(duritos);

    prismarine.setCraftable(true);
    prismarine.addItem("gemPrismarine", 1, Material.VALUE_Fragment);
    prismarine.addItem("blockPrismarine", 1, Material.VALUE_Ingot);
    prismarine.addItem("blockPrismarineBrick", 1, Material.VALUE_Fragment * 9);
    prismarine.addItem("blockPrismarineDark", 1, Material.VALUE_Ingot * 2);
    prismarine.setRepresentativeItem(Blocks.PRISMARINE);
    prismarine.addTrait(jagged, HEAD);
    prismarine.addTrait(aquadynamic, HEAD);
    prismarine.addTrait(aquadynamic);

    netherrack.setCraftable(true);
    netherrack.addItemIngot("netherrack");
    netherrack.setRepresentativeItem(Blocks.NETHERRACK);
    netherrack.addTrait(aridiculous, HEAD);
    netherrack.addTrait(hellish, HEAD);
    netherrack.addTrait(hellish);

    endstone.setCraftable(true);
    endstone.addItemIngot("endstone");
    endstone.setRepresentativeItem(Blocks.END_STONE);
    endstone.addTrait(alien, HEAD);
    endstone.addTrait(enderference);
    endstone.addTrait(enderference, PROJECTILE);

    // item/special resources
    bone.setCraftable(true);
    bone.addItemIngot("bone");
    bone.addItem(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()), 1, Material.VALUE_Fragment); // bonemeal
    bone.setRepresentativeItem(Items.BONE);
    bone.addTrait(splintering, HEAD);
    bone.addTrait(splitting, SHAFT);
    bone.addTrait(fractured);

    paper.setCraftable(true);
    paper.addItem("paper", 1, Material.VALUE_Fragment);
    paper.setRepresentativeItem(Items.PAPER);
    paper.addTrait(writable2, HEAD);
    paper.addTrait(writable);

    sponge.setCraftable(true);
    sponge.addItem(Blocks.SPONGE, Material.VALUE_Ingot);
    sponge.setRepresentativeItem(Blocks.SPONGE);
    sponge.addTrait(squeaky);

    firewood.setCraftable(true);
    firewood.addItem(TinkerCommons.firewood, 1, Material.VALUE_Ingot);
    firewood.setRepresentativeItem(TinkerCommons.firewood);
    firewood.addTrait(autosmelt);

    // tier 1 (TC3): chorus comes from popped chorus fruit, wool is an arrow head/binding material
    chorus.setCraftable(true);
    chorus.addItem(new ItemStack(Items.CHORUS_FRUIT_POPPED), 1, Material.VALUE_Ingot);
    chorus.setRepresentativeItem(new ItemStack(Items.CHORUS_FRUIT_POPPED));
    chorus.addTrait(enderference);

    wool.setCraftable(true);
    wool.addItem(new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE), 1, Material.VALUE_Ingot);
    wool.setRepresentativeItem(new ItemStack(Blocks.WOOL));

    slime.setCraftable(true);
    slime.addItemIngot("slimecrystalGreen");
    slime.addTrait(slimeyGreen);
    slime.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);
    slime.addTrait(overshield, ArmorMaterialStats.TYPE_HELMET);

    blueslime.setCraftable(true);
    blueslime.addItemIngot("slimecrystalBlue");
    blueslime.addTrait(slimeyBlue);
    blueslime.addTrait(overshield, ArmorMaterialStats.TYPE_HELMET);
    blueslime.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);

    knightslime.setCraftable(true);
    knightslime.addCommonItems("Knightslime");
    knightslime.addTrait(crumbling, HEAD);
    knightslime.addTrait(unnatural);
    knightslime.addTrait(invigorating, ArmorMaterialStats.TYPE_HELMET);
    knightslime.addTrait(invigorating, ArmorMaterialStats.TYPE_CHESTPLATE);
    knightslime.addTrait(invigorating, ArmorMaterialStats.TYPE_LEGGINGS);
    knightslime.addTrait(invigorating, ArmorMaterialStats.TYPE_BOOTS);
    knightslime.addTrait(overshield, ArmorMaterialStats.TYPE_HELMET);
    knightslime.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);

    magmaslime.setCraftable(true);
    magmaslime.addItemIngot("slimecrystalMagma");
    magmaslime.setRepresentativeItem(TinkerCommons.matSlimeCrystalMagma);
    magmaslime.addTrait(superheat, HEAD);
    magmaslime.addTrait(flammable);

    // Metals
    iron.addCommonItems("Iron");
    iron.setRepresentativeItem(Items.IRON_INGOT);
    iron.addTrait(magnetic2, HEAD);
    iron.addTrait(magnetic);
    iron.addTrait(heavy, ArmorMaterialStats.TYPE_CHESTPLATE);
    iron.addTrait(heavy, ArmorMaterialStats.TYPE_HELMET);

    pigiron.addCommonItems("Pigiron");
    pigiron.addTrait(baconlicious, HEAD);
    pigiron.addTrait(tasty, HEAD);
    pigiron.addTrait(tasty);

    // Tier 4: las dos aleaciones no tienen insumo de crafteo (se cuelan); el hueso ardiente usa el
    // item que el fork ya fabrica colando hueso necrótico + sangre de blaze; ancient necesita el
    // Nether Backport, y sin él se queda sin insumo igual que las vides.
    // Rasgos TC3 → fork: overlord+overslime → slimeyBlue, momentum → momentum,
    // conducting → shocking, vintage/worldbound → duritos.
    // Habilidades pasivas armor duales (weapon vs armor): invigorating/overshield/revitalizing/etc para placas
    queensslime.addTrait(slimeyBlue);
    queensslime.addTrait(overshield, ArmorMaterialStats.TYPE_HELMET);
    queensslime.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);
    hepatizon.addTrait(momentum);
    hepatizon.addTrait(revitalizingArmor, ArmorMaterialStats.TYPE_CHESTPLATE);
    hepatizon.addTrait(revitalizingArmor, ArmorMaterialStats.TYPE_HELMET);
    blazingbone.addTrait(shocking);
    blazingbone.addTrait(conductiveArmor, ArmorMaterialStats.TYPE_HELMET);
    blazingbone.addTrait(conductiveArmor, ArmorMaterialStats.TYPE_CHESTPLATE);
    netherite.setCraftable(true);
    netherite.setCastable(true);
    netherite.setFluid(TinkerFluids.netherite);
    netherite.addCommonItems("Netherite");
    netherite.setRepresentativeItem("ingotNetherite");
    netherite.addTrait(duritos);
    netherite.addTrait(heavy);

    ancient.addTrait(duritos);

    ItemStack netheriteScrap = getModItem("nb:nether_scrap");
    if(!netheriteScrap.isEmpty()) {
      ancient.setCraftable(true);
      ancient.addItem(netheriteScrap, 1, Material.VALUE_Ingot);
      ancient.setRepresentativeItem(netheriteScrap);
    }
    else {
      log.warn("Netherite Scrap (nb:nether_scrap) not found, ancient material will have no items");
    }

    // Chatarra de netherite (Nether Backport) como material completo: se cuela del fluido scrap
    scrap.setCastable(true);
    scrap.setFluid(TinkerFluids.scrap);
    scrap.addTrait(heavy);
    if(!netheriteScrap.isEmpty()) {
      scrap.setCraftable(true);
      scrap.addItem(netheriteScrap, 1, Material.VALUE_Ingot);
      scrap.setRepresentativeItem(netheriteScrap);
    }

    if(TinkerCommons.matBlazingBone != null && !TinkerCommons.matBlazingBone.isEmpty()) {
      blazingbone.setCraftable(true);
      blazingbone.addItem(TinkerCommons.matBlazingBone, 1, Material.VALUE_Ingot);
      blazingbone.setRepresentativeItem(TinkerCommons.matBlazingBone);
    }

    // Aleaciones de TC3 (tier 3). Sin insumos de crafteo: se obtienen colando piezas desde la smeltery.
    // Rasgos: TC3 da overcast+overslime (slimesteel), enhanced (rose gold) y crumbling (amethyst bronze).
    // Armor dual: amethyst_bronze crystalstrike -> crystalArmor, slimesteel overslime -> overshield
    slimesteel.addItemIngot("ingotSlimesteel");
    slimesteel.addTrait(dense);
    slimesteel.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);
    slimesteel.setRepresentativeItem(TinkerCommons.ingotSlimesteel);
    cinderslime.addItemIngot("ingotCinderslime");
    cinderslime.addTrait(flammable);
    cinderslime.addTrait(overshield, ArmorMaterialStats.TYPE_HELMET);
    cinderslime.addTrait(overshield, ArmorMaterialStats.TYPE_CHESTPLATE);
    cinderslime.setRepresentativeItem(TinkerCommons.ingotCinderslime);
    rosegold.addItemIngot("ingotRosegold");
    rosegold.addTrait(established);
    rosegold.addTrait(crystalArmor, ArmorMaterialStats.TYPE_HELMET);
    rosegold.setRepresentativeItem(TinkerCommons.ingotRosegold);
    amethystbronze.addItemIngot("ingotAmethystBronze");
    amethystbronze.addTrait(crumbling, HEAD);
    amethystbronze.addTrait(crystalArmor, ArmorMaterialStats.TYPE_HELMET);
    amethystbronze.addTrait(crystalArmor, ArmorMaterialStats.TYPE_CHESTPLATE);
    amethystbronze.setRepresentativeItem(TinkerCommons.ingotAmethystBronze);

    // Aleaciones de compat: mismos rasgos aproximados que sus equivalentes del fork
    constantan.addTrait(shocking);
    invar.addTrait(stiff);
    pewter.addTrait(heavy);
    nicrosil.addTrait(duritos);
    necronium.setCraftable(true);
    necronium.addItem("ingotUranium", 1, Material.VALUE_Ingot);
    necronium.setRepresentativeItem("ingotUranium");
    necronium.addTrait(heavy);
    necronium.addTrait(duritos);

    // Materiales de mods: crafteables desde los lingotes que ya aportan
    ironwood.addCommonItems("Ironwood");
    ironwood.addTrait(ecological);

    steeleaf.addCommonItems("Steeleaf");
    steeleaf.addTrait(ecological);

    fiery.addCommonItems("Fiery");
    fiery.addTrait(superheat);

    // escamas del dragón del End (drop añadido en ToolEvents) + las de Ice and Fire por oredict
    dragonscale.setCraftable(true);
    dragonscale.addItem("dragonScale", 1, Material.VALUE_Ingot);
    dragonscale.setRepresentativeItem(TinkerCommons.matDragonScale);
    dragonscale.addTrait(sharp); // TC3: ammo trait dragonshot

    // Compuestos: items propios del fork (meta 24-28 de TinkerCommons.materials)
    slimeskin.setCraftable(true);
    slimeskin.addItem(TinkerCommons.matSlimeSkin, 1, Material.VALUE_Ingot);
    slimeskin.setRepresentativeItem(TinkerCommons.matSlimeSkin);
    slimeskin.addTrait(cheap);

    nahuatl.setCraftable(true);
    nahuatl.addItem(TinkerCommons.nahuatlPlanks, 1, Material.VALUE_Ingot);
    nahuatl.setRepresentativeItem(TinkerCommons.nahuatlPlanks);
    nahuatl.addTrait(established);

    darkthread.setCraftable(true);
    darkthread.addItem(TinkerCommons.matDarkthread, 1, Material.VALUE_Ingot);
    darkthread.setRepresentativeItem(TinkerCommons.matDarkthread);
    darkthread.addTrait(established);

    jeweledhide.setCraftable(true);
    jeweledhide.addItem(TinkerCommons.matJeweledHide, 1, Material.VALUE_Ingot);
    jeweledhide.setRepresentativeItem(TinkerCommons.matJeweledHide);
    jeweledhide.addTrait(dense);

    blazewood.setCraftable(true);
    blazewood.addItem(TinkerCommons.blazewoodPlanks, 1, Material.VALUE_Ingot);
    blazewood.setRepresentativeItem(TinkerCommons.blazewoodPlanks);
    blazewood.addTrait(flammable);

    // Bambú de Future MC: en TC3 solo es limb + asta (ni cabeza ni mango). `reed` sigue existiendo
    // como material propio del fork (asta), pero el hueco de `bamboo` ya lo cubre su item real.
    bamboo.setCraftable(true);
    bamboo.addItem("blockCactus", 1, Material.VALUE_Ingot);
    bamboo.addItem("cactus", 1, Material.VALUE_Ingot);
    bamboo.addTrait(spiky);

    // Queso: el material se craftea con el lingote ya seco (leche -> molde -> secadero)
    cheese.setCraftable(true);
    cheese.addItem(TinkerCommons.matCheese, 1, Material.VALUE_Ingot);
    cheese.setRepresentativeItem(TinkerCommons.matCheese);

    cobalt.addCommonItems("Cobalt");
    cobalt.addTrait(momentum, HEAD);
    cobalt.addTrait(lightweight);
    cobalt.addTrait(lightweight, ArmorMaterialStats.TYPE_HELMET);
    cobalt.addTrait(conductiveArmor, ArmorMaterialStats.TYPE_CHESTPLATE);

    ardite.addCommonItems("Ardite");
    ardite.addTrait(stonebound, HEAD);
    ardite.addTrait(petramor);

    manyullyn.addCommonItems("Manyullyn");
    manyullyn.addTrait(insatiable, HEAD);
    manyullyn.addTrait(coldblooded);
    manyullyn.addTrait(revitalizingArmor, ArmorMaterialStats.TYPE_CHESTPLATE);
    
    // Special Bone Materials
    bloodbone.setCraftable(true);
    bloodbone.addItemIngot("boneBloodied");
    bloodbone.setRepresentativeItem(TinkerCommons.matBloodyBone);
    bloodbone.addTrait(raging2, HEAD);
    bloodbone.addTrait(splintering, HEAD);
    bloodbone.addTrait(raging);
    bloodbone.addTrait(fractured);

    // Materials backed by fluids the fork already had. The fluids are registered by
    // TinkerFluids during the block registry event, so they can only be linked here (init)
    gold.addCommonItems("Gold");
    gold.setRepresentativeItem(Items.GOLD_INGOT);
    gold.setFluid(TinkerFluids.gold);
    gold.setCraftable(true);
    gold.setCastable(true);
    gold.addTrait(established);

    searedstone.setFluid(TinkerFluids.searedStone);
    searedstone.setCraftable(true);
    searedstone.setCastable(true);
    searedstone.addItem("ingotBrickSeared", 1, Material.VALUE_SearedMaterial);
    searedstone.addItem("blockSeared", 1, Material.VALUE_SearedBlock);
    searedstone.setRepresentativeItem(TinkerCommons.searedBrick);
    searedstone.addTrait(aridiculous, HEAD);
    searedstone.addTrait(hellish);

    // TC3 tier 2: la piedra scorched es el material de la foundry. Sus bloques viven en el pulse
    // scorched, así que si ese pulse está desactivado no hay insumo posible y el material queda vacío.
    if(TinkerScorched.blockScorched != null) {
      ItemStack scorchedBrick = new ItemStack(TinkerScorched.blockScorched, 1, BlockScorched.ScorchedType.BRICK.getMeta());
      ItemStack scorchedBlock = new ItemStack(TinkerScorched.blockScorched, 1, BlockScorched.ScorchedType.STONE.getMeta());
      scorchedstone.setFluid(TinkerFluids.scorchedStone);
      scorchedstone.setCraftable(true);
      scorchedstone.setCastable(true);
      scorchedstone.addItem(scorchedBrick, 1, Material.VALUE_SearedMaterial);
      scorchedstone.addItem(scorchedBlock, 1, Material.VALUE_SearedBlock);
      scorchedstone.setRepresentativeItem(scorchedBrick);
      scorchedstone.addTrait(superheat); // TC3: scorching
    }

    glass.setFluid(TinkerFluids.glass);
    glass.setCraftable(true);
    glass.setCastable(true);
    glass.addItem("blockGlass", 1, Material.VALUE_Glass);
    glass.addItem("paneGlass", 1, Material.VALUE_Glass * 6 / 16);
    glass.setRepresentativeItem(Blocks.GLASS);
    glass.addTrait(jagged, HEAD);

    clay.setFluid(TinkerFluids.clay);
    clay.setCraftable(true);
    clay.setCastable(true);
    clay.addItem("clay", 1, Material.VALUE_Ingot);
    clay.addItem("blockClay", 1, Material.VALUE_BrickBlock);
    clay.setRepresentativeItem(Items.CLAY_BALL);
    clay.addTrait(cheap);

    enderslime.setFluid(TinkerFluids.purpleSlime);
    enderslime.setCraftable(true);
    enderslime.setCastable(true);
    enderslime.addItem("slimeballPurple", 1, Material.VALUE_SlimeBall);
    enderslime.setRepresentativeItem(TinkerCommons.matSlimeBallPurple);
    enderslime.addTrait(enderference);
    enderslime.addTrait(lightweight);

    blood.setFluid(TinkerFluids.blood);
    blood.setCraftable(true);
    blood.setCastable(true);
    blood.addItem("slimeballBlood", 1, Material.VALUE_SlimeBall);
    blood.setRepresentativeItem(TinkerCommons.matSlimeBallBlood);
    blood.addTrait(splintering, HEAD);
    blood.addTrait(raging);

    // Common Metals
    copper.addCommonItems("Copper");
    copper.addTrait(established);
    copper.addTrait(aquadynamic, ArmorMaterialStats.TYPE_HELMET);

    bronze.addCommonItems("Bronze");
    bronze.addTrait(dense);

    tin.addCommonItems("Tin");
    aluminum.addCommonItems("Aluminum");

    lead.addCommonItems("Lead");
    lead.addTrait(poisonous);
    lead.addTrait(heavy);

    silver.addCommonItems("Silver");
    silver.addTrait(holy);
    silver.addTrait(holy, ArmorMaterialStats.TYPE_HELMET);

    electrum.addCommonItems("Electrum");
    electrum.addTrait(shocking);

    steel.addCommonItems("Steel");
    steel.addTrait(sharp, HEAD);
    steel.addTrait(stiff);

    nickel.addCommonItems("Nickel");
    nickel.setFluid(TinkerFluids.nickel);
    nickel.setCastable(true);
    nickel.setCraftable(true);
    nickel.addTrait(magnetic);
    
    alubrass.addCommonItems("Alubrass");
    alubrass.addTrait(depthdigger);
    
    alumite.addCommonItems("Alumite");
    alumite.addTrait(duritos);

    // bindings + ammo: en TC3 solo son arrow heads / bindings / bowstrings, sin stats de cabeza
    leather.setCraftable(true);
    leather.addItem(Items.LEATHER, 1, Material.VALUE_Ingot);
    leather.setRepresentativeItem(Items.LEATHER);

    necroticbone.setCraftable(true);
    necroticbone.addItem(TinkerCommons.matNecroticBone, 1, Material.VALUE_Ingot);
    necroticbone.setRepresentativeItem(TinkerCommons.matNecroticBone);
    necroticbone.addTrait(poisonous);
    necroticbone.addTrait(revitalizingArmor, ArmorMaterialStats.TYPE_HELMET);

    enderpearl.setCraftable(true);
    enderpearl.addItem(Items.ENDER_PEARL, 1, Material.VALUE_Ingot);
    enderpearl.setRepresentativeItem(Items.ENDER_PEARL);
    enderpearl.addTrait(enderference);

    gunpowder.setCraftable(true);
    gunpowder.addItem(Items.GUNPOWDER, 1, Material.VALUE_Ingot);
    gunpowder.setRepresentativeItem(Items.GUNPOWDER);
    gunpowder.addTrait(flammable);

    redstone.setCraftable(true);
    redstone.addItem(Items.REDSTONE, 1, Material.VALUE_Ingot);
    redstone.setRepresentativeItem(Items.REDSTONE);
    redstone.addTrait(shocking);

    amethyst.setCraftable(true);
    amethyst.addItem("gemAmethyst", 1, Material.VALUE_Ingot);
    amethyst.addTrait(jagged);

    quartz.setCraftable(true);
    quartz.addItem(Items.QUARTZ, 1, Material.VALUE_Ingot);
    quartz.setRepresentativeItem(Items.QUARTZ);
    quartz.addTrait(fractured);

    glowstone.setCraftable(true);
    glowstone.addItem(Items.GLOWSTONE_DUST, 1, Material.VALUE_Ingot);
    glowstone.setRepresentativeItem(Items.GLOWSTONE_DUST);

    shulker.setCraftable(true);
    shulker.addItem(Items.SHULKER_SHELL, 1, Material.VALUE_Ingot);
    shulker.setRepresentativeItem(Items.SHULKER_SHELL);
    shulker.addTrait(hovering);

    // tier 2 de TC3: vides del Nether. No existen en vanilla 1.12, las aporta Unseen's
    // Nether Backport (modid "nb") y no tienen oredict, así que se resuelven por id de registro.
    // Si el mod no está cargado (o el id cambia) el material se queda sin insumos y sin integrar.
    ItemStack crimsonVine = getModItem("nb:crimson_vine"); // Weeping Vines
    ItemStack warpedVine = getModItem("nb:warped_vine");   // Twisting Vines
    if(!crimsonVine.isEmpty()) {
      weepingvine.setCraftable(true);
      weepingvine.addItem(crimsonVine, 1, Material.VALUE_Ingot);
      weepingvine.setRepresentativeItem(crimsonVine);
      weepingvine.addTrait(flammable); // TC3: flamestance
    }
    else {
      log.warn("Weeping Vines (nb:crimson_vine) not found, weepingvine material will have no items");
    }
    if(!warpedVine.isEmpty()) {
      twistingvine.setCraftable(true);
      twistingvine.addItem(warpedVine, 1, Material.VALUE_Ingot);
      twistingvine.setRepresentativeItem(warpedVine);
      twistingvine.addTrait(freezing); // TC3: entangled
    }
    else {
      log.warn("Twisting Vines (nb:warped_vine) not found, twistingvine material will have no items");
    }

    // bowstring
    string.addItemIngot("string");
    string.setRepresentativeItem(Items.STRING);
    vine.addItemIngot("vine");
    vine.setRepresentativeItem(Blocks.VINE);
    safeAdd(slimevine_blue, new ItemStack(TinkerWorld.slimeVineBlue1), Material.VALUE_Ingot, true);
    safeAdd(slimevine_blue, new ItemStack(TinkerWorld.slimeVineBlue2), Material.VALUE_Ingot, false);
    safeAdd(slimevine_blue, new ItemStack(TinkerWorld.slimeVineBlue3), Material.VALUE_Ingot, false);
    safeAdd(slimevine_purple, new ItemStack(TinkerWorld.slimeVinePurple1), Material.VALUE_Ingot, true);
    safeAdd(slimevine_purple, new ItemStack(TinkerWorld.slimeVinePurple2), Material.VALUE_Ingot, false);
    safeAdd(slimevine_purple, new ItemStack(TinkerWorld.slimeVinePurple3), Material.VALUE_Ingot, false);

    // arrow only materials
    blaze.addItem(Items.BLAZE_ROD, 1, Material.VALUE_Ingot);
    blaze.setRepresentativeItem(Items.BLAZE_ROD);
    blaze.addTrait(hovering);

    reed.addItem(Items.REEDS, 1, Material.VALUE_Ingot);
    reed.setRepresentativeItem(Items.REEDS);
    reed.addTrait(breakable);

    ice.addItem(Blocks.PACKED_ICE, Material.VALUE_Ingot);
    ice.setRepresentativeItem(Blocks.PACKED_ICE);
    ice.addTrait(freezing);

    endrod.addItem(Blocks.END_ROD, Material.VALUE_Ingot);
    endrod.setRepresentativeItem(Blocks.END_ROD);
    endrod.addTrait(endspeed);

    feather.addItemIngot("feather");
    feather.setRepresentativeItem(Items.FEATHER);
    leaf.addItem("treeLeaves", 1, Material.VALUE_Shard);
    leaf.setRepresentativeItem(Blocks.LEAVES);
    safeAdd(slimeleaf_blue, new ItemStack(TinkerWorld.slimeLeaves, 1, BlockSlimeGrass.FoliageType.BLUE.getMeta()), Material.VALUE_Shard, true);
    safeAdd(slimeleaf_orange, new ItemStack(TinkerWorld.slimeLeaves, 1, BlockSlimeGrass.FoliageType.ORANGE.getMeta()), Material.VALUE_Shard, true);
    safeAdd(slimeleaf_purple, new ItemStack(TinkerWorld.slimeLeaves, 1, BlockSlimeGrass.FoliageType.PURPLE.getMeta()), Material.VALUE_Shard, true);
    leaves.setCraftable(true); leaves.addItem("treeLeaves", 1, Material.VALUE_Shard); leaves.setRepresentativeItem(Blocks.LEAVES);
    venombone.setCraftable(true); venombone.addItemIngot("boneBloodied"); venombone.setRepresentativeItem(TinkerCommons.matBloodyBone);
    whitestone.setCraftable(true); whitestone.addItemIngot("endstone"); whitestone.setRepresentativeItem(Blocks.END_STONE);
    skyslimeVine.setCraftable(true); safeAdd(skyslimeVine, new ItemStack(TinkerWorld.slimeVineBlue1), Material.VALUE_Ingot, true);
    earthslime.setCraftable(true); earthslime.addItemIngot("slimecrystalGreen");
    skyslime.setCraftable(true); skyslime.addItemIngot("slimecrystalBlue");
    slimeball.setCraftable(true); slimeball.addItemIngot("slimecrystalGreen");
    magma.setCraftable(true); magma.addItemIngot("slimecrystalMagma");
    enderslimeVine.setCraftable(true); safeAdd(enderslimeVine, new ItemStack(TinkerWorld.slimeVinePurple1), Material.VALUE_Ingot, true);
  }

  /**
   * Resuelve el ítem de otro mod por su id de registro. Devuelve ItemStack.EMPTY si el mod no está
   * cargado o el id no existe, para no crashear: los materiales dependientes se quedan sin insumos.
   */
  private static ItemStack getModItem(String name) {
    Item item = Item.getByNameOrId(name);
    if(item == null) {
      return ItemStack.EMPTY;
    }
    return new ItemStack(item);
  }

  private void safeAdd(Material material, ItemStack item, int value) {
    this.safeAdd(material, item, value, false);
  }

  private void safeAddOredicted(Material material, String oredict, ItemStack representative) {
    material.addItem(oredict, 1, Material.VALUE_Ingot);
    material.setRepresentativeItem(representative);
  }

  private void safeAdd(Material material, ItemStack itemStack, int value, boolean representative) {
    if(!itemStack.isEmpty()) {
      material.addItem(itemStack, 1, value);
      if(representative) {
        material.setRepresentativeItem(itemStack);
      }
    }
  }

  public void registerToolMaterialStats() {
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

  public void registerBowMaterialStats() {
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
    TinkerRegistry.addMaterialStats(leather, bowstring);
    TinkerRegistry.addMaterialStats(string, bowstring);
    TinkerRegistry.addMaterialStats(vine, bowstring);
    // TC3 tier 2: ambas vides del Nether son cuerda de arco además de binding
    TinkerRegistry.addMaterialStats(weepingvine, bowstring);
    TinkerRegistry.addMaterialStats(twistingvine, bowstring);
    TinkerRegistry.addMaterialStats(slimevine_blue, bowstring);
    TinkerRegistry.addMaterialStats(slimevine_purple, bowstring);
  }

  public void registerProjectileMaterialStats() {
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

  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    if(TinkerTools.shard == null) {
      return;
    }

    // each material without a shard set gets the default one set
    for(Material material : TinkerRegistry.getAllMaterials()) {
      ItemStack shard = TinkerTools.shard.getItemstackWithMaterial(material);

      material.addRecipeMatch(new RecipeMatch.ItemCombination(Material.VALUE_Shard, shard));
      if(material.getShard() != null) {
        material.setShard(shard);
      }
    }
  }
}
