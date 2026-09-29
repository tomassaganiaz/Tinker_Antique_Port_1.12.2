package slimeknights.tconstruct.tools.armor;

import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.common.CommonProxy;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.AbstractToolPulse;
import slimeknights.tconstruct.tools.armor.item.ArmorCore;
import slimeknights.tconstruct.tools.modifiers.ModBlastProtection;
import slimeknights.tconstruct.tools.modifiers.ModChip;
import slimeknights.tconstruct.tools.modifiers.ModDoubleJump;
import slimeknights.tconstruct.tools.modifiers.ModFeatherFalling;
import slimeknights.tconstruct.tools.modifiers.ModFireProtection;
import slimeknights.tconstruct.tools.modifiers.ModMagicProtection;
import slimeknights.tconstruct.tools.modifiers.ModMeleeProtection;
import slimeknights.tconstruct.tools.modifiers.ModProjectileProtection;
import slimeknights.tconstruct.tools.modifiers.ModProtection;
import slimeknights.tconstruct.tools.modifiers.ReinforcementRecipeMatch;
import slimeknights.tconstruct.tools.modifiers.ModKnockbackResistance;
import slimeknights.tconstruct.tools.modifiers.ModRevitalizing;
import slimeknights.tconstruct.tools.modifiers.ModShulking;
import slimeknights.tconstruct.tools.modifiers.ModStrength;
import slimeknights.tconstruct.tools.modifiers.ModThorns;
import slimeknights.tconstruct.tools.modifiers.ModPyroclastic;
import slimeknights.tconstruct.tools.modifiers.ModShattering;
import slimeknights.tconstruct.tools.modifiers.ModSlippery;
import slimeknights.tconstruct.tools.modifiers.ModSlurping;
import slimeknights.tconstruct.tools.modifiers.ModSpilling;
import slimeknights.tconstruct.tools.modifiers.ModTwin;
import slimeknights.tconstruct.tools.modifiers.ModWetting;
import slimeknights.tconstruct.tools.modifiers.ModZooming;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateBoots;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateChestplate;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateHelmet;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateLeggings;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.armor.item.ItemPlateShield;

@Pulse(id = TinkerArmor.PulseId, description = "Tinkers Plate Armor")
public class TinkerArmor extends AbstractToolPulse {

  public static final String PulseId = "TinkerArmor";
  static final org.apache.logging.log4j.Logger log = Util.getLogger(PulseId);

  @net.minecraftforge.fml.common.SidedProxy(clientSide = "slimeknights.tconstruct.tools.armor.ArmorClientProxy", serverSide = "slimeknights.tconstruct.common.CommonProxy")
  public static CommonProxy proxy;

  public static ToolPart plateHelmet;
  public static ToolPart plateChestplate;
  public static ToolPart plateLeggings;
  public static ToolPart plateBoots;
  public static ToolPart plateShield;
  public static ToolPart maille;
  public static ToolPart slimePlateHelmet;
  public static ToolPart slimePlateChestplate;
  public static ToolPart slimePlateLeggings;
  public static ToolPart slimePlateBoots;
  public static ToolPart travelersPlateHelmet;
  public static ToolPart travelersPlateChestplate;
  public static ToolPart travelersPlateLeggings;
  public static ToolPart travelersPlateBoots;

  public static ArmorCore helmet;
  public static ArmorCore chestplate;
  public static ArmorCore leggings;
  public static ArmorCore boots;
  public static ItemPlateShield shield;
  public static ArmorCore slimeHelmet;
  public static ArmorCore slimeChestplate;
  public static ArmorCore slimeLeggings;
  public static ArmorCore slimeArmorBoots;
  public static ArmorCore travelersHelmet;
  public static ArmorCore travelersChestplate;
  public static ArmorCore travelersLeggings;
  public static ArmorCore travelersBoots;
  public static ModProtection modProtection;
  public static ModProjectileProtection modProjectileProtection;
  public static ModBlastProtection modBlastProtection;
  public static ModFireProtection modFireProtection;
  public static ModMagicProtection modMagicProtection;
  public static ModMeleeProtection modMeleeProtection;
  public static ModThorns modThorns;
  public static ModStrength modStrength;
  public static ModRevitalizing modRevitalizing;
  public static ModShulking modShulking;
  public static ModKnockbackResistance modKnockbackResistance;
  public static ModFeatherFalling modFeatherFalling;
  public static ModDoubleJump modDoubleJump;
  public static ModTwin modTwin;
  public static ModSpilling modSpilling;
  public static ModWetting modWetting;
  public static ModSlurping modSlurping;
  public static ModShattering modShattering;
  public static ModSlippery modSlippery;
  public static ModPyroclastic modPyroclastic;
  public static ModChip modChip;
  public static ModZooming modZooming;

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {}

