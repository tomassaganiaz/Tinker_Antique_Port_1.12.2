package slimeknights.tconstruct.tactical.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tactical.TinkerTactical;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TacticalScoutArmorEvents {
    private static final Map<UUID,Integer> jumps=new HashMap<>();
    @SubscribeEvent public void onHurt(LivingHurtEvent e){
        if(!(e.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p=(EntityPlayer)e.getEntityLiving();
        int pieces=count(p);
        if(pieces==0) return;
        float dodge = pieces*0.07f;
        if(p.getRNG().nextFloat() < Math.min(dodge,0.25f)){
            e.setCanceled(true);
            p.world.playSound(null,p.posX,p.posY,p.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, p.getSoundCategory(),0.8f,1f);
            return;
        }
        int plate=countPlate(p);
        if(plate>0) e.setAmount(e.getAmount() * (1f - Math.min(plate*0.10f, 0.4f)));
    }
    @SubscribeEvent public void onFall(LivingFallEvent e){
        if(!(e.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p=(EntityPlayer)e.getEntityLiving();
        int pieces=count(p);
        if(pieces>0) e.setDamageMultiplier(e.getDamageMultiplier() * (1f - Math.min(pieces*0.15f,0.6f)));
        int slimePieces=countSlime(p);
        if(slimePieces>=2 && e.getDistance()>2){
            p.motionY = Math.min(0.5, e.getDistance()*0.05);
            p.velocityChanged=true; p.fallDistance=0;
        }
    }
    @SubscribeEvent public void onTick(TickEvent.PlayerTickEvent e){
        if(e.phase==TickEvent.Phase.END){
            int pieces=count(e.player);
            e.player.stepHeight = pieces>=3? 1.5f:0.6f;
            if(e.player.onGround) jumps.remove(e.player.getUniqueID());
            int scout=countScout(e.player);
            if(scout>=2 && !e.player.isPotionActive(MobEffects.SPEED) && e.player.ticksExisted%40==0){
                e.player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 80, scout>=4?1:0, true, false));
            }
            if(hasSlimeWings(e.player) && !e.player.onGround && e.player.motionY < -0.3 && e.player.isSneaking()){
                e.player.motionY *= 0.6; e.player.fallDistance=0; e.player.velocityChanged=true;
                if(e.player.ticksExisted%10==0) e.player.playSound(net.minecraft.init.SoundEvents.ENTITY_SLIME_JUMP, 0.4f, 1.2f);
            }
        }
    }
    public boolean handleJump(EntityPlayerMP p){ return handleJumpStatic(p); }
    public static boolean handleJumpStatic(EntityPlayerMP p){
        UUID id=p.getUniqueID();
        int used=jumps.getOrDefault(id,0);
        int max = count(p)>=4?2:count(p)>=2?1:0;
        if(used>=max || p.onGround || p.capabilities.isFlying) return false;
        jumps.put(id, used+1);
        p.motionY=0.42; if(p.isPotionActive(MobEffects.JUMP_BOOST)) p.motionY+= (p.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier()+1)*0.1;
        if(p.isSprinting()){ float y=p.rotationYaw*0.017453292F; p.motionX-=MathHelper.sin(y)*0.2; p.motionZ+=MathHelper.cos(y)*0.2; }
        p.isAirBorne=true; p.fallDistance=0; p.velocityChanged=true; return true;
    }
    private static int count(EntityPlayer p){
        int c=0; for(ItemStack s: p.getArmorInventoryList()) if(!s.isEmpty() && !ToolHelper.isBroken(s) && s.getItem() instanceof slimeknights.tconstruct.tools.armor.item.ArmorCore && s.getItem().getRegistryName()!=null && s.getItem().getRegistryName().getResourcePath().startsWith("tactical_")) c++; return c;
    }
    private static int countSlime(EntityPlayer p){
        int c=0; for(ItemStack s: p.getArmorInventoryList()) if(!s.isEmpty() && s.getItem().getRegistryName()!=null && s.getItem().getRegistryName().getResourcePath().contains("slime")) c++; return c;
    }
    private static int countPlate(EntityPlayer p){
        int c=0; for(ItemStack s: p.getArmorInventoryList()) if(!s.isEmpty() && s.getItem().getRegistryName()!=null && s.getItem().getRegistryName().getResourcePath().contains("plate")) c++; return c;
    }
    private static int countScout(EntityPlayer p){
        int c=0; for(ItemStack s: p.getArmorInventoryList()) if(!s.isEmpty() && s.getItem().getRegistryName()!=null && s.getItem().getRegistryName().getResourcePath().contains("scout")) c++; return c;
    }
    private static boolean hasSlimeWings(EntityPlayer p){
        for(ItemStack s: p.getArmorInventoryList()) if(!s.isEmpty() && s.getItem().getRegistryName()!=null && s.getItem().getRegistryName().getResourcePath().equals("tactical_slime_chestplate")) return true;
        return false;
    }
}
