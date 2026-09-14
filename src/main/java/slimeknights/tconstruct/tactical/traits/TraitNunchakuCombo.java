package slimeknights.tconstruct.tactical.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class TraitNunchakuCombo extends AbstractTrait {
    public TraitNunchakuCombo() { super("tactical_combo", TextFormatting.GOLD); }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        NBTTagCompound tag = TagUtil.getExtraTag(tool);
        int combo = tag.getInteger("tac_combo");
        combo = Math.min(combo + 1, 5);
        tag.setInteger("tac_combo", combo);
        TagUtil.setExtraTag(tool, tag);
        return newDamage * (1.0f + combo * 0.08f);
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        if (target.getHealth() <= 0) {
            NBTTagCompound tag = TagUtil.getExtraTag(tool);
            tag.setInteger("tac_combo", 0);
            TagUtil.setExtraTag(tool, tag);
        }
    }
}