  @Override
  @SubscribeEvent
  public void registerItems(Register<Item> event) {
    super.registerItems(event);
    for(ArmorCore tool : allTools()) {
      if(tool != null) TinkerRegistry.registerTool(tool);
    }
    if(shield != null) TinkerRegistry.registerTool(shield);
    modProtection = regMod(new ModProtection());
    modProtection.addRecipeMatch(new ReinforcementRecipeMatch(
      new ItemStack(slimeknights.tconstruct.shared.TinkerCommons.matReinforcement.getItem(), 1, slimeknights.tconstruct.shared.TinkerCommons.matReinforcement.getMetadata())));
    modProtection.addRecipeMatch(new ReinforcementRecipeMatch(
      new ItemStack(slimeknights.tconstruct.shared.TinkerCommons.matReinforceCobalt.getItem(), 1, slimeknights.tconstruct.shared.TinkerCommons.matReinforceCobalt.getMetadata())));
    modProtection.addRecipeMatch(new ReinforcementRecipeMatch(
      new ItemStack(slimeknights.tconstruct.shared.TinkerCommons.matReinforceGold.getItem(), 1, slimeknights.tconstruct.shared.TinkerCommons.matReinforceGold.getMetadata())));
    modProtection.addRecipeMatch(new ReinforcementRecipeMatch(
      new ItemStack(slimeknights.tconstruct.shared.TinkerCommons.matReinforceObsidian.getItem(), 1, slimeknights.tconstruct.shared.TinkerCommons.matReinforceObsidian.getMetadata())));
    modProtection.addRecipeMatch(new ReinforcementRecipeMatch(
      new ItemStack(slimeknights.tconstruct.shared.TinkerCommons.matReinforceSeared.getItem(), 1, slimeknights.tconstruct.shared.TinkerCommons.matReinforceSeared.getMetadata())));
    modProjectileProtection = regMod(new ModProjectileProtection(), "blockWool", "wool");
    modProjectileProtection.addItem(slimeknights.tconstruct.shared.TinkerCommons.matReinforcement, 2, 1);
    modBlastProtection = regMod(new ModBlastProtection(), "blockObsidian", "obsidian");
    modBlastProtection.addItem(slimeknights.tconstruct.shared.TinkerCommons.matReinforceObsidian, 2, 1);
    modFireProtection = regMod(new ModFireProtection(), "blockMagma", "dustBlaze");
    modFireProtection.addItem(slimeknights.tconstruct.shared.TinkerCommons.matReinforceSeared, 2, 1);
    modMagicProtection = regMod(new ModMagicProtection(), "gemEmerald", "blockEmerald");
    modMagicProtection.addItem(slimeknights.tconstruct.shared.TinkerCommons.matReinforceGold, 2, 1);
    modMeleeProtection = regMod(new ModMeleeProtection());
    modMeleeProtection.addItem(slimeknights.tconstruct.shared.TinkerCommons.matReinforceCobalt, 2, 1);
    modFeatherFalling = regMod(new ModFeatherFalling(), "feather", "blockWool");
    modDoubleJump = regMod(new ModDoubleJump(), "slimeball", "blockSlime");
    modTwin = regMod(new ModTwin(), "slimeball", "blockSlime");
    modSpilling = regMod(new ModSpilling(), "water_bucket", "bucketWater");
    modWetting = regMod(new ModWetting(), "sponge", "blockSponge");
    modSlurping = regMod(new ModSlurping(), "bucketLava", "water_bucket");
    modShattering = regMod(new ModShattering(), "blockGlass", "paneGlass");
    modSlippery = regMod(new ModSlippery(), "slimeball", "blockSlime");
    modPyroclastic = regMod(new ModPyroclastic(), "blockMagma", "magma_cream");
    modChip = regMod(new ModChip(), "nuggetIron", "ingotIron");
    modZooming = regMod(new ModZooming(), "glass", "paneGlass");
    modThorns = regMod(new ModThorns(), "blockCactus", "cactus");
    modStrength = regMod(new ModStrength(), "blazePowder", "blazeRod");
    modRevitalizing = regMod(new ModRevitalizing());
    if(slimeknights.tconstruct.smeltery.TinkerSmeltery.diamondApple != null) {
      modRevitalizing.addItem(slimeknights.tconstruct.smeltery.TinkerSmeltery.diamondApple, 2, 1);
    }
    modRevitalizing.addItem("appleDiamond", 2, 1);
    modRevitalizing.addItem("diamondApple", 2, 1);
    modShulking = TinkerModifiers.modShulking;
    modShulking.addItem("blockShulker");
    modShulking.addItem("shulkerShell");
    modKnockbackResistance = regMod(new ModKnockbackResistance(), "blockAnvil", "anvil");
  }

