package slimeknights.tconstruct.library;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import slimeknights.tconstruct.library.events.ModifierRegisterEvent;
import slimeknights.tconstruct.library.modifiers.IModifier;

import static slimeknights.tconstruct.library.TinkerRegistry.log;

/** 
Registro de modificadores y drops de cabeza.
 */
public final class 
ModifierRegistry
 {

  private 
ModifierRegistry
() {
  }

  private static final Map<String, IModifier> modifiers = new Object2ObjectOpenHashMap<>();
  private static final Multimap<Class<? extends EntityLivingBase>, Function<EntityLivingBase, ItemStack>> headDrops = ArrayListMultimap.create();
  private static final Multimap<Class<? extends EntityLivingBase>, ItemStack> headDropsRaw = ArrayListMultimap.create();

  public static void registerModifier(IModifier modifier) {
    registerModifierAlias(modifier, modifier.getIdentifier());
  }

  public static void registerModifierAlias(IModifier modifier, String alias) {
    if(modifiers.containsKey(alias)) {
      log.fatal("Trying to register a modifier with the name " + alias + " but it already is registered");
      return;
    }
    if(new ModifierRegisterEvent(modifier).fire()) {
      modifiers.put(alias, modifier);
    }
    else {
      log.debug("Registration of modifier " + alias + " has been cancelled by event");
    }
  }

  public static IModifier getModifier(String identifier) {
    return modifiers.get(identifier);
  }

  public static Collection<IModifier> getAllModifiers() {
    return ImmutableList.copyOf(modifiers.values());
  }

  public static void registerHeadDropForAll(Class<? extends EntityLivingBase> clazz, ItemStack head) {
    for(EntityEntry entry : ForgeRegistries.ENTITIES) {
      Class<? extends Entity> entityClass = entry.getEntityClass();
      if(clazz.isAssignableFrom(entityClass)) {
        registerHeadDrop((Class<? extends EntityLivingBase>) entityClass, head);
      }
    }
  }

  /**
   * Registers a beheading head drop for an entity
   * @param clazz     Entity class
   * @param callback  Callback function, takes entity as a parameter and returns an item stack
   */
  public static void registerHeadDrop(Class<? extends EntityLivingBase> clazz, Function<EntityLivingBase,ItemStack> callback) {
    headDrops.put(clazz, callback);
    log.info("Registered head drop for {}", clazz.getSimpleName());
  }

  /**
   * Registers a beheading head drop for an entity
   * @param clazz  Entity class
   * @param head   Head that drops from that entity
   */
  public static void registerHeadDrop(Class<? extends EntityLivingBase> clazz, ItemStack head) {
    final ItemStack safeStack = head.copy();
    registerHeadDrop(clazz, e -> safeStack);
    headDropsRaw.put(clazz, head);
  }

  public static Collection<ItemStack> getHeadDrops(EntityLivingBase entity) {
    Collection<ItemStack> drops = new ArrayList<>();
    for(Map.Entry<Class<? extends EntityLivingBase>, Function<EntityLivingBase, ItemStack>> entry : headDrops.entries()) {
      if(entry.getKey().isAssignableFrom(entity.getClass())) {
        ItemStack stack = entry.getValue().apply(entity);
        if (!stack.isEmpty()) {
          drops.add(stack.copy());
        }
      }
    }
    return drops;
  }

  public static ItemStack getHeadDrop(EntityLivingBase entity) {
    for(Map.Entry<Class<? extends EntityLivingBase>, Function<EntityLivingBase, ItemStack>> entry : headDrops.entries()) {
      if(entry.getKey().isAssignableFrom(entity.getClass())) {
        ItemStack stack = entry.getValue().apply(entity);
        if (!stack.isEmpty()) {
          return stack.copy();
        }
      }
    }
    return ItemStack.EMPTY;
  }

  public static Map<Class<? extends EntityLivingBase>, Collection<ItemStack>> getAllSeveringRecipes() {
    return headDropsRaw.asMap();
  }

}
