package slimeknights.tconstruct.tactical.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTacticalExpBottle extends Item {
    public static final String TAG_XP="Experience";
    public ItemTacticalExpBottle(){
        setCreativeTab(TinkerRegistry.tabGeneral);
        setMaxStackSize(16);
    }
    public static ItemStack withXp(int xp){
        ItemStack s=new ItemStack(slimeknights.tconstruct.tactical.TinkerTactical.tacticalExpBottle);
        NBTTagCompound t=new NBTTagCompound(); t.setInteger(TAG_XP, Math.max(0,xp)); s.setTagCompound(t); return s;
    }
    public static int getXp(ItemStack s){ return s.hasTagCompound()? Math.max(0,s.getTagCompound().getInteger(TAG_XP)):0;}
    @Override @SideOnly(Side.CLIENT) public void addInformation(ItemStack s,@Nullable World w,List<String> tt,ITooltipFlag f){ tt.add(TextFormatting.GRAY.toString() + getXp(s) + " XP");}
    @Override public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h){
        ItemStack s=p.getHeldItem(h);
        if(!w.isRemote){
            int xp=getXp(s);
            if(xp>0){ p.addExperience(xp); s.shrink(1);}
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, p.getHeldItem(h));
    }
}
