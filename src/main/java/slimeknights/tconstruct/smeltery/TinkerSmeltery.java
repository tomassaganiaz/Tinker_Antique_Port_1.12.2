package slimeknights.tconstruct.smeltery;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.mantle.item.ItemBlockMeta;
import slimeknights.mantle.pulsar.pulse.Pulse;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.mantle.util.RecipeMatchRegistry;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.common.CommonProxy;
import slimeknights.tconstruct.common.TinkerPulse;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.BucketCastingRecipe;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.smeltery.PreferenceCastingRecipe;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.shared.block.BlockSlime;
import slimeknights.tconstruct.smeltery.block.BlockCasting;
import slimeknights.tconstruct.smeltery.block.BlockChannel;
import slimeknights.tconstruct.smeltery.block.BlockFaucet;
import slimeknights.tconstruct.smeltery.block.BlockSeared;
import slimeknights.tconstruct.smeltery.block.BlockSearedFurnaceController;
import slimeknights.tconstruct.smeltery.block.BlockSearedGlass;
import slimeknights.tconstruct.smeltery.block.BlockSearedLadder;
import slimeknights.tconstruct.smeltery.block.BlockSearedSlab;
import slimeknights.tconstruct.smeltery.block.BlockSearedSlab2;
import slimeknights.tconstruct.smeltery.block.BlockSearedStairs;
import slimeknights.tconstruct.smeltery.block.BlockSmelteryController;
import slimeknights.tconstruct.smeltery.block.BlockSmelteryIO;
import slimeknights.tconstruct.smeltery.block.BlockTank;
import slimeknights.tconstruct.smeltery.block.BlockTinkerTankController;
import slimeknights.tconstruct.smeltery.item.CastCustom;
import slimeknights.tconstruct.smeltery.item.ItemChannel;
import slimeknights.tconstruct.smeltery.item.ItemDiamondApple;
import slimeknights.tconstruct.smeltery.item.ItemTank;
import slimeknights.tconstruct.smeltery.tileentity.TileCastingBasin;
import slimeknights.tconstruct.smeltery.tileentity.TileCastingTable;
import slimeknights.tconstruct.smeltery.tileentity.TileChannel;
import slimeknights.tconstruct.smeltery.tileentity.TileDrain;
import slimeknights.tconstruct.smeltery.tileentity.TileFaucet;
import slimeknights.tconstruct.smeltery.tileentity.TileSearedFurnace;
import slimeknights.tconstruct.smeltery.tileentity.TileSmeltery;
import slimeknights.tconstruct.smeltery.tileentity.TileSmelteryComponent;
import slimeknights.tconstruct.smeltery.tileentity.TileTank;
import slimeknights.tconstruct.smeltery.tileentity.TileTinkerTank;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.world.TinkerWorld;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Pulse(id = TinkerSmeltery.PulseId, description = "The smeltery and items needed for it")
public class TinkerSmeltery extends TinkerPulse {

  public static final String PulseId = "TinkerSmeltery";
  public static final Logger log = Util.getLogger(PulseId);

  @SidedProxy(clientSide = "slimeknights.tconstruct.smeltery.SmelteryClientProxy", serverSide = "slimeknights.tconstruct.common.CommonProxy")
  public static CommonProxy proxy;

  // Blocks
  public static BlockSeared searedBlock;
  public static BlockSmelteryController smelteryController;
  public static BlockTank searedTank;
  public static BlockFaucet faucet;
  public static BlockChannel channel;
  public static BlockCasting castingBlock;
  public static BlockSmelteryIO smelteryIO;
  public static BlockSearedGlass searedGlass;
  public static BlockSearedLadder searedLadder;

  public static Block searedFurnaceController;
  public static Block tinkerTankController;

  public static BlockSearedSlab searedSlab;
  public static BlockSearedSlab2 searedSlab2;

  // stairs
  public static Block searedStairsStone;
  public static Block searedStairsCobble;
  public static Block searedStairsPaver;
  public static Block searedStairsBrick;
  public static Block searedStairsBrickCracked;
  public static Block searedStairsBrickFancy;
  public static Block searedStairsBrickSquare;
  public static Block searedStairsBrickTriangle;
  public static Block searedStairsBrickSmall;
  public static Block searedStairsRoad;
  public static Block searedStairsTile;
  public static Block searedStairsCreeper;

