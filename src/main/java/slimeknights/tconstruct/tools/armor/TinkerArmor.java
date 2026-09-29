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
import slimeknights.tconstruct.tools.modifiers.ModWings;
import slimeknights.tconstruct.tools.modifiers.ModRespiration;
import slimeknights.tconstruct.tools.modifiers.ModAquaAffinity;
import slimeknights.tconstruct.tools.modifiers.ModDepthStrider;
import slimeknights.tconstruct.tools.modifiers.ModLongFall;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateBoots;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateChestplate;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateHelmet;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateLeggings;
import slimeknights.tconstruct.tools.TinkerModifiers;

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
  public static ToolPart maille;

  public static ArmorCore helmet;
  public static ArmorCore chestplate;
  public static ArmorCore leggings;
  public static ArmorCore boots;
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
  public static ModWings modWings;
  public static ModRespiration modRespiration;
  public static ModAquaAffinity modAquaAffinity;
  public static ModDepthStrider modDepthStrider;
  public static ModLongFall modLongFall;

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {}

  @Override
  @SubscribeEvent
  public void registerItems(Register<Item> event) {
    super.registerItems(event);
    for(ArmorCore tool : allTools()) {
      if(tool != null) TinkerRegistry.registerTool(tool);
    }
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
    // modificadores de armadura portados de 1.20.1 (wings/ELYTRA + utilidades por pieza)
    modWings = regMod(new ModWings(), "slimeball", "blockSlime", "feather");
    modRespiration = regMod(new ModRespiration(), "sponge", "blockSponge");
    modAquaAffinity = regMod(new ModAquaAffinity(), "prismarine_shard", "prismarine_crystals");
    modDepthStrider = regMod(new ModDepthStrider(), "prismarine_shard");
    modLongFall = regMod(new ModLongFall(), "feather");
  }

  private static <T extends slimeknights.tconstruct.tools.modifiers.ToolModifier> T regMod(T mod, String... items) {
    for(String item : items) mod.addItem(item);
    return mod;
  }

  private static java.util.List<ArmorCore> allTools() {
    java.util.List<ArmorCore> tools = new java.util.ArrayList<>(4);
    java.util.Collections.addAll(tools, helmet, chestplate, leggings, boots);
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
        {"maille", Material.VALUE_Ingot * 2},
    };
    ToolPart[] out = new ToolPart[parts.length];
    for(int i = 0; i < parts.length; i++) {
      out[i] = registerToolPart(registry, new ToolPart((int) parts[i][1]), (String) parts[i][0]);
    }
    plateHelmet = out[0];
    plateChestplate = out[1];
    plateLeggings = out[2];
    plateBoots = out[3];
    maille = out[4];
  }

  @Override
  protected void registerTools(IForgeRegistry<Item> registry) {
    helmet = registerTool(registry, new ItemArmorPlateHelmet(new PartMaterialType(plateHelmet, ArmorMaterialStats.TYPE_HELMET), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_helmet");
    chestplate = registerTool(registry, new ItemArmorPlateChestplate(new PartMaterialType(plateChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_chestplate");
    leggings = registerTool(registry, new ItemArmorPlateLeggings(new PartMaterialType(plateLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_leggings");
    boots = registerTool(registry, new ItemArmorPlateBoots(new PartMaterialType(plateBoots, ArmorMaterialStats.TYPE_BOOTS), new PartMaterialType(maille, ArmorMaterialStats.TYPE_MAILLE)), "plate_boots");
  }

  private void registerArmorMaterialStats() {
    Object[][] data = {
      {TinkerMaterials.stone, 6, 1,2,2,1, 0f, 0f, 0.074f, 0.06f, 0.012f, 0f},
      {TinkerMaterials.obsidian, 11, 2,5,4,2, 0f, 0f, 0.094f, 0.11f, 0.022f, 0f},
      {TinkerMaterials.knightslime, 33, 3,8,6,3, 2f, 1f, 0.482f, 0.33f, 0.066f, 0.2f},
      {TinkerMaterials.cobalt, 30, 3,8,6,3, 1f, 0.5f, 0.32f, 0.3f, 0.06f, 0.1f},
      {TinkerMaterials.ardite, 24, 3,5,6,3, 0.5f, 0.5f, 0.221f, 0.24f, 0.048f, 0.05f},
      {TinkerMaterials.manyullyn, 35, 3,8,6,3, 3f, 1f, 0.6f, 0.35f, 0.07f, 0.3f},
      {TinkerMaterials.iron, 15, 3,6,5,3, 0.5f, 0.5f, 0.185f, 0.15f, 0.03f, 0.05f},
      {TinkerMaterials.gold, 7, 1,4,3,1, 0f, 0f, 0.078f, 0.07f, 0.014f, 0f},
      {TinkerMaterials.pigiron, 23, 2,5,4,2, 1f, 0.5f, 0.292f, 0.23f, 0.046f, 0.1f},
      {TinkerMaterials.steel, 29, 3,8,6,3, 2f, 0.5f, 0.466f, 0.29f, 0.058f, 0.2f},
      {TinkerMaterials.slimesteel, 40, 3,7,6,3, 1f, 0.5f, 0.36f, 0.4f, 0.08f, 0.1f},
      {TinkerMaterials.cinderslime, 42, 3,8,6,3, 2f, 1f, 0.518f, 0.42f, 0.08f, 0.2f},
      {TinkerMaterials.alubrass, 9, 2,3,4,2, 0f, 0f, 0.086f, 0.09f, 0.018f, 0f},
      {TinkerMaterials.alumite, 28, 3,6,7,3, 1f, 0.5f, 0.312f, 0.28f, 0.056f, 0.1f},
      {TinkerMaterials.copper, 13, 2,4,3,2, 0.5f, 0.5f, 0.177f, 0.13f, 0.026f, 0.05f},
      {TinkerMaterials.bronze, 28, 3,7,6,3, 1f, 0.5f, 0.312f, 0.28f, 0.056f, 0.1f},
      {TinkerMaterials.lead, 12, 3,5,4,2, 0.5f, 0.5f, 0.173f, 0.12f, 0.024f, 0.05f},
      {TinkerMaterials.silver, 18, 3,6,5,2, 0.5f, 0.5f, 0.197f, 0.18f, 0.036f, 0.05f},
      {TinkerMaterials.electrum, 14, 3,5,4,2, 0.5f, 0.5f, 0.181f, 0.14f, 0.028f, 0.05f},
      {TinkerMaterials.tin, 9, 1,2,2,1, 0f, 0f, 0.086f, 0.09f, 0.018f, 0f},
      {TinkerMaterials.aluminum, 13, 3,7,5,3, 0.5f, 0.5f, 0.177f, 0.13f, 0.026f, 0.05f},
      {TinkerMaterials.nickel, 14, 2,4,5,2, 0.5f, 0.5f, 0.181f, 0.14f, 0.028f, 0.05f},
      {TinkerMaterials.steeleaf, 10, 3,8,6,3, 1f, 0.5f, 0.24f, 0.1f, 0.02f, 0.1f},
      {TinkerMaterials.ironwood, 8, 3,5,6,3, 0.5f, 0.5f, 0.157f, 0.08f, 0.016f, 0.05f},
      {TinkerMaterials.fiery, 25, 4,9,7,4, 2f, 1f, 0.45f, 0.25f, 0.05f, 0.2f},
      {TinkerMaterials.knightmetal, 20, 3,8,6,3, 2f, 1f, 0.43f, 0.2f, 0.04f, 0.2f},
      {TinkerMaterials.kobold, 28, 3,6,8,3, 1f, 0.5f, 0.312f, 0.28f, 0.056f, 0.1f},
      {TinkerMaterials.magnetite, 26, 3,6,7,3, 1f, 0.5f, 0.304f, 0.26f, 0.052f, 0.1f},
      {TinkerMaterials.knightly, 32, 3,6,8,3, 1f, 0.5f, 0.328f, 0.32f, 0.064f, 0.1f},
      {TinkerMaterials.jadeite, 20, 3,6,7,3, 0.5f, 0.5f, 0.205f, 0.2f, 0.04f, 0.05f},
      {TinkerMaterials.osmium, 25, 3,6,4,2, 0.5f, 0.5f, 0.225f, 0.25f, 0.05f, 0.05f},
      {TinkerMaterials.searedstone, 14, 3,5,4,2, 0.5f, 0.5f, 0.181f, 0.14f, 0.028f, 0.05f},
      {TinkerMaterials.scorchedstone, 10, 3,6,5,2, 0.5f, 0.5f, 0.165f, 0.1f, 0.02f, 0.05f},
      {TinkerMaterials.amethyst, 12, 1,3,4,1, 0f, 0f, 0.098f, 0.12f, 0.024f, 0f},
      {TinkerMaterials.quartz, 10, 1,2,3,1, 0f, 0f, 0.09f, 0.1f, 0.02f, 0f},
      {TinkerMaterials.amethystbronze, 28, 3,7,6,3, 1f, 0.5f, 0.312f, 0.28f, 0.056f, 0.1f},
      {TinkerMaterials.rosegold, 9, 2,5,3,1, 0f, 0f, 0.086f, 0.09f, 0.018f, 0f},
      {TinkerMaterials.hepatizon, 32, 3,8,6,3, 2f, 1f, 0.478f, 0.32f, 0.064f, 0.2f},
      {TinkerMaterials.queensslime, 50, 3,8,6,3, 2f, 1f, 0.55f, 0.5f, 0.08f, 0.2f},
      {TinkerMaterials.constantan, 25, 3,6,5,2, 1f, 0.5f, 0.3f, 0.25f, 0.05f, 0.1f},
      {TinkerMaterials.invar, 24, 3,6,4,2, 1f, 0.5f, 0.296f, 0.24f, 0.048f, 0.1f},
      {TinkerMaterials.pewter, 16, 3,8,6,3, 1f, 0.5f, 0.264f, 0.16f, 0.032f, 0.1f},
      {TinkerMaterials.nicrosil, 28, 3,8,6,3, 2f, 1f, 0.462f, 0.28f, 0.056f, 0.2f},
      {TinkerMaterials.necronium, 18, 3,4,5,3, 1f, 0.5f, 0.272f, 0.18f, 0.036f, 0.1f},
      {TinkerMaterials.ancient, 25, 3,7,5,3, 2f, 1f, 0.45f, 0.25f, 0.05f, 0.2f},
      {TinkerMaterials.netherite, 40, 4,7,9,4, 3f, 1f, 0.6f, 0.4f, 0.08f, 0.3f},
      {TinkerMaterials.dragonscale, 12, 2,5,6,2, 0.5f, 0f, 0.173f, 0.12f, 0.024f, 0.05f},
      {TinkerMaterials.darkthread, 24, 3,6,5,3, 1f, 0.5f, 0.296f, 0.24f, 0.048f, 0.1f},
      {TinkerMaterials.jeweledhide, 20, 3,6,5,3, 0.5f, 0.5f, 0.205f, 0.2f, 0.04f, 0.05f},
    };
    for(Object[] d : data) {
      Material m = (Material)d[0];
      int f = (int)d[1];
      float t = d.length > 6 ? (float)d[6] : 0f;
      float k = d.length > 7 ? (float)d[7] : 0f;
      // stats nuevas opcionales por material (si valen 0 se derivan de la formula)
      float hardness = d.length > 8 ? (float)d[8] : 0f;
      float environmental = d.length > 9 ? (float)d[9] : 0f;
      float weight = d.length > 10 ? (float)d[10] : 0f;
      float ricochet = d.length > 11 ? (float)d[11] : 0f;
      addMatStats(m, f, (int)d[2], (int)d[3], (int)d[4], (int)d[5], t, k, hardness, environmental, weight, ricochet);
    }
    // prioridad final: el data-pack 1.20.1 (data/tconstruct/tinkering/materials/stats/*.json) sobreescribe la tabla
    try { slimeknights.tconstruct.common.JsonMaterialLoader.loadDataPackStats(); } catch(Exception e) { }
  }

  private void addMatStats(Material mat, int factor, int h, int c, int l, int b, float toughness, float knockback,
                           float hardnessIn, float environmentalIn, float weightIn, float ricochetIn) {
    // valores base: si el material define uno explicito se usa; si no, se deriva del tier/complejidad
    float hardnessBase = hardnessIn > 0f ? Math.min(0.9f, hardnessIn) : Math.min(0.60f, 0.05f + toughness * 0.15f + factor * 0.004f);
    float environmentalBase = environmentalIn > 0f ? Math.min(0.9f, environmentalIn) : Math.min(0.50f, factor * 0.010f);
    float ricochetBase = ricochetIn > 0f ? Math.min(0.9f, ricochetIn) : Math.min(0.35f, toughness * 0.10f);
    float weightBase = weightIn > 0f ? weightIn : Math.min(0.08f, factor * 0.0020f);
    // escalado por cobertura del cuerpo: pechera > pantalon > casco > botas
    // (factorW = factor de peso; factorS = factor de hardness/environmental/ricochet)
    addPiece(mat, ArmorMaterialStats.TYPE_HELMET, factor*12, h, toughness, knockback, hardnessBase, environmentalBase, ricochetBase, weightBase, 0.6f, 0.9f);
    addPiece(mat, ArmorMaterialStats.TYPE_CHESTPLATE, factor*18, c, toughness, knockback, hardnessBase, environmentalBase, ricochetBase, weightBase, 1.6f, 1.4f);
    addPiece(mat, ArmorMaterialStats.TYPE_LEGGINGS, factor*17, l, toughness, knockback, hardnessBase, environmentalBase, ricochetBase, weightBase, 1.3f, 1.1f);
    addPiece(mat, ArmorMaterialStats.TYPE_BOOTS, factor*14, b, toughness, knockback, hardnessBase, environmentalBase, ricochetBase, weightBase, 0.5f, 0.6f);
    // el escudo (TYPE_SHIELD) NO se registra: plate_shield no se porta (otro mod del pack lo implementa)
    addPiece(mat, ArmorMaterialStats.TYPE_MAILLE, factor*5, 1, 0f, 0f, hardnessBase, environmentalBase, ricochetBase, weightBase, 0.4f, 0.4f);
  }

  /** Registra la stat de una pieza. El peso aporta 50% a KB y 40% a la dureza; las resistencias escalan por cobertura. */
  private static void addPiece(Material mat, String type, int dur, int armor, float toughness, float knockback,
                               float hardnessBase, float environmentalBase, float ricochetBase, float weightBase,
                               float weightFactor, float statFactor) {
    float weight = -weightBase * weightFactor;
    float hardness = Math.min(0.9f, hardnessBase * statFactor + (-weight) * 0.4f);
    float environmental = Math.min(0.9f, environmentalBase * statFactor);
    float ricochet = Math.min(0.9f, ricochetBase * statFactor);
    float kb = Math.min(1f, knockback + (-weight) * 0.5f);
    TinkerRegistry.addMaterialStats(mat, new ArmorMaterialStats(type, dur, armor, toughness, kb, hardness, environmental, weight, ricochet));
  }

  @Override
  @Subscribe
  public void init(FMLInitializationEvent event) {
    super.init(event);
    proxy.init();
    java.util.List<slimeknights.tconstruct.library.tools.ToolCore> all = new java.util.ArrayList<>();
    all.addAll(allTools());
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
