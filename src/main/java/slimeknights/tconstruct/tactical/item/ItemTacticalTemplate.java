package slimeknights.tconstruct.tactical.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTacticalTemplate extends Item {
    private final String key;
    private final TextFormatting color;
    public ItemTacticalTemplate(String variant, TextFormatting c){
        this.key = "tooltip.tactical.template." + variant;
        this.color = c;
        setCreativeTab(TinkerRegistry.tabGeneral);
        setMaxStackSize(64);
    }
    @Override @SideOnly(Side.CLIENT) public void addInformation(ItemStack s,@Nullable World w,List<String> tt,ITooltipFlag f){ tt.add(color + key);}
}
