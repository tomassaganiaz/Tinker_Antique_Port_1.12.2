package slimeknights.tconstruct.tactical;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import net.minecraft.block.Block;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.tactical.tools.*;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.tactical.item.ItemTacticalCrystal;
import slimeknights.tconstruct.tactical.item.ItemTacticalExpBottle;
import slimeknights.tconstruct.tactical.item.ItemTacticalTemplate;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalXpBoost;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalProjectileProtection;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalBlastProtection;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalFireProtection;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalMagicProtection;
import slimeknights.tconstruct.tactical.modifiers.ModTacticalFeatherFalling;
import slimeknights.tconstruct.tactical.worktable.BlockTacticalWorktable;
import slimeknights.tconstruct.tactical.worktable.TileTacticalWorktable;
import slimeknights.tconstruct.tools.AbstractToolPulse;
import slimeknights.tconstruct.tools.armor.item.ArmorCore;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateBoots;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateChestplate;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateHelmet;
import slimeknights.tconstruct.tools.armor.item.ItemArmorPlateLeggings;
import com.google.common.eventbus.Subscribe;

@Pulse(id = TinkerTactical.PulseId, description = "Tinker Tactical Toughness - escudos, nunchakus, lanza y mas")
public class TinkerTactical extends AbstractToolPulse {
    public static final String PulseId = "TinkerTactical";
    public static SwiftShield swiftShield;
    public static HeavyShield heavyShield;
    public static TinkerNunchaku nunchaku;
    public static Spear spear;
    public static Doppelhander doppelhander;
    public static Maraca maraca;
    public static CraftsmanStaff craftsmanStaff;
    public static ArmorCore scoutHelmet;
    public static ArmorCore scoutChestplate;
    public static ArmorCore scoutLeggings;
    public static ArmorCore scoutBoots;
    public static ArmorCore tacticalPlateHelmet;
    public static ArmorCore tacticalPlateChestplate;
    public static ArmorCore tacticalPlateLeggings;
    public static ArmorCore tacticalPlateBoots;
    public static ArmorCore tacticalSlimeHelmet;
    public static ArmorCore tacticalSlimeChestplate;
    public static ArmorCore tacticalSlimeLeggings;
    public static ArmorCore tacticalSlimeBoots;
    // DEPRECADO 1.20.1 shields/wings — se mantiene Swift/Heavy táctico como sistema principal (ver PORT_PORCENTAJE.md)
    // public static ItemPlateShield tacticalTravelersShield;
    // public static ItemPlateShield tacticalPlateShield;
    // public static ArmorCore tacticalSlimeWings;
    public static TacticalVeinHammer tacticalVeinHammer;
    public static TacticalBroadAxe tacticalBroadAxe;
    public static TacticalSledgeHammer tacticalSledgeHammer;
    public static TacticalPickadze tacticalPickadze;
    public static TacticalKama tacticalKama;
    public static TacticalScythe tacticalScythe;
    public static TacticalDagger tacticalDagger;
    public static TacticalSword tacticalSword;
    public static TacticalCleaver tacticalCleaver;
    public static TacticalCrossbow tacticalCrossbow;
    public static TacticalLongbow tacticalLongbow;
    public static TacticalFishingRod tacticalFishingRod;
    public static TacticalJavelin tacticalJavelin;
    public static TacticalArrow tacticalArrow;
    public static TacticalShuriken tacticalShuriken;
    public static TacticalThrowingAxe tacticalThrowingAxe;
    public static TacticalFlintAndBrick tacticalFlintAndBrick;
    public static TacticalSkyStaff tacticalSkyStaff;
    public static TacticalEarthStaff tacticalEarthStaff;
    public static TacticalIchorStaff tacticalIchorStaff;
    public static TacticalEnderStaff tacticalEnderStaff;
    public static TacticalMeltingPan tacticalMeltingPan;
    public static TacticalWarPick tacticalWarPick;
    public static TacticalBattlesign tacticalBattlesign;
    public static TacticalSwasher tacticalSwasher;
    public static TacticalMinotaurAxe tacticalMinotaurAxe;
    public static slimeknights.tconstruct.library.tools.ToolPart tacticalPlatingHelmet;
    public static slimeknights.tconstruct.library.tools.ToolPart tacticalPlatingChestplate;
    public static slimeknights.tconstruct.library.tools.ToolPart tacticalPlatingLeggings;
    public static slimeknights.tconstruct.library.tools.ToolPart tacticalPlatingBoots;
    public static slimeknights.tconstruct.library.tools.ToolPart tacticalPlatingShield;
    public static BlockTacticalWorktable tacticalWorktable;
    public static ItemTacticalCrystal tacticalCrystal;
    public static ItemTacticalExpBottle tacticalExpBottle;
    public static ItemTacticalTemplate tplFarming, tplCombat, tplMining, tplExcavation, tplFelling, tplShearing;
    public static ModTacticalXpBoost modTacticalXp;
    public static ModTacticalProjectileProtection modTacticalProjectile;
    public static ModTacticalBlastProtection modTacticalBlast;
    public static ModTacticalFireProtection modTacticalFire;
    public static ModTacticalMagicProtection modTacticalMagic;
    public static ModTacticalFeatherFalling modTacticalFeather;

