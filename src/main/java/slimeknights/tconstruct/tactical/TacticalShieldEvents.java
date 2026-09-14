package slimeknights.tconstruct.tactical;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.tactical.tools.HeavyShield;
import slimeknights.tconstruct.tactical.tools.SwiftShield;

public class TacticalShieldEvents {
    @SubscribeEvent
    public void onHurt(LivingHurtEvent e){
        if(!(e.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p = (EntityPlayer)e.getEntityLiving();
        if(!p.isHandActive()) return;
        ItemStack s = p.getActiveItemStack();
        if(s.isEmpty()) return;
        float red = 0;
        if(s.getItem() instanceof SwiftShield) red = (float)TacticalConfig.swiftShieldReduction;
        else if(s.getItem() instanceof HeavyShield) red = (float)TacticalConfig.heavyShieldReduction;
        else return;
        if(p.getActiveItemStack().getItem().getItemUseAction(s) == net.minecraft.item.EnumAction.BLOCK){
            e.setAmount(e.getAmount() * (1f - red * 0.5f));
        }
    }
}
