package slimeknights.tconstruct.tactical.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTacticalCrystal extends Item {
    public static final String TAG_MODIFIER="Modifier", TAG_LEVEL="Level", TAG_COLOR="Color", TAG_VALUE="Value", TAG_MAX="MaxValue";
    public ItemTacticalCrystal(){
        setCreativeTab(TinkerRegistry.tabGeneral);
        setMaxStackSize(64);
    }
    public static ItemStack withModifier(String mod,int lvl,int color){
        ItemStack s=new ItemStack(slimeknights.tconstruct.tactical.TinkerTactical.tacticalCrystal);
        NBTTagCompound t=new NBTTagCompound(); t.setString(TAG_MODIFIER, mod); t.setInteger(TAG_LEVEL, Math.max(1,lvl)); t.setInteger(TAG_COLOR, color); t.setInteger(TAG_VALUE,1); s.setTagCompound(t); return s;
    }
    public static String getModifier(ItemStack s){ return s.hasTagCompound()? s.getTagCompound().getString(TAG_MODIFIER):"";}    
    public static int getColor(ItemStack s){ return s.hasTagCompound() && s.getTagCompound().hasKey(TAG_COLOR)? s.getTagCompound().getInteger(TAG_COLOR):0xFFFFFF;}
    @Override public String getItemStackDisplayName(ItemStack s){
        String base=super.getItemStackDisplayName(s);
        if(!s.hasTagCompound()) return base;
        String id=s.getTagCompound().getString(TAG_MODIFIER);
        IModifier m=TinkerRegistry.getModifier(id);
        String name=m!=null?m.getLocalizedName():id;
        return name + " Crystal";
    }
    @Override @SideOnly(Side.CLIENT) public void addInformation(ItemStack s,@Nullable World w,List<String> tt,ITooltipFlag f){
        if(!s.hasTagCompound()) return;
        NBTTagCompound t=s.getTagCompound();
        tt.add(TextFormatting.DARK_GRAY + t.getString(TAG_MODIFIER));
        if(t.getInteger(TAG_LEVEL)>1) tt.add(TextFormatting.GRAY+"Level: "+t.getInteger(TAG_LEVEL));
    }
}
