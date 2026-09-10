package slimeknights.tconstruct.foundry;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import org.apache.logging.log4j.Logger;

import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.common.CommonProxy;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.mantle.item.ItemBlockMeta;
import slimeknights.tconstruct.foundry.block.BlockAlloyer;
import slimeknights.tconstruct.foundry.block.BlockFoundryCasting;
import slimeknights.tconstruct.foundry.block.BlockHeater;
import slimeknights.tconstruct.foundry.block.BlockMelter;
import slimeknights.tconstruct.foundry.block.BlockScorchedFaucet;
import slimeknights.tconstruct.smeltery.block.BlockCasting;
import slimeknights.tconstruct.foundry.block.BlockScorched;
import slimeknights.tconstruct.foundry.block.BlockScorchedChannel;
import slimeknights.tconstruct.foundry.block.BlockScorchedController;
import slimeknights.tconstruct.foundry.block.BlockScorchedForge;
import slimeknights.tconstruct.foundry.block.BlockScorchedIO;
import slimeknights.tconstruct.foundry.block.BlockScorchedLantern;
import slimeknights.tconstruct.foundry.block.BlockScorchedTank;
import slimeknights.tconstruct.foundry.tileentity.TileAlloyer;
import slimeknights.tconstruct.foundry.tileentity.TileHeater;
import slimeknights.tconstruct.foundry.tileentity.TileMelter;
import slimeknights.tconstruct.foundry.tileentity.TileScorchedFoundry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.shared.block.BlockSoil;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockTank;
import slimeknights.tconstruct.smeltery.item.ItemTank;

/**
 * Scorched foundry: a blaze-blood powered multiblock that melts items without creating alloys.
 */
@Pulse(id = TinkerScorched.PulseId, description = "The scorched foundry, a blaze-blood powered multiblock",
       pulsesRequired = TinkerSmeltery.PulseId + ";" + TinkerIntegration.PulseId)
public class TinkerScorched extends TinkerPulse {

  public static final String PulseId = "TinkerScorched";
  public static final Logger log = Util.getLogger(PulseId);

  @SidedProxy(clientSide = "slimeknights.tconstruct.foundry.FoundryClientProxy", serverSide = "slimeknights.tconstruct.common.CommonProxy")
  public static CommonProxy proxy;

  // Blocks
  public static BlockScorched blockScorched;
  public static BlockScorchedTank scorchedTank;
  public static BlockScorchedIO scorchedIO;
  public static BlockScorchedController foundryController;
  public static BlockMelter melter;
  public static BlockScorchedChannel scorchedChannel;
  public static BlockScorchedForge scorchedForge;
  public static BlockFoundryCasting foundryCasting;
  public static BlockHeater heater;
  public static BlockAlloyer alloyer;
  public static BlockScorchedFaucet scorchedFaucet;
  public static BlockScorchedLantern scorchedLantern;
  public static Item scorchedBrick;

  public static ImmutableSet<Block> validScorchedBlocks;

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {
    IForgeRegistry<Block> registry = event.getRegistry();

    blockScorched = registerBlock(registry, new BlockScorched(), "block_scorched");
    scorchedTank = registerBlock(registry, new BlockScorchedTank(), "scorched_tank");
    scorchedIO = registerBlock(registry, new BlockScorchedIO(), "scorched_drain");
    foundryController = registerBlock(registry, new BlockScorchedController(), "foundry_controller");
    melter = registerBlock(registry, new BlockMelter(), "melter");
    scorchedChannel = registerBlock(registry, new BlockScorchedChannel(), "scorched_channel");
    scorchedForge = registerBlock(registry, new BlockScorchedForge(), "scorched_toolforge");
    foundryCasting = registerBlock(registry, new BlockFoundryCasting(), "foundry_casting");
    heater = registerBlock(registry, new BlockHeater(), "heater");
    alloyer = registerBlock(registry, new BlockAlloyer(), "scorched_alloyer");
    scorchedFaucet = registerBlock(registry, new BlockScorchedFaucet(), "scorched_faucet");
    scorchedLantern = registerBlock(registry, new BlockScorchedLantern(), "scorched_lantern");

    registerTE(TileScorchedFoundry.class, "foundry_controller");
    registerTE(TileMelter.class, "melter");
    registerTE(TileHeater.class, "heater");
    registerTE(TileAlloyer.class, "scorched_alloyer");
  }