    @Override
    protected void registerToolParts(IForgeRegistry<Item> r){
        tacticalPlatingHelmet = registerToolPart(r, new slimeknights.tconstruct.library.tools.ToolPart(slimeknights.tconstruct.library.materials.Material.VALUE_Ingot * 5 + 1), "tactical_plating_helmet");
        tacticalPlatingChestplate = registerToolPart(r, new slimeknights.tconstruct.library.tools.ToolPart(slimeknights.tconstruct.library.materials.Material.VALUE_Ingot * 8 + 1), "tactical_plating_chestplate");
        tacticalPlatingLeggings = registerToolPart(r, new slimeknights.tconstruct.library.tools.ToolPart(slimeknights.tconstruct.library.materials.Material.VALUE_Ingot * 7 + 1), "tactical_plating_leggings");
        tacticalPlatingBoots = registerToolPart(r, new slimeknights.tconstruct.library.tools.ToolPart(slimeknights.tconstruct.library.materials.Material.VALUE_Ingot * 4 + 1), "tactical_plating_boots");
        tacticalPlatingShield = registerToolPart(r, new slimeknights.tconstruct.library.tools.ToolPart(slimeknights.tconstruct.library.materials.Material.VALUE_Ingot * 6 + 1), "tactical_plating_shield");
    }
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        tacticalWorktable = registerBlock(event.getRegistry(), new BlockTacticalWorktable(), "tactical_worktable");
        registerTE(TileTacticalWorktable.class, "tactical_worktable");
    }
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        registerToolParts(r);
        if(tacticalWorktable != null) registerItemBlock(r, tacticalWorktable);
        tacticalCrystal = registerItem(r, new ItemTacticalCrystal(), "tactical_crystal");
        tacticalExpBottle = registerItem(r, new ItemTacticalExpBottle(), "tactical_exp_bottle");
        tplFarming = registerItem(r, new ItemTacticalTemplate("farming", TextFormatting.GOLD), "tactical_template_farming");
        tplCombat = registerItem(r, new ItemTacticalTemplate("combat", TextFormatting.GRAY), "tactical_template_combat");
        tplMining = registerItem(r, new ItemTacticalTemplate("mining", TextFormatting.YELLOW), "tactical_template_mining");
        tplExcavation = registerItem(r, new ItemTacticalTemplate("excavation", TextFormatting.BLUE), "tactical_template_excavation");
        tplFelling = registerItem(r, new ItemTacticalTemplate("felling", TextFormatting.DARK_AQUA), "tactical_template_felling");
        tplShearing = registerItem(r, new ItemTacticalTemplate("shearing", TextFormatting.AQUA), "tactical_template_shearing");
        modTacticalXp = registerModifier(new ModTacticalXpBoost());
        modTacticalXp.addItem("gemEmerald", 1, 1);
        modTacticalProjectile = registerModifier(new ModTacticalProjectileProtection());
        modTacticalProjectile.addItem("blockWool", 1, 1);
        modTacticalBlast = registerModifier(new ModTacticalBlastProtection());
        modTacticalBlast.addItem("blockObsidian", 1, 1);
        modTacticalFire = registerModifier(new ModTacticalFireProtection());
        modTacticalFire.addItem("blockMagma", 1, 1);
        modTacticalMagic = registerModifier(new ModTacticalMagicProtection());
        modTacticalMagic.addItem("gemEmerald", 1, 1);
        modTacticalFeather = registerModifier(new ModTacticalFeatherFalling());
        modTacticalFeather.addItem("feather", 1, 1);
        TinkerRegistry.addTrait(TinkerNunchaku.COMBO);
        swiftShield = registerTool(r, new SwiftShield(), "tactical_swift_shield");
        heavyShield = registerTool(r, new HeavyShield(), "tactical_heavy_shield");
        nunchaku = registerTool(r, new TinkerNunchaku(), "tactical_nunchaku");
        spear = registerTool(r, new Spear(), "tactical_spear");
        doppelhander = registerTool(r, new Doppelhander(), "tactical_doppelhander");
        maraca = registerTool(r, new Maraca(), "tactical_maraca");
        craftsmanStaff = registerTool(r, new CraftsmanStaff(), "tactical_craftsman_staff");
        TinkerRegistry.registerToolCrafting(swiftShield);
        TinkerRegistry.registerToolCrafting(nunchaku);
        TinkerRegistry.registerToolCrafting(spear);
        TinkerRegistry.registerToolCrafting(maraca);
        TinkerRegistry.registerToolCrafting(craftsmanStaff);
        TinkerRegistry.registerToolForgeCrafting(heavyShield);
        TinkerRegistry.registerToolForgeCrafting(doppelhander);
        scoutHelmet = registerTool(r, new ItemArmorPlateHelmet(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingHelmet, ArmorMaterialStats.TYPE_HELMET), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_scout_helmet");
        scoutChestplate = registerTool(r, new ItemArmorPlateChestplate(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_scout_chestplate");
        scoutLeggings = registerTool(r, new ItemArmorPlateLeggings(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_scout_leggings");
        scoutBoots = registerTool(r, new ItemArmorPlateBoots(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingBoots, ArmorMaterialStats.TYPE_BOOTS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_scout_boots");
        tacticalPlateHelmet = registerTool(r, new ItemArmorPlateHelmet(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingHelmet, ArmorMaterialStats.TYPE_HELMET), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_plate_helmet");
        tacticalPlateChestplate = registerTool(r, new ItemArmorPlateChestplate(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_plate_chestplate");
        tacticalPlateLeggings = registerTool(r, new ItemArmorPlateLeggings(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_plate_leggings");
        tacticalPlateBoots = registerTool(r, new ItemArmorPlateBoots(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingBoots, ArmorMaterialStats.TYPE_BOOTS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_plate_boots");
        tacticalSlimeHelmet = registerTool(r, new ItemArmorPlateHelmet(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingHelmet, ArmorMaterialStats.TYPE_HELMET), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_slime_helmet");
        tacticalSlimeChestplate = registerTool(r, new ItemArmorPlateChestplate(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_slime_chestplate");
        tacticalSlimeLeggings = registerTool(r, new ItemArmorPlateLeggings(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingLeggings, ArmorMaterialStats.TYPE_LEGGINGS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_slime_leggings");
        tacticalSlimeBoots = registerTool(r, new ItemArmorPlateBoots(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingBoots, ArmorMaterialStats.TYPE_BOOTS), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_slime_boots");
        // tacticalTravelersShield = registerTool(r, new slimeknights.tconstruct.tools.armor.item.ItemPlateShield(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingShield, ArmorMaterialStats.TYPE_SHIELD)), "tactical_travelers_shield");
        // tacticalPlateShield = registerTool(r, new slimeknights.tconstruct.tools.armor.item.ItemPlateShield(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingShield, ArmorMaterialStats.TYPE_SHIELD)), "tactical_plate_shield");
        // tacticalSlimeWings = registerTool(r, new ItemArmorPlateChestplate(new slimeknights.tconstruct.library.tinkering.PartMaterialType(tacticalPlatingChestplate, ArmorMaterialStats.TYPE_CHESTPLATE), new slimeknights.tconstruct.library.tinkering.PartMaterialType(slimeknights.tconstruct.tools.armor.TinkerArmor.maille, ArmorMaterialStats.TYPE_MAILLE)), "tactical_slime_wings");
        tacticalVeinHammer = registerTool(r, new TacticalVeinHammer(), "tactical_vein_hammer");
        tacticalBroadAxe = registerTool(r, new TacticalBroadAxe(), "tactical_broad_axe");
        tacticalSledgeHammer = registerTool(r, new TacticalSledgeHammer(), "tactical_sledge_hammer");
        tacticalPickadze = registerTool(r, new TacticalPickadze(), "tactical_pickadze");
        tacticalKama = registerTool(r, new TacticalKama(), "tactical_kama");
        tacticalScythe = registerTool(r, new TacticalScythe(), "tactical_scythe");
        tacticalDagger = registerTool(r, new TacticalDagger(), "tactical_dagger");
        tacticalSword = registerTool(r, new TacticalSword(), "tactical_sword");
        tacticalCleaver = registerTool(r, new TacticalCleaver(), "tactical_cleaver");
        tacticalCrossbow = registerTool(r, new TacticalCrossbow(), "tactical_crossbow");
        tacticalLongbow = registerTool(r, new TacticalLongbow(), "tactical_longbow");
        tacticalFishingRod = registerTool(r, new TacticalFishingRod(), "tactical_fishing_rod");
        tacticalJavelin = registerTool(r, new TacticalJavelin(), "tactical_javelin");
        tacticalArrow = registerTool(r, new TacticalArrow(), "tactical_arrow");
        tacticalShuriken = registerTool(r, new TacticalShuriken(), "tactical_shuriken");
        tacticalThrowingAxe = registerTool(r, new TacticalThrowingAxe(), "tactical_throwing_axe");
        tacticalFlintAndBrick = registerTool(r, new TacticalFlintAndBrick(), "tactical_flint_and_brick");
        tacticalSkyStaff = registerTool(r, new TacticalSkyStaff(), "tactical_sky_staff");
        tacticalEarthStaff = registerTool(r, new TacticalEarthStaff(), "tactical_earth_staff");
        tacticalIchorStaff = registerTool(r, new TacticalIchorStaff(), "tactical_ichor_staff");
        tacticalEnderStaff = registerTool(r, new TacticalEnderStaff(), "tactical_ender_staff");
        tacticalMeltingPan = registerTool(r, new TacticalMeltingPan(), "tactical_melting_pan");
        tacticalWarPick = registerTool(r, new TacticalWarPick(), "tactical_war_pick");
        tacticalBattlesign = registerTool(r, new TacticalBattlesign(), "tactical_battlesign");
        tacticalSwasher = registerTool(r, new TacticalSwasher(), "tactical_swasher");
        tacticalMinotaurAxe = registerTool(r, new TacticalMinotaurAxe(), "tactical_minotaur_axe");
        for(ArmorCore a : new ArmorCore[]{scoutHelmet, scoutChestplate, scoutLeggings, scoutBoots, tacticalPlateHelmet, tacticalPlateChestplate, tacticalPlateLeggings, tacticalPlateBoots, tacticalSlimeHelmet, tacticalSlimeChestplate, tacticalSlimeLeggings, tacticalSlimeBoots}) TinkerRegistry.registerTool(a);
        // TinkerRegistry.registerTool(tacticalTravelersShield);
        // TinkerRegistry.registerTool(tacticalPlateShield);
        TinkerRegistry.registerTool(tacticalVeinHammer);
        TinkerRegistry.registerTool(tacticalBroadAxe);
        TinkerRegistry.registerTool(tacticalSledgeHammer);
        TinkerRegistry.registerTool(tacticalPickadze);
        TinkerRegistry.registerTool(tacticalKama);
        TinkerRegistry.registerTool(tacticalScythe);
        TinkerRegistry.registerTool(tacticalDagger);
        TinkerRegistry.registerTool(tacticalSword);
        TinkerRegistry.registerTool(tacticalCleaver);
        TinkerRegistry.registerTool(tacticalCrossbow);
        TinkerRegistry.registerTool(tacticalLongbow);
        TinkerRegistry.registerTool(tacticalFishingRod);
        TinkerRegistry.registerTool(tacticalJavelin);
        TinkerRegistry.registerTool(tacticalArrow);
        TinkerRegistry.registerTool(tacticalShuriken);
        TinkerRegistry.registerTool(tacticalThrowingAxe);
        TinkerRegistry.registerTool(tacticalFlintAndBrick);
        TinkerRegistry.registerTool(tacticalSkyStaff);
        TinkerRegistry.registerTool(tacticalEarthStaff);
        TinkerRegistry.registerTool(tacticalIchorStaff);
        TinkerRegistry.registerTool(tacticalEnderStaff);
        TinkerRegistry.registerTool(tacticalMeltingPan);
        TinkerRegistry.registerTool(tacticalWarPick);
        TinkerRegistry.registerTool(tacticalBattlesign);
        TinkerRegistry.registerTool(tacticalSwasher);
        TinkerRegistry.registerTool(tacticalMinotaurAxe);
    }

    @Subscribe
    public void setupMaterialStats(FMLPreInitializationEvent e) { TacticalConfig.init(new java.io.File(e.getModConfigurationDirectory(), "tactical.cfg")); TacticalMaterials.registerStats(); slimeknights.tconstruct.tactical.network.TacticalNetwork.register(); }
    @Subscribe
    public void setupMaterials(FMLInitializationEvent e) { TacticalMaterials.register(); }
    @Subscribe
    public void init(FMLInitializationEvent e) { super.init(e); net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new TacticalShieldEvents()); net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new TacticalAchievements()); net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new slimeknights.tconstruct.tactical.events.TacticalScoutArmorEvents()); net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new slimeknights.tconstruct.tactical.events.TacticalLevelingEvents()); slimeknights.tconstruct.tactical.worktable.TacticalGuiHandler.register();
        if(net.minecraftforge.fml.common.FMLCommonHandler.instance().getSide().isClient()) net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new slimeknights.tconstruct.tactical.client.TacticalClientEvents());
    }
}
