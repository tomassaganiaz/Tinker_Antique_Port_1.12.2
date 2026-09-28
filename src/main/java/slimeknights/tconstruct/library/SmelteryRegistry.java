package slimeknights.tconstruct.library;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.events.AlloyRegisterEvent;
import slimeknights.tconstruct.library.events.BasinCastingRegisterEvent;
import slimeknights.tconstruct.library.events.EntityMeltingRegisterEvent;
import slimeknights.tconstruct.library.events.MeltingRegisterEvent;
import slimeknights.tconstruct.library.events.SmelteryFuelRegisterEvent;
import slimeknights.tconstruct.library.events.TableCastingRegisterEvent;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.library.utils.FluidUtil;
import slimeknights.mantle.util.RecipeMatch;

import static slimeknights.tconstruct.library.TinkerRegistry.log;

/** 
Registro de recetas de fundido, vaciado, aleaciones, combustible y fundido de entidades.
 */
public final class 
SmelteryRegistry
 {

  private 
SmelteryRegistry
() {
  }

  private static List<MeltingRecipe> meltingRegistry = new ObjectArrayList<>();
  private static List<ICastingRecipe> tableCastRegistry = new ObjectArrayList<>();
  private static List<ICastingRecipe> basinCastRegistry = new ObjectArrayList<>();
  private static List<AlloyRecipe> alloyRegistry = new ObjectArrayList<>();
  private static Map<FluidStack, Integer> smelteryFuels = new Object2IntOpenHashMap<>();
  private static Map<ResourceLocation, FluidStack> entityMeltingRegistry = new Object2ObjectOpenHashMap<>();

  public static void registerMelting(Item item, Fluid fluid, int amount) {
    ItemStack stack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
    registerMelting(new MeltingRecipe(new RecipeMatch.Item(stack, 1, amount), fluid));
  }

  public static void registerMelting(Block block, Fluid fluid, int amount) {
    ItemStack stack = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
    registerMelting(new MeltingRecipe(new RecipeMatch.Item(stack, 1, amount), fluid));
  }

  public static void registerMelting(ItemStack stack, Fluid fluid, int amount) {
    registerMelting(new MeltingRecipe(new RecipeMatch.ItemCombination(amount, stack), fluid));
  }

  public static void registerMelting(String oredict, Fluid fluid, int amount) {
    registerMelting(new MeltingRecipe(new RecipeMatch.Oredict(oredict, 1, amount), fluid));
  }

  public static void registerMelting(MeltingRecipe recipe) {
    if(Arrays.stream(Config.fluidIgnore).anyMatch(f -> f.equals(recipe.output.getFluid().getName()))) {
      return;
    }
    if(new MeltingRegisterEvent(recipe).fire()) {
      meltingRegistry.add(recipe);
    }
    else {
      try {
        String input = recipe.input.getInputs().stream().findFirst().map(ItemStack::getUnlocalizedName).orElse("?");
        log.debug("Registration of melting recipe for " + recipe.getResult().getUnlocalizedName() + " from " + input + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging melting event", e);
      }
    }
  }

  public static MeltingRecipe getMelting(ItemStack stack) {
    for(MeltingRecipe recipe : meltingRegistry) {
      if(recipe.matches(stack)) {
        return recipe;
      }
    }

    return null;
  }

  public static List<MeltingRecipe> getAllMeltingRecipies() {
    return ImmutableList.copyOf(meltingRegistry);
  }

  public static void registerAlloy(FluidStack result, FluidStack... inputs) {
    if(result.amount < 1) {
      log.fatal("Alloy Recipe: Resulting alloy {} has to have an amount ({})", result.getLocalizedName(), result.amount);
      return;
    }
    if(inputs.length < 2) {
      log.fatal("Alloy Recipe: Alloy for {} must consist of at least 2 liquids", result.getLocalizedName());
      return;
    }

    registerAlloy(new AlloyRecipe(result, inputs));
  }

  public static void registerAlloy(AlloyRecipe recipe) {
    if(new AlloyRegisterEvent(recipe).fire()) {
      alloyRegistry.add(recipe);
    }
    else {
      try {
        String input = recipe.getFluids().stream().map(FluidStack::getUnlocalizedName).collect(Collectors.joining(", "));
        String output = recipe.getResult().getUnlocalizedName();
        log.debug("Registration of alloy recipe for " + output + " from [" + input + "] has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging alloy event", e);
      }
    }
  }

  public static List<AlloyRecipe> getAlloys() {
    return ImmutableList.copyOf(alloyRegistry);
  }

  public static void registerTableCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount) {
    if(Arrays.stream(Config.fluidIgnore).anyMatch(f -> f.equals(fluid.getName()))) {
      return;
    }
    RecipeMatch rm = null;
    if(cast != ItemStack.EMPTY) {
      rm = RecipeMatch.ofNBT(cast);
    }
    registerTableCasting(new CastingRecipe(output, rm, fluid, amount));
  }

  public static void registerTableCasting(ICastingRecipe recipe) {
    if(new TableCastingRegisterEvent(recipe).fire()) {
      tableCastRegistry.add(recipe);
    }
    else {
      try {
        String output = Optional.ofNullable(recipe.getResult(ItemStack.EMPTY, FluidRegistry.WATER)).map(ItemStack::getUnlocalizedName).orElse("Unknown");
        log.debug("Registration of table casting recipe for " + output + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging table casting event", e);
      }
    }
  }

  public static ICastingRecipe getTableCasting(ItemStack cast, Fluid fluid) {
    for(ICastingRecipe recipe : tableCastRegistry) {
      if(recipe.matches(cast, fluid)) {
        return recipe;
      }
    }
    return null;
  }

  public static List<ICastingRecipe> getAllTableCastingRecipes() {
    return ImmutableList.copyOf(tableCastRegistry);
  }

  public static void registerBasinCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount) {
    if(Arrays.stream(Config.fluidIgnore).anyMatch(f -> f.equals(fluid.getName()))) {
      return;
    }
    RecipeMatch rm = null;
    if(!cast.isEmpty()) {
      rm = RecipeMatch.ofNBT(cast);
    }
    registerBasinCasting(new CastingRecipe(output, rm, fluid, amount));
  }

  public static void registerBasinCasting(ICastingRecipe recipe) {
    if(new BasinCastingRegisterEvent(recipe).fire()) {
      basinCastRegistry.add(recipe);
    }
    else {
      try {
        String output = Optional.ofNullable(recipe.getResult(ItemStack.EMPTY, FluidRegistry.WATER)).map(ItemStack::getUnlocalizedName).orElse("Unknown");
        log.debug("Registration of basin casting recipe for " + output + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging basin casting event", e);
      }
    }
  }

  public static ICastingRecipe getBasinCasting(ItemStack cast, Fluid fluid) {
    for(ICastingRecipe recipe : basinCastRegistry) {
      if(recipe.matches(cast, fluid)) {
        return recipe;
      }
    }
    return null;
  }

  public static List<ICastingRecipe> getAllBasinCastingRecipes() {
    return ImmutableList.copyOf(basinCastRegistry);
  }

  public static void registerSmelteryFuel(FluidStack fluidStack, int fuelDuration) {
    if(new SmelteryFuelRegisterEvent(fluidStack, fuelDuration).fire()) {
      smelteryFuels.put(fluidStack, fuelDuration);
    }
    else {
      try {
        String input = fluidStack.getUnlocalizedName();
        log.debug("Registration of smeltery fuel " + input + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging smeltery fuel event", e);
      }
    }
  }

  public static boolean isSmelteryFuel(FluidStack in) {
    for(Map.Entry<FluidStack, Integer> entry : smelteryFuels.entrySet()) {
      if(entry.getKey().isFluidEqual(in)) {
        return true;
      }
    }

    return false;
  }

  public static int consumeSmelteryFuel(FluidStack in) {
    for(Map.Entry<FluidStack, Integer> entry : smelteryFuels.entrySet()) {
      if(entry.getKey().isFluidEqual(in)) {
        FluidStack fuel = entry.getKey();
        int out = entry.getValue();
        if(in.amount < fuel.amount) {
          float coeff = (float) in.amount / (float) fuel.amount;
          out = Math.round(coeff * in.amount);
          in.amount = 0;
        }
        else {
          in.amount -= fuel.amount;
        }

        return out;
      }
    }

    return 0;
  }

  public static Collection<FluidStack> getSmelteryFuels() {
    return ImmutableSet.copyOf(smelteryFuels.keySet());
  }

  public static void registerEntityMelting() {
    for(String entry : Config.entityMelting) {
      String[] parts = entry.split(";");
      if(parts.length != 4) {
        log.error("Invalid entity melting entry: {}", entry);
        continue;
      }
      String entityRL = parts[0];
      String subtypes = parts[1];
      String fluidName = parts[2];
      int amount;
      try {
        amount = Integer.parseInt(parts[3]);
      } catch(NumberFormatException e) {
        log.error("Invalid fluid amount in entity melting entry: {}", entry);
        continue;
      }
      ResourceLocation entityLocation;
      try {
        entityLocation = new ResourceLocation(entityRL);
        if(!Loader.isModLoaded(entityLocation.getResourceDomain())) {
          continue;
        }
      } catch(Exception e) {
        log.error("Invalid entity resource location: {}", entityRL);
        continue;
      }
      EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityLocation);
      if(entityEntry == null) {
        log.error("Entity not found for melting: {}", entityRL);
        continue;
      }
      Fluid fluid = FluidRegistry.getFluid(fluidName);
      if(fluid == null) {
        log.error("Fluid not found for entity melting: {}", fluidName);
        continue;
      }
      Class<? extends Entity> entityClass = entityEntry.getEntityClass();
      FluidStack fluidStack = new FluidStack(fluid, amount);
      if(subtypes.equals("true")) {
          registerEntityMeltingForAll(entityClass, fluidStack);
      } else {
          registerEntityMelting(entityClass, fluidStack);
      }
    }
  }

  public static void registerEntityMeltingForAll(Class<? extends Entity> clazz, FluidStack liquid) {
    for(EntityEntry entry : ForgeRegistries.ENTITIES) {
      Class<? extends Entity> entityClass = entry.getEntityClass();
      if(clazz.isAssignableFrom(entityClass)) {
        registerEntityMelting(entityClass, liquid);
      }
    }
  }

  public static void registerEntityMelting(Class<? extends Entity> clazz, FluidStack liquid) {
    ResourceLocation name = EntityList.getKey(clazz);

    if(name == null) {
      log.fatal("Entity Melting: Entity {} is not registered in the EntityList", clazz.getSimpleName());
      return;
    }

    EntityMeltingRegisterEvent event = new EntityMeltingRegisterEvent(clazz, liquid);
    if(event.fire()) {
      entityMeltingRegistry.put(name, event.getNewFluidStack());
    }
    else {
      try {
        String output = liquid.getUnlocalizedName();
        log.debug("Registration of entity melting for " + clazz.getName() + " into " + output + " has been cancelled by event");
      } catch(Exception e) {
        log.error("Error when logging entity melting event", e);
      }
    }
    log.info("Registered entity melting for {} into {} mB of {}", clazz.getSimpleName(), liquid.amount, liquid.getLocalizedName());
  }

  public static FluidStack getMeltingForEntity(Entity entity) {
    ResourceLocation name = EntityList.getKey(entity);
    FluidStack fluidStack = entityMeltingRegistry.get(name);
    // check if the fluid is the correct one to use
    return Optional.ofNullable(fluidStack)
                   .map(slimeknights.tconstruct.library.utils.FluidUtil::getValidFluidStackOrNull)
                   .orElse(null);
  }

  public static Map<ResourceLocation, FluidStack> getAllEntityMeltingRecipes() {
    return ImmutableMap.copyOf(entityMeltingRegistry);
  }

}
