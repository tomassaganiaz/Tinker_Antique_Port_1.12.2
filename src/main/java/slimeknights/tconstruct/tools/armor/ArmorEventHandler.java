package slimeknights.tconstruct.tools.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.Tags;
import slimeknights.tconstruct.tools.armor.item.ArmorCore;

public class ArmorEventHandler {

  public static final ArmorEventHandler INSTANCE = new ArmorEventHandler();
  private static final UUID REVITALIZING_UUID = UUID.fromString("e3d3c4a1-7e6b-4a2b-9b3c-8d7e6f5a4b3c");

  private ArmorEventHandler() {}

  public static void register() {
    MinecraftForge.EVENT_BUS.register(INSTANCE);
  }

  @SubscribeEvent
  public void onKnockback(LivingKnockBackEvent event) {
    if(!(event.getEntity() instanceof EntityPlayer)) return;
    EntityPlayer p = (EntityPlayer) event.getEntity();
    float r = 0f;
    for(ItemStack s : p.getArmorInventoryList()) if(s.getItem() instanceof ArmorCore && s.hasTagCompound()) r += TagUtil.getToolTag(s).getFloat(Tags.KNOCKBACK_RESISTANCE);
    if(r > 0) event.setStrength(event.getStrength() * (1f - Math.min(1f, r)));
  }

