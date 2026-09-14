package slimeknights.tconstruct.tactical.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tactical.TinkerTactical;

public class TacticalLevelingEvents {
    @SubscribeEvent public void onKill(LivingDeathEvent e){
        if(!(e.getSource().getTrueSource() instanceof EntityPlayer)) return;
        EntityPlayer p=(EntityPlayer)e.getSource().getTrueSource();
        ItemStack tool=p.getHeldItemMainhand();
        if(tool.isEmpty() || ToolHelper.isBroken(tool)) return;
        if(tool.getItem().getRegistryName()==null || !tool.getItem().getRegistryName().getResourcePath().startsWith("tactical_")) return;
        if(p.world.rand.nextFloat()<0.35f){
            ToolHelper.healTool(tool, 5, p);
        }
        if(e.getEntityLiving() instanceof EntityLivingBase){
            p.addExperience(2);
        }
    }
}
