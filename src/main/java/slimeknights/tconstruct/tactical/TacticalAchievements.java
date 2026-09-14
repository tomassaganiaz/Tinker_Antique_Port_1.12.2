package slimeknights.tconstruct.tactical;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.tactical.tools.*;
import net.minecraft.util.ResourceLocation;

public class TacticalAchievements {
    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent e){
        if(e.player==null||e.player instanceof FakePlayer||!(e.player instanceof EntityPlayerMP)||e.crafting.isEmpty()) return;
        Item it=e.crafting.getItem();
        boolean isTac = it instanceof TinkerNunchaku || it instanceof SwiftShield || it instanceof HeavyShield || it instanceof Spear || it instanceof Doppelhander || it instanceof Maraca || it instanceof CraftsmanStaff;
        if(isTac){
            EntityPlayerMP mp=(EntityPlayerMP)e.player;
            grant(mp, "tconstruct:tactical/craft_tactical");
            grant(mp, "minecraft:story/root");
        }
    }
    private void grant(EntityPlayerMP p,String id){
        try{
            net.minecraft.advancements.Advancement a=p.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(id));
            if(a!=null){
                net.minecraft.advancements.AdvancementProgress pr=p.getAdvancements().getProgress(a);
                if(!pr.isDone()) pr.getRemaningCriteria().forEach(c->p.getAdvancements().grantCriterion(a,c));
            }
        }catch(Exception ex){}
    }
}