  @SubscribeEvent
  public void onHurt(LivingHurtEvent event) {
    if(!(event.getEntity() instanceof EntityPlayer)) return;
    EntityPlayer p = (EntityPlayer) event.getEntity();
    DamageSource src = event.getSource();
    float red = 0f;
    float hardness = 0f;
    float ricochet = 0f;
    int thornsLvl = 0;
    int strengthLvl = 0;
    int shulkingLvl = 0;
    for(ItemStack s : p.getArmorInventoryList()) {
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()
          || slimeknights.tconstruct.library.utils.ToolHelper.isBroken(s)) continue;
      NBTTagCompound tag = TagUtil.getToolTag(s);
      red += tag.getFloat(Tags.PROTECTION) * 0.10f;
      if(isDirectMeleeDamage(src)) red += tag.getFloat(Tags.MELEE_PROTECTION) * 0.10f;
      if(src.isFireDamage()) red += tag.getFloat(Tags.FIRE_PROTECTION) * 0.10f;
      if(src.isMagicDamage()) red += tag.getFloat(Tags.MAGIC_PROTECTION) * 0.10f;
      if(src.isProjectile()) red += tag.getFloat(Tags.PROJECTILE_PROTECTION) * 0.10f;
      if(src.isExplosion()) red += tag.getFloat(Tags.BLAST_PROTECTION) * 0.10f;
      if("fall".equals(src.getDamageType())) red += (tag.getFloat(Tags.FEATHER_FALLING) * 0.12f + tag.getFloat(Tags.LONG_FALL) * 0.08f);
      if(isEnvironmentalDamage(src)) red += tag.getFloat(Tags.ENVIRONMENTAL_PROTECTION) * 0.15f;
      hardness += tag.getFloat(Tags.HARDNESS);
      ricochet = Math.max(ricochet, tag.getFloat(Tags.RICOCHET));
      net.minecraft.nbt.NBTTagList mods = TagUtil.getModifiersTagList(s);
      for(int i=0;i<mods.tagCount();i++) {
        net.minecraft.nbt.NBTTagCompound mtag = mods.getCompoundTagAt(i);
        slimeknights.tconstruct.library.modifiers.ModifierNBT data = slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(mtag);
        if("thorns".equals(data.identifier)) thornsLvl += data.level == 0 ? 1 : data.level;
        if("strength".equals(data.identifier)) strengthLvl = Math.max(strengthLvl, data.level == 0 ? 1 : data.level);
        if("shulking".equals(data.identifier)) shulkingLvl += data.level == 0 ? 1 : data.level;
      }
    }
    if(shulkingLvl > 0 && p.isSneaking()) {
      red += 0.4f;
      // also reduce outgoing damage when attacker is sneaking with shulking – handled below
    }
    // attacker shulking reduces damage dealt while sneaking
    if(src.getTrueSource() instanceof EntityPlayer) {
      EntityPlayer attacker = (EntityPlayer) src.getTrueSource();
      if(attacker.isSneaking()) {
        int atkShulk = 0;
        for(ItemStack s : attacker.getArmorInventoryList()) {
          if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()
              || slimeknights.tconstruct.library.utils.ToolHelper.isBroken(s)) continue;
          net.minecraft.nbt.NBTTagList mods = TagUtil.getModifiersTagList(s);
          for(int i=0;i<mods.tagCount();i++) {
            slimeknights.tconstruct.library.modifiers.ModifierNBT data = slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(mods.getCompoundTagAt(i));
            if("shulking".equals(data.identifier)) atkShulk += data.level == 0 ? 1 : data.level;
          }
        }
        if(atkShulk > 0) event.setAmount(event.getAmount() * 0.8f);
      }
    }
    if(thornsLvl > 0 && src.getTrueSource() instanceof net.minecraft.entity.EntityLivingBase) {
      boolean isThorns = src instanceof net.minecraft.util.EntityDamageSource && ((net.minecraft.util.EntityDamageSource)src).getIsThornsDamage();
      if(!isThorns) {
        net.minecraft.entity.EntityLivingBase attacker = (net.minecraft.entity.EntityLivingBase) src.getTrueSource();
        float chance = thornsLvl * 0.15f;
        if(p.getRNG().nextFloat() < chance) {
          float dmg = 1 + p.getRNG().nextInt(4);
          attacker.attackEntityFrom(DamageSource.causeThornsDamage(p), dmg);
        }
      }
    }
    if(strengthLvl > 0 && !src.isUnblockable()) {
      int duration = strengthLvl * 5 * 20;
      p.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.init.MobEffects.STRENGTH, duration, 0, false, true));
    }
    // ricochet: chance de rebotar el proyectil en vez de recibir el golpe
    if(!event.isCanceled() && src.isProjectile() && ricochet > 0 && p.getRNG().nextFloat() < Math.min(1f, ricochet)) {
      if(src.getImmediateSource() instanceof net.minecraft.entity.projectile.EntityArrow) {
        net.minecraft.entity.projectile.EntityArrow arrow = (net.minecraft.entity.projectile.EntityArrow) src.getImmediateSource();
        arrow.motionX = -arrow.motionX;
        arrow.motionY = -arrow.motionY;
        arrow.motionZ = -arrow.motionZ;
      }
      event.setCanceled(true);
      return;
    }
    if(red > 0) event.setAmount(event.getAmount() * (1f - Math.min(0.8f, red)));
    // vanilla only damages instanceof ItemArmor, so Tinker armor would never wear down: damage one random piece per hit
    if(!event.isCanceled() && event.getAmount() > 0 && !src.isUnblockable()) {
      // hardness reduce la chance de desgastar una pieza
      float durResistance = Math.min(0.9f, hardness);
      if(p.getRNG().nextFloat() >= durResistance) {
        java.util.List<ItemStack> wearable = new java.util.ArrayList<>();
        for(ItemStack s : p.getArmorInventoryList()) {
          if(s.getItem() instanceof ArmorCore && s.hasTagCompound()) wearable.add(s);
        }
        if(!wearable.isEmpty()) {
          ItemStack damaged = wearable.get(p.getRNG().nextInt(wearable.size()));
          slimeknights.tconstruct.library.utils.ToolHelper.damageTool(damaged, 1, p);
        }
      }
    }
  }

  /** Daño de entorno: cactus, rayo, ahogo, hambre, choque con muro, etc. (no cubierto por las protecciones existentes).
   *  OJO: no incluir "fall" — ya lo cubre Feather Falling (evita doble conteo). */
  private static boolean isEnvironmentalDamage(DamageSource source) {
    String type = source.getDamageType();
    return "cactus".equals(type) || "lightningBolt".equals(type) || "drown".equals(type)
        || "starve".equals(type) || "flyIntoWall".equals(type) || "inWall".equals(type)
        || "hotFloor".equals(type) || "outOfWorld".equals(type);
  }

  /** Reaplica un efecto de poción si esta por expirar (usado por Respiration/Aqua Affinity/Depth Strider). */
  private static void ensureEffect(EntityPlayer p, net.minecraft.potion.Potion potion, int duration, int amplifier) {
    net.minecraft.potion.PotionEffect current = p.getActivePotionEffect(potion);
    if(current == null || current.getDuration() < 40) {
      p.addPotionEffect(new net.minecraft.potion.PotionEffect(potion, duration, amplifier, false, false));
    }
  }

  private boolean isDirectMeleeDamage(DamageSource source) {
    return source.getImmediateSource() instanceof net.minecraft.entity.EntityLivingBase
        && source.getImmediateSource() == source.getTrueSource()
        && !source.isProjectile()
        && !source.isExplosion()
        && !source.isFireDamage()
        && !source.isMagicDamage()
        && !"fall".equals(source.getDamageType());
  }

  @SubscribeEvent
  public void onFall(LivingFallEvent event) {
    if(!(event.getEntity() instanceof EntityPlayer)) return;
    EntityPlayer p = (EntityPlayer) event.getEntity();
    if(hasDoubleJump(p)) {
      if(event.getDistance() < 6) event.setCanceled(true);
      else event.setDamageMultiplier(0.5f);
    }
  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {
    if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
    EntityPlayer p = (EntityPlayer) event.getEntityLiving();
    int total = 0;
    for(ItemStack s : p.getArmorInventoryList()) {
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()
          || slimeknights.tconstruct.library.utils.ToolHelper.isBroken(s)) continue;
      net.minecraft.nbt.NBTTagList mods = TagUtil.getModifiersTagList(s);
      for(int i=0;i<mods.tagCount();i++) {
        slimeknights.tconstruct.library.modifiers.ModifierNBT data = slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(mods.getCompoundTagAt(i));
        if("revitalizing".equals(data.identifier)) total += data.level == 0 ? 1 : data.level;
      }
    }
    // respiration / aqua affinity / depth strider / wings
    boolean resp = false, aqua = false, wings = false;
    float depth = 0f;
    for(ItemStack s : p.getArmorInventoryList()) {
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()
          || slimeknights.tconstruct.library.utils.ToolHelper.isBroken(s)) continue;
      NBTTagCompound tag = TagUtil.getToolTag(s);
      if(tag.getFloat(Tags.RESPIRATION) > 0) resp = true;
      if(tag.getFloat(Tags.AQUA_AFFINITY) > 0) aqua = true;
      depth += tag.getFloat(Tags.DEPTH_STRIDER);
      if(tag.getFloat(Tags.WINGS) > 0) wings = true;
    }
    if(p.isInWater()) {
      if(resp) ensureEffect(p, net.minecraft.init.MobEffects.WATER_BREATHING, 200, 0);
      if(aqua) ensureEffect(p, net.minecraft.init.MobEffects.HASTE, 200, 0);
      if(depth > 0) ensureEffect(p, net.minecraft.init.MobEffects.SPEED, 60, 0);
    }
    // wings (ELYTRA): planeo simplificado (slow-fall) mientras se cae
    if(wings && !p.onGround && !p.isInWater() && !p.capabilities.isFlying && !p.isElytraFlying() && p.motionY < 0) {
      p.fallDistance = 0f;
      p.motionY = Math.max(p.motionY, -0.35);
    }
    IAttributeInstance maxHealth = p.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
    AttributeModifier mod = maxHealth.getModifier(REVITALIZING_UUID);
    double extra = total * 2.0;
    if(total > 0) {
      if(mod == null || mod.getAmount() != extra) {
        if(mod != null) maxHealth.removeModifier(mod);
        maxHealth.applyModifier(new AttributeModifier(REVITALIZING_UUID, "Revitalizing", extra, 0));
        if(p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
      }
    } else {
      if(mod != null) {
        maxHealth.removeModifier(mod);
        if(p.getHealth() > p.getMaxHealth()) p.setHealth(p.getMaxHealth());
      }
    }
  }

  public static boolean hasDoubleJump(EntityPlayer p) {
    for(ItemStack s : p.getArmorInventoryList()) {
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()
          || slimeknights.tconstruct.library.utils.ToolHelper.isBroken(s)) continue;
      net.minecraft.nbt.NBTTagList list = TagUtil.getModifiersTagList(s);
      for(int i = 0; i < list.tagCount(); i++) {
        if("double_jump".equals(slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(list.getCompoundTagAt(i)).identifier)) {
          return true;
        }
      }
    }
    return false;
  }

  @SubscribeEvent
  public void onInteractArmorStand(PlayerInteractEvent.EntityInteract event) {
    if(!(event.getTarget() instanceof EntityArmorStand)) return;
    EntityArmorStand stand = (EntityArmorStand) event.getTarget();
    ItemStack held = event.getEntityPlayer().getHeldItem(event.getHand());
    if(held.isEmpty() || !(held.getItem() instanceof ArmorCore)) return;
    ArmorCore armor = (ArmorCore) held.getItem();
    net.minecraft.inventory.EntityEquipmentSlot slot = armor.armorType;
    ItemStack existing = stand.getItemStackFromSlot(slot);
    if(!existing.isEmpty()) return;
    if(event.getWorld().isRemote) return;
    ItemStack toEquip = held.splitStack(1);
    stand.setItemStackToSlot(slot, toEquip);
    event.setCanceled(true);
  }
}