  // Items
  public static Cast cast;
  public static CastCustom castCustom;
  public static Cast clayCast;
  public static ItemDiamondApple diamondApple;

  // itemstacks!
  public static ItemStack castIngot;
  public static ItemStack castNugget;
  public static ItemStack castGem;
  public static ItemStack castShard;
  public static ItemStack castPlate;
  public static ItemStack castGear;

  static Map<Fluid, Set<Pair<String, Integer>>> knownOreFluids = new Object2ObjectOpenHashMap<>();
  public static List<FluidStack> castCreationFluids = new ObjectArrayList<>();
  public static List<FluidStack> clayCreationFluids = new ObjectArrayList<>();

  public static ImmutableSet<Block> validSmelteryBlocks;
  public static ImmutableSet<Block> searedStairsSlabs;
  public static ImmutableSet<Block> validTinkerTankBlocks;
  public static ImmutableSet<Block> validTinkerTankFloorBlocks;
  public static List<ItemStack> meltingBlacklist = new ObjectArrayList<>();

  @SubscribeEvent
  public void registerBlocks(Register<Block> event) {
    IForgeRegistry<Block> registry = event.getRegistry();

    searedBlock = registerBlock(registry, new BlockSeared(), "seared");
    smelteryController = registerBlock(registry, new BlockSmelteryController(), "smeltery_controller");
    searedTank = registerBlock(registry, new BlockTank(), "seared_tank");
    faucet = registerBlock(registry, new BlockFaucet(), "faucet");
    channel = registerBlock(registry, new BlockChannel(), "channel");
    castingBlock = registerBlock(registry, new BlockCasting(), "casting");
    smelteryIO = registerBlock(registry, new BlockSmelteryIO(), "smeltery_io");
    searedGlass = registerBlock(registry, new BlockSearedGlass(), "seared_glass");
    searedLadder = registerBlock(registry, new BlockSearedLadder(), "seared_ladder");

    searedFurnaceController = registerBlock(registry, new BlockSearedFurnaceController(), "seared_furnace_controller");
    tinkerTankController = registerBlock(registry, new BlockTinkerTankController(), "tinker_tank_controller");

    // slabs
    searedSlab = registerBlock(registry, new BlockSearedSlab(), "seared_slab");
    searedSlab2 = registerBlock(registry, new BlockSearedSlab2(), "seared_slab2");

    // stairs
    searedStairsStone = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.STONE, "seared_stairs_stone");
    searedStairsCobble = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.COBBLE, "seared_stairs_cobble");
    searedStairsPaver = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.PAVER, "seared_stairs_paver");
    searedStairsBrick = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK, "seared_stairs_brick");
    searedStairsBrickCracked = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK_CRACKED, "seared_stairs_brick_cracked");
    searedStairsBrickFancy = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK_FANCY, "seared_stairs_brick_fancy");
    searedStairsBrickSquare = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK_SQUARE, "seared_stairs_brick_square");
    searedStairsBrickTriangle = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK_TRIANGLE, "seared_stairs_brick_triangle");
    searedStairsBrickSmall = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.BRICK_SMALL, "seared_stairs_brick_small");
    searedStairsRoad = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.ROAD, "seared_stairs_road");
    searedStairsTile = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.TILE, "seared_stairs_tile");
    searedStairsCreeper = registerBlockSearedStairsFrom(registry, searedBlock, BlockSeared.SearedType.CREEPER, "seared_stairs_creeper");

    registerTE(TileSmeltery.class, "smeltery_controller");
    registerTE(TileSmelteryComponent.class, "smeltery_component");
    registerTE(TileTank.class, "tank");
    registerTE(TileFaucet.class, "faucet");
    registerTE(TileChannel.class, "channel");
    registerTE(TileCastingTable.class, "casting_table");
    registerTE(TileCastingBasin.class, "casting_basin");
    registerTE(TileDrain.class, "smeltery_drain");
    registerTE(TileSearedFurnace.class, "seared_furnace");
    registerTE(TileTinkerTank.class, "tinker_tank");
  }

  @SubscribeEvent
  public void registerItems(Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    searedBlock = registerEnumItemBlock(registry, searedBlock);
    smelteryController = registerItemBlock(registry, smelteryController);
    searedTank = registerItemBlockProp(registry, new ItemTank(searedTank), BlockTank.TYPE);
    faucet = registerItemBlock(registry, faucet);
    channel = registerItemBlock(registry, new ItemChannel(channel));
    castingBlock = registerItemBlockProp(registry, new ItemBlockMeta(castingBlock), BlockCasting.TYPE);
    smelteryIO = registerEnumItemBlock(registry, smelteryIO);
    searedGlass = registerEnumItemBlock(registry, searedGlass);
    searedLadder = registerEnumItemBlock(registry, searedLadder);

    searedFurnaceController = registerItemBlock(registry, searedFurnaceController);
    tinkerTankController = registerItemBlock(registry, tinkerTankController);

    // slabs
    searedSlab = registerEnumItemBlockSlab(registry, searedSlab);
    searedSlab2 = registerEnumItemBlockSlab(registry, searedSlab2);

    // stairs
    searedStairsStone = registerItemBlock(registry, searedStairsStone);
    searedStairsCobble = registerItemBlock(registry, searedStairsCobble);
    searedStairsPaver = registerItemBlock(registry, searedStairsPaver);
    searedStairsBrick = registerItemBlock(registry, searedStairsBrick);
    searedStairsBrickCracked = registerItemBlock(registry, searedStairsBrickCracked);
    searedStairsBrickFancy = registerItemBlock(registry, searedStairsBrickFancy);
    searedStairsBrickSquare = registerItemBlock(registry, searedStairsBrickSquare);
    searedStairsBrickTriangle = registerItemBlock(registry, searedStairsBrickTriangle);
    searedStairsBrickSmall = registerItemBlock(registry, searedStairsBrickSmall);
    searedStairsRoad = registerItemBlock(registry, searedStairsRoad);
    searedStairsTile = registerItemBlock(registry, searedStairsTile);
    searedStairsCreeper = registerItemBlock(registry, searedStairsCreeper);

    cast = registerItem(registry, new Cast(), "cast");
    castCustom = registerItem(registry, new CastCustom(), "cast_custom");
    castIngot = castCustom.addMeta(0, "ingot", Material.VALUE_Ingot);
    castNugget = castCustom.addMeta(1, "nugget", Material.VALUE_Nugget);
    castGem = castCustom.addMeta(2, "gem", Material.VALUE_Gem);
    castPlate = castCustom.addMeta(3, "plate", Material.VALUE_Ingot);
    castGear = castCustom.addMeta(4, "gear", Material.VALUE_Ingot * 4);

    if(Config.claycasts) {
      clayCast = registerItem(registry, new Cast(), "clay_cast");
    }

    diamondApple = registerItem(registry, new ItemDiamondApple(), "diamond_apple");

    if(TinkerRegistry.getShard() != null) {
      TinkerRegistry.addCastForItem(TinkerRegistry.getShard());
      castShard = new ItemStack(cast);
      Cast.setTagForPart(castShard, TinkerRegistry.getShard());
    }
    
    // smeltery blocks
    ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
    builder.add(searedBlock);
    builder.add(searedTank);
    builder.add(smelteryIO);
    builder.add(searedGlass);
    builder.add(searedLadder);

    validSmelteryBlocks = builder.build();
    validTinkerTankBlocks = builder.build(); // same blocks right now
    validTinkerTankFloorBlocks = ImmutableSet.of(searedBlock, searedGlass, smelteryIO);

    // seared furnace ceiling blocks, no smelteryIO or seared glass
    // does not affect sides, those are forced to use seared blocks/tanks where relevant
    builder = ImmutableSet.builder();
    builder.add(searedBlock);

    builder.add(searedSlab);
    builder.add(searedSlab2);
    builder.add(searedStairsStone);
    builder.add(searedStairsCobble);
    builder.add(searedStairsPaver);
    builder.add(searedStairsBrick);
    builder.add(searedStairsBrickCracked);
    builder.add(searedStairsBrickFancy);
    builder.add(searedStairsBrickSquare);
    builder.add(searedStairsBrickTriangle);
    builder.add(searedStairsBrickSmall);
    builder.add(searedStairsRoad);
    builder.add(searedStairsTile);
    builder.add(searedStairsCreeper);

    searedStairsSlabs = builder.build();
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
    // done here so they're present for integration in MaterialIntegration and fluids in TinkerFluids are also initialized
    castCreationFluids.add(new FluidStack(TinkerFluids.gold, Material.VALUE_Ingot * 2));

    // always add extra fluids, as we are not sure if they are integrated until the end of postInit and we added recipes using them before integration
    castCreationFluids.add(new FluidStack(TinkerFluids.brass, Material.VALUE_Ingot));
    castCreationFluids.add(new FluidStack(TinkerFluids.alubrass, Material.VALUE_Ingot));

    // add clay casts if enabled
    if(Config.claycasts) {
      clayCreationFluids.add(new FluidStack(TinkerFluids.clay, Material.VALUE_Ingot * 2));
    }

    registerSmelting();

    proxy.init();
  }

  private void registerSmelting() {
    GameRegistry.addSmelting(TinkerCommons.grout, TinkerCommons.searedBrick, 0.4f);

    GameRegistry.addSmelting(new ItemStack(searedBlock, 1, BlockSeared.SearedType.BRICK.getMeta()), new ItemStack(searedBlock, 1, BlockSeared.SearedType.BRICK_CRACKED.getMeta()), 0.1f);
  }

  // POST-INITIALIZATION
  @Subscribe
  public void postInit(FMLPostInitializationEvent event) {
    registerSmelteryFuel();
    registerMeltingCasting();

    // register remaining cast creation
    for(FluidStack fs : castCreationFluids) {
      TinkerRegistry.registerTableCasting(new ItemStack(cast), ItemStack.EMPTY, fs.getFluid(), fs.amount);
      TinkerRegistry.registerTableCasting(new CastingRecipe(castGem, RecipeMatch.of("gemEmerald"), fs, true, true));
      TinkerRegistry.registerTableCasting(new CastingRecipe(castGem, RecipeMatch.of("gemDiamond"), fs, true, true));
      TinkerRegistry.registerTableCasting(new CastingRecipe(castIngot, RecipeMatch.of("ingotBrick"), fs, true, true));
      TinkerRegistry.registerTableCasting(new CastingRecipe(castIngot, RecipeMatch.of("ingotBrickNether"), fs, true, true));
      TinkerRegistry.registerTableCasting(new CastingRecipe(castIngot, new RecipeMatch.Item(TinkerCommons.searedBrick, 1), fs, true, true));
    }

    proxy.postInit();
    TinkerRegistry.tabSmeltery.setDisplayIcon(new ItemStack(searedTank));
  }

  private void registerSmelteryFuel() {
    TinkerRegistry.registerSmelteryFuel(new FluidStack(FluidRegistry.LAVA, 50), 100);
    TinkerRegistry.registerSmelteryFuel(new FluidStack(TinkerFluids.blazingBlood, 50), 150);
  }

  private void registerMeltingCasting() {
    SmelteryRecipes.registerMeltingCasting();
  }

  public static void registerToolpartMeltingCasting(Material material) {
    SmelteryRecipes.registerToolpartMeltingCasting(material);
  }

  public static void registerOredictMeltingCasting(Fluid fluid, String ore) {
    SmelteryRecipes.registerOredictMeltingCasting(fluid, ore);
  }

  public static void registerRecipeOredictMelting() {
    SmelteryRecipes.registerOredictMelting();
  }

  public static void registerAlloys() {
    SmelteryAlloys.register();
  }

  protected static <E extends Enum<E> & EnumBlock.IEnumMeta & IStringSerializable> BlockSearedStairs registerBlockSearedStairsFrom(IForgeRegistry<Block> registry, EnumBlock<E> block, E value, String name) {
    return registerBlock(registry, new BlockSearedStairs(block.getDefaultState().withProperty(block.prop, value)), name);
  }
}