  @SubscribeEvent
  public void registerItems(Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    blockScorched = registerEnumItemBlock(registry, blockScorched);
    scorchedTank = registerItemBlockProp(registry, new ItemTank(scorchedTank), BlockTank.TYPE);
    scorchedIO = registerEnumItemBlock(registry, scorchedIO);
    foundryController = registerItemBlock(registry, foundryController);
    melter = registerItemBlock(registry, melter);
    scorchedChannel = registerItemBlock(registry, scorchedChannel);
    scorchedForge = registerItemBlock(registry, scorchedForge);
    foundryCasting = registerItemBlockProp(registry, new ItemBlockMeta(foundryCasting), BlockCasting.TYPE);
    heater = registerItemBlock(registry, heater);
    alloyer = registerItemBlock(registry, alloyer);
    scorchedFaucet = registerItemBlock(registry, scorchedFaucet);
    scorchedLantern = registerItemBlock(registry, scorchedLantern);
    scorchedBrick = registerItem(registry, new Item().setCreativeTab(TinkerRegistry.tabSmeltery), "scorched_brick");

    ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
    builder.add(blockScorched);
    builder.add(scorchedTank);
    builder.add(scorchedIO);
    builder.add(scorchedChannel);
    builder.add(scorchedForge);
    builder.add(foundryCasting);
    builder.add(heater);
    builder.add(alloyer);
    builder.add(scorchedFaucet);
    builder.add(scorchedLantern);
    validScorchedBlocks = builder.build();
  }

  @SubscribeEvent
  public void registerModels(ModelRegistryEvent event) {
    proxy.registerModels();
  }

  // PRE-INITIALIZATION
  @Subscribe
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit();
  }

  // INITIALIZATION
  @Subscribe
  public void init(FMLInitializationEvent event) {
    registerFurnaceRecipes();
    proxy.init();
  }

  private void registerFurnaceRecipes() {
    ItemStack brick = new ItemStack(blockScorched, 1, BlockScorched.ScorchedType.BRICK.getMeta());
    ItemStack cracked = new ItemStack(blockScorched, 1, BlockScorched.ScorchedType.BRICK_CRACKED.getMeta());
    GameRegistry.addSmelting(brick, cracked, 0.1f);
    // TC3: nether grout se cocina a ladrillo requemado (item)
    if(scorchedBrick != null) {
      ItemStack grout = new ItemStack(TinkerCommons.blockSoil, 1, BlockSoil.SoilTypes.NETHER_GROUT.getMeta());
      GameRegistry.addSmelting(grout, new ItemStack(scorchedBrick), 0.3f);
    }
  }

  // POST-INITIALIZATION
  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    registerMeltingCasting();
    proxy.postInit();
  }

  private void registerMeltingCasting() {
    // TC3: la piedra scorched es su propio material (tier 2), con fluido propio. Antes los bloques
    // scorched se fundían en piedra seared; ahora dan piedra scorched y se vuelven a vaciar con ella.
    TinkerRegistry.registerMelting(blockScorched, TinkerFluids.scorchedStone, Material.VALUE_SearedBlock);

    ItemStack scorchedBrick = new ItemStack(blockScorched, 1, BlockScorched.ScorchedType.BRICK.getMeta());
    TinkerRegistry.registerTableCasting(scorchedBrick, TinkerSmeltery.castIngot, TinkerFluids.scorchedStone, Material.VALUE_SearedMaterial);
    TinkerRegistry.registerMelting(TinkerScorched.scorchedBrick, TinkerFluids.scorchedStone, Material.VALUE_SearedMaterial);

    ItemStack scorchedStone = new ItemStack(blockScorched, 1, BlockScorched.ScorchedType.STONE.getMeta());
    TinkerRegistry.registerBasinCasting(scorchedStone, ItemStack.EMPTY, TinkerFluids.scorchedStone, Material.VALUE_SearedBlock);
  }
}