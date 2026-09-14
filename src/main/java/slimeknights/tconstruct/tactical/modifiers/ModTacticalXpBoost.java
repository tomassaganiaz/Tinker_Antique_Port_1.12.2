package slimeknights.tconstruct.tactical.modifiers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModTacticalXpBoost extends ToolModifier {
    public ModTacticalXpBoost(){ super("tactical_xp_boost", 0x2d8a4e);}
    @Override public void applyEffect(NBTTagCompound root, NBTTagCompound modTag){
        NBTTagCompound tag = TagUtil.getExtraTag(root);
        tag.setInteger("tacticalXp", modTag.getInteger("level"));
        TagUtil.setExtraTag(root, tag);
    }
    public void onHitBonus(ItemStack tool, EntityLivingBase player, EntityLivingBase target){}
}