  private static <T extends slimeknights.tconstruct.tools.modifiers.ToolModifier> T regMod(T mod, String... items) {
    for(String item : items) mod.addItem(item);
    return mod;
  }

  private static java.util.List<ArmorCore> allTools() {
    java.util.List<ArmorCore> tools = new java.util.ArrayList<>(13);
    java.util.Collections.addAll(tools, helmet, chestplate, leggings, boots,
        slimeHelmet, slimeChestplate, slimeLeggings, slimeArmorBoots,
        travelersHelmet, travelersChestplate, travelersLeggings, travelersBoots);
    return tools;
  }

  @SubscribeEvent
  public void registerModels(ModelRegistryEvent event) {
    proxy.registerModels();
  }

  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit();
    registerArmorMaterialStats();
  }

  @Override
  protected void registerToolParts(IForgeRegistry<Item> registry) {
    Object[][] parts = {
        {"plating_helmet", Material.VALUE_Ingot * 5},
        {"plating_chestplate", Material.VALUE_Ingot * 8},
        {"plating_leggings", Material.VALUE_Ingot * 7},
        {"plating_boots", Material.VALUE_Ingot * 4},
        {"plating_shield", Material.VALUE_Ingot * 6},
        {"maille", Material.VALUE_Ingot * 2},
        {"slime_plating_helmet", Material.VALUE_Ingot * 5},
        {"slime_plating_chestplate", Material.VALUE_Ingot * 8},
        {"slime_plating_leggings", Material.VALUE_Ingot * 7},
        {"slime_plating_boots", Material.VALUE_Ingot * 4},
        {"travelers_plating_helmet", Material.VALUE_Ingot * 5},
        {"travelers_plating_chestplate", Material.VALUE_Ingot * 8},
        {"travelers_plating_leggings", Material.VALUE_Ingot * 7},
        {"travelers_plating_boots", Material.VALUE_Ingot * 4},
    };
    ToolPart[] out = new ToolPart[parts.length];
    for(int i = 0; i < parts.length; i++) {
      out[i] = registerToolPart(registry, new ToolPart((int) parts[i][1]), (String) parts[i][0]);
    }
    plateHelmet = out[0];
    plateChestplate = out[1];
    plateLeggings = out[2];
    plateBoots = out[3];
    plateShield = out[4];
    maille = out[5];
    slimePlateHelmet = out[6];
    slimePlateChestplate = out[7];
    slimePlateLeggings = out[8];
    slimePlateBoots = out[9];
    travelersPlateHelmet = out[10];
    travelersPlateChestplate = out[11];
    travelersPlateLeggings = out[12];
    travelersPlateBoots = out[13];
  }

  @Override
  protected void registerTools(IForgeRegistry<Item> registry) {
    helmet = registerTool(registry, new ItemArmorPlateHelmet(new PartMaterialType(plateHelmet, ArmorMaterialStats.TYPE_HELMET), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_helmet");
    chestplate = registerTool(registry, new ItemArmorPlateChestplate(new PartMaterialType(plateChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_chestplate");
    leggings = registerTool(registry, new ItemArmorPlateLeggings(new PartMaterialType(plateLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_leggings");
    boots = registerTool(registry, new ItemArmorPlateBoots(new PartMaterialType(plateBoots, ArmorMaterialStats.TYPE_BOOTS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_boots");
    shield = registerTool(registry, new ItemPlateShield(new PartMaterialType(plateShield, ArmorMaterialStats.TYPE_SHIELD)), "plate_shield");
    slimeHelmet = registerTool(registry, new ItemArmorPlateHelmet(new PartMaterialType(slimePlateHelmet, ArmorMaterialStats.TYPE_HELMET), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "slime_helmet");
    slimeChestplate = registerTool(registry, new ItemArmorPlateChestplate(new PartMaterialType(slimePlateChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "slime_chestplate");
    slimeLeggings = registerTool(registry, new ItemArmorPlateLeggings(new PartMaterialType(slimePlateLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "slime_leggings");
    slimeArmorBoots = registerTool(registry, new ItemArmorPlateBoots(new PartMaterialType(slimePlateBoots, ArmorMaterialStats.TYPE_BOOTS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "slime_armor_boots");
    travelersHelmet = registerTool(registry, new ItemArmorPlateHelmet(new PartMaterialType(travelersPlateHelmet, ArmorMaterialStats.TYPE_HELMET), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "travelers_helmet");
    travelersChestplate = registerTool(registry, new ItemArmorPlateChestplate(new PartMaterialType(travelersPlateChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "travelers_chestplate");
    travelersLeggings = registerTool(registry, new ItemArmorPlateLeggings(new PartMaterialType(travelersPlateLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "travelers_leggings");
    travelersBoots = registerTool(registry, new ItemArmorPlateBoots(new PartMaterialType(travelersPlateBoots, ArmorMaterialStats.TYPE_BOOTS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "travelers_boots");
  }

  private void registerArmorMaterialStats() {
    Object[][] data = {
      {TinkerMaterials.wood, 5, 1,1,1,1},
      {TinkerMaterials.stone, 6, 1,2,2,1},
      {TinkerMaterials.flint, 7, 1,2,2,1},
      {TinkerMaterials.cactus, 6, 1,1,2,1},
      {TinkerMaterials.bone, 8, 1,2,2,1},
      {TinkerMaterials.obsidian, 11, 2,4,5,2},
      {TinkerMaterials.prismarine, 9, 2,3,4,2},
      {TinkerMaterials.endstone, 7, 1,2,3,1},
      {TinkerMaterials.paper, 3, 1,1,1,1},
      {TinkerMaterials.sponge, 4, 1,1,1,1},
      {TinkerMaterials.slime, 9, 1,2,3,1},
      {TinkerMaterials.blueslime, 9, 1,2,3,1},
      {TinkerMaterials.knightslime, 33, 2,5,7,2, 1f},
      {TinkerMaterials.magmaslime, 10, 2,3,4,2},
      {TinkerMaterials.netherrack, 6, 1,1,2,1},
      {TinkerMaterials.cobalt, 30, 2,5,7,2, 1f},
      {TinkerMaterials.ardite, 24, 2,4,5,2},
      {TinkerMaterials.manyullyn, 35, 2,5,7,2, 3f},
      {TinkerMaterials.firewood, 6, 1,1,2,1},
      {TinkerMaterials.iron, 15, 2,4,5,2},
      {TinkerMaterials.pigiron, 23, 1,3,4,1},
      {TinkerMaterials.steel, 29, 2,5,7,2, 2f},
      {TinkerMaterials.slimesteel, 40, 2,5,6,2},
      {TinkerMaterials.cinderslime, 42, 2,5,6,2},
      {TinkerMaterials.alubrass, 9, 2,3,4,2},
      {TinkerMaterials.alumite, 28, 2,5,6,2},
      {TinkerMaterials.copper, 13, 1,2,3,1},
      {TinkerMaterials.bronze, 28, 2,5,6,2},
      {TinkerMaterials.lead, 12, 1,3,4,2},
      {TinkerMaterials.silver, 18, 1,4,5,2},
      {TinkerMaterials.electrum, 14, 1,3,4,2},
      {TinkerMaterials.tin, 9, 1,2,2,1},
      {TinkerMaterials.aluminum, 13, 2,4,6,2},
      {TinkerMaterials.nickel, 14, 1,3,4,1},
      {TinkerMaterials.steeleaf, 10, 2,5,7,2},
      {TinkerMaterials.ironwood, 8, 2,4,5,2},
      {TinkerMaterials.fiery, 25, 3,6,8,3},
      {TinkerMaterials.knightmetal, 40, 3,6,8,3, 2f, 0.05f},
      {TinkerMaterials.kobold, 28, 2,5,7,2},
      {TinkerMaterials.magnetite, 26, 2,5,6,2},
      {TinkerMaterials.knightly, 32, 2,5,7,2},
      {TinkerMaterials.ichor, 22, 2,4,6,2},
      {TinkerMaterials.ichorskin, 18, 2,4,5,2},
      {TinkerMaterials.jadeite, 20, 2,5,6,2},
      {TinkerMaterials.ancienthide, 26, 3,6,8,3},
      {TinkerMaterials.osmium, 24, 2,5,6,2},
      {TinkerMaterials.turtle, 18, 2,4,6,2},
      {TinkerMaterials.nautilus, 16, 2,4,5,2},
      {TinkerMaterials.searedstone, 14, 1,3,4,2},
      {TinkerMaterials.scorchedstone, 10, 1,4,5,2},
      {TinkerMaterials.amethyst, 12, 1,3,4,1},
      {TinkerMaterials.quartz, 10, 1,2,3,1},
      {TinkerMaterials.amethystbronze, 28, 2,5,6,2},
      {TinkerMaterials.rosegold, 9, 1,3,5,2},
      {TinkerMaterials.hepatizon, 32, 2,5,7,2},
      {TinkerMaterials.queensslime, 50, 2,5,7,2},
      {TinkerMaterials.constantan, 25, 1,4,5,2},
      {TinkerMaterials.invar, 24, 1,3,5,2},
      {TinkerMaterials.pewter, 16, 2,5,7,2},
      {TinkerMaterials.nicrosil, 28, 2,5,7,2},
      {TinkerMaterials.necronium, 18, 2,3,4,2, 0.5f},
      {TinkerMaterials.blazingbone, 18, 1,3,4,2},
      {TinkerMaterials.ancient, 25, 2,4,6,2},
      {TinkerMaterials.netherite, 40, 3,6,8,3, 3f},
      {TinkerMaterials.bloodbone, 13, 1,2,3,1},
      {TinkerMaterials.bamboo, 6, 1,2,3,1},
      {TinkerMaterials.dragonscale, 12, 2,5,6,2, 0.5f},
    };
    for(Object[] d : data) {
      Material m = (Material)d[0];
      int f = (int)d[1];
      float t = d.length > 6 ? (float)d[6] : 0f;
      float k = d.length > 7 ? (float)d[7] : 0f;
      addMatStats(m, f, (int)d[2], (int)d[3], (int)d[4], (int)d[5], t, k);
    }
  }

  private void addMatStats(Material mat, int factor, int h, int c, int l, int b, float toughness, float knockback) {
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_HELMET, factor*11, h, toughness, knockback));
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_CHESTPLATE, factor*16, c, toughness, knockback));
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_LEGGINGS, factor*15, l, toughness, knockback));
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_BOOTS, factor*13, b, toughness, knockback));
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_SHIELD, factor*18, 0, toughness, knockback));
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(ArmorMaterialStats.TYPE_MAILLE, factor*5, 1, 0, 0));
  }

  @Override
  @Subscribe
  public void init(FMLInitializationEvent event) {
    super.init(event);
    proxy.init();
    java.util.List<slimeknights.tconstruct.library.tools.ToolCore> all = new java.util.ArrayList<>();
    all.addAll(allTools());
    if(shield != null) all.add(shield);
    for(slimeknights.tconstruct.library.tools.ToolCore tool : all) {
      TinkerRegistry.registerToolCrafting(tool);
    }
    ArmorDispenserBehavior.registerAll();
  }

  @Override
  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
    proxy.postInit();
  }

  @Override
  protected void registerEventHandlers() {
    ArmorEventHandler.register();
  }
}
