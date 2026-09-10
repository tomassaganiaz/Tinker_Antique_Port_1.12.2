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
    int thornsLvl = 0;
    int strengthLvl = 0;
    int shulkingLvl = 0;
    for(ItemStack s : p.getArmorInventoryList()) {
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()) continue;
      NBTTagCompound tag = TagUtil.getToolTag(s);
      if(src.isFireDamage()) red += tag.getFloat(Tags.FIRE_PROTECTION) * 0.10f;
      if(src.isMagicDamage()) red += tag.getFloat(Tags.MAGIC_PROTECTION) * 0.10f;
      if(src.isProjectile()) red += tag.getFloat(Tags.PROJECTILE_PROTECTION) * 0.10f;
      if(src.isExplosion()) red += tag.getFloat(Tags.BLAST_PROTECTION) * 0.10f;
      if("fall".equals(src.getDamageType())) red += tag.getFloat(Tags.FEATHER_FALLING) * 0.12f;
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
          if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()) continue;
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
    if(red > 0) event.setAmount(event.getAmount() * (1f - Math.min(0.8f, red)));
    // vanilla only damages instanceof ItemArmor, so Tinker armor would never wear down: damage it here
    if(!event.isCanceled() && event.getAmount() > 0 && !src.isUnblockable()) {
      for(ItemStack s : p.getArmorInventoryList()) {
        if(s.getItem() instanceof ArmorCore && s.hasTagCompound()) {
          slimeknights.tconstruct.library.utils.ToolHelper.damageTool(s, 1, p);
        }
      }
    }
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
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()) continue;
      net.minecraft.nbt.NBTTagList mods = TagUtil.getModifiersTagList(s);
      for(int i=0;i<mods.tagCount();i++) {
        slimeknights.tconstruct.library.modifiers.ModifierNBT data = slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(mods.getCompoundTagAt(i));
        if("revitalizing".equals(data.identifier)) total += data.level == 0 ? 1 : data.level;
      }
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
      if(!(s.getItem() instanceof ArmorCore) || !s.hasTagCompound()) continue;
      net.minecraft.nbt.NBTTagList list = TagUtil.getModifiersTagList(s);
      for(int i = 0; i < list.tagCount(); i++) {
        if("double_jump".equals(slimeknights.tconstruct.library.modifiers.ModifierNBT.readTag(list.getCompoundTagAt(i)).identifier)) {
          return true;
        }
      }
    }
    return false;
  }
}
