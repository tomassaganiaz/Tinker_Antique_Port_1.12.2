package slimeknights.tconstruct.library.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.function.Predicate;

import slimeknights.tconstruct.common.TinkerNetwork;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.tools.ranged.IProjectile;
import slimeknights.tconstruct.library.traits.ITrait;

/** 
Logica de combate: ataque, dano, knockback y swing.
 */
public final class 
ToolCombatHelper
 {

  private 
ToolCombatHelper
() {
  }

  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity) {
    return attackEntity(stack, tool, attacker, targetEntity, null, true);
  }

  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity, Entity projectileEntity) {
    return attackEntity(stack, tool, attacker, targetEntity, projectileEntity, false);
  }

  public static boolean attackEntity(ItemStack stack, ToolCore tool, EntityLivingBase attacker, Entity targetEntity, Entity projectileEntity, boolean applyCooldown) {
    // nothing to do, no target?
    if(targetEntity == null || !targetEntity.canBeAttackedWithItem() || targetEntity.hitByEntity(attacker) || !stack.hasTagCompound()) {
      return false;
    }
    if(ToolDurabilityHelper.isBroken(stack)) {
      return false;
    }
    if(attacker == null) {
      return false;
    }
    boolean isProjectile = projectileEntity != null;
    EntityLivingBase target = null;
    EntityPlayer player = null;
    if(targetEntity instanceof EntityLivingBase) {
      target = (EntityLivingBase) targetEntity;
    }
    if(attacker instanceof EntityPlayer) {
      player = (EntityPlayer) attacker;
      if(target instanceof EntityPlayer) {
        if(!player.canAttackPlayer((EntityPlayer) target)) {
          return false;
        }
      }
    }

    // traits on the tool
    List<ITrait> traits = TinkerUtil.getTraitsOrdered(stack);

    // players base damage (includes tools damage stat)
    float baseDamage = (float) attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();

    // missing because not supported by tcon tools: vanilla damage enchantments, we have our own modifiers
    // missing because not supported by tcon tools: vanilla knockback enchantments, we have our own modifiers
    float baseKnockback = attacker.isSprinting() ? 1 : 0;

    // calculate if it's a critical hit
    boolean isCritical = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(MobEffects.BLINDNESS) && !attacker.isRiding();
    for(ITrait trait : traits) {
      if(trait.isCriticalHit(stack, attacker, target)) {
        isCritical = true;
      }
    }

    // calculate actual damage
    float damage = baseDamage;
    if(target != null) {
      for(ITrait trait : traits) {
        damage = trait.damage(stack, attacker, target, baseDamage, damage, isCritical);
      }
    }

    // apply critical damage
    if(isCritical) {
      damage *= 1.5f;
    }

    // calculate cutoff
    damage = calcCutoffDamage(damage, tool.damageCutoff());

    // calculate actual knockback
    float knockback = baseKnockback;
    if(target != null) {
      for(ITrait trait : traits) {
        knockback = trait.knockBack(stack, attacker, target, damage, baseKnockback, knockback, isCritical);
      }
    }

    // missing because not supported by tcon tools: vanilla fire aspect enchantments, we have our own modifiers

    float oldHP = 0;

    double oldVelX = targetEntity.motionX;
    double oldVelY = targetEntity.motionY;
    double oldVelZ = targetEntity.motionZ;

    if(target != null) {
      oldHP = target.getHealth();
    }

    // apply cooldown damage decrease
    SoundEvent sound = null;
    if(player != null) {
      float cooldown = ((EntityPlayer) attacker).getCooledAttackStrength(0.5F);
      sound = cooldown > 0.9f ? SoundEvents.ENTITY_PLAYER_ATTACK_STRONG : SoundEvents.ENTITY_PLAYER_ATTACK_WEAK;
      damage *= (0.2F + cooldown * cooldown * 0.8F);
    }

    // deal the damage
    if(target != null) {
      int hurtResistantTime = target.hurtResistantTime;
      for(ITrait trait : traits) {
        trait.onHit(stack, attacker, target, damage, isCritical);
        // reset hurt reristant time
        target.hurtResistantTime = hurtResistantTime;
      }
    }

    boolean hit = false;
    if(isProjectile && tool instanceof IProjectile) {
      hit = ((IProjectile) tool).dealDamageRanged(stack, projectileEntity, attacker, targetEntity, damage);
    }
    else {
      hit = tool.dealDamage(stack, attacker, targetEntity, damage);
    }

    // did we hit?
    if(hit && target != null) {
      // actual damage dealt
      float damageDealt = oldHP - target.getHealth();

      // apply knockback modifier
      oldVelX = target.motionX = oldVelX + (target.motionX - oldVelX) * tool.knockback();
      oldVelY = target.motionY = oldVelY + (target.motionY - oldVelY) * tool.knockback() / 3f;
      oldVelZ = target.motionZ = oldVelZ + (target.motionZ - oldVelZ) * tool.knockback();

      // apply knockback
      if(knockback > 0f) {
        double velX = -MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F;
        double velZ = MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F;
        targetEntity.addVelocity(velX, 0.1d, velZ);

        // slow down player
        attacker.motionX *= 0.6f;
        attacker.motionZ *= 0.6f;
        attacker.setSprinting(false);
      }

      // Send movement changes caused by attacking directly to hit players.
      // I guess this is to allow better handling at the hit players side? No idea why it resets the motion though.
      if(targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
        TinkerNetwork.sendPacket(targetEntity, new SPacketEntityVelocity(targetEntity));
        targetEntity.velocityChanged = false;
        targetEntity.motionX = oldVelX;
        targetEntity.motionY = oldVelY;
        targetEntity.motionZ = oldVelZ;
      }

      if(player != null) {
        // vanilla critical callback
        if(isCritical) {
          player.onCriticalHit(target);
          sound = SoundEvents.ENTITY_PLAYER_ATTACK_CRIT;
        }

        // "magical" critical damage? (aka caused by modifiers)
        if(damage > baseDamage) {
          // this usually only displays some particles :)
          player.onEnchantmentCritical(targetEntity);
        }

        // vanilla achievement support :D
        // TODO: READD
        //if(damage >= 18f) {
        //  player.addStat(AchievementList.OVERKILL);
        //}
      }

      attacker.setLastAttackedEntity(target);
      // Damage indicator particles

      // we don't support vanilla thorns or antispider enchantments
      //EnchantmentHelper.applyThornEnchantments(target, player);
      //EnchantmentHelper.applyArthropodEnchantments(player, target);

      // call post-hit callbacks before reducing the durability
      for(ITrait trait : traits) {
        trait.afterHit(stack, attacker, target, damageDealt, isCritical, true); // hit is always true
      }

      // damage the tool
      if(player != null) {
        stack.hitEntity(target, player);
        if(!player.capabilities.isCreativeMode && !isProjectile) {
          tool.reduceDurabilityOnHit(stack, player, damage);
        }

        player.addStat(StatList.DAMAGE_DEALT, Math.round(damageDealt * 10f));
        player.addExhaustion(0.3f);

        if(player.getEntityWorld() instanceof WorldServer && damageDealt > 2f) {
          int k = (int) (damageDealt * 0.5);
          ((WorldServer) player.getEntityWorld()).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + targetEntity.height * 0.5F, targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
        }

        // cooldown for non-projectiles
        if(!isProjectile && applyCooldown) {
          player.resetCooldown();
        }
      }
      else if(!isProjectile) {
        tool.reduceDurabilityOnHit(stack, null, damage);
      }
    } else {
      sound = SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE;
    }

    if (player != null && sound != null) {
      player.world.playSound(null, player.posX, player.posY, player.posZ, sound, player.getSoundCategory(), 1.0F, 1.0F);
    }

    return true;
  }

  public static float calcCutoffDamage(float damage, float cutoff) {
    float p = 1f;
    float d = damage;
    damage = 0f;
    while(d > cutoff) {
      damage += p * cutoff;
      // safety for ridiculous values
      if(p > 0.001f) {
        p *= 0.9f;
      }
      else {
        damage += p * cutoff * ((d / cutoff) - 1f);
        return damage;
      }
      d -= cutoff;
    }

    damage += p * d;

    return damage;
  }

  public static float getActualDamage(ItemStack stack, EntityLivingBase player) {
    float damage = (float) SharedMonsterAttributes.ATTACK_DAMAGE.getDefaultValue();
    if (player != null) {
      damage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
    }

    damage += ToolStatsHelper.getActualAttack(stack);

    if(stack.getItem() instanceof ToolCore) {
      damage = calcCutoffDamage(damage, ((ToolCore) stack.getItem()).damageCutoff());
    }

    return damage;
  }

  public static void swingItem(int speed, EntityLivingBase entity) {
    if(!entity.isSwingInProgress || entity.swingProgressInt >= 3 || entity.swingProgressInt < 0) {
      entity.swingProgressInt = Math.min(4, -1 + speed);
      entity.isSwingInProgress = true;

      if(entity.getEntityWorld() instanceof WorldServer) {
        ((WorldServer) entity.getEntityWorld()).getEntityTracker().sendToTracking(entity, new SPacketAnimation(entity, 0));
      }
    }
  }

  public static ItemStack playerIsHoldingItemWith(EntityPlayer player, Predicate<ItemStack> predicate) {
    ItemStack tool = player.getHeldItemMainhand();
    if(!predicate.test(tool)) {
      tool = player.getHeldItemOffhand();
      if(!predicate.test(tool)) {
        return ItemStack.EMPTY;
      }
    }
    return tool;
  }

}
