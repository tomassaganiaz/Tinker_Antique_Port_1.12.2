package slimeknights.tconstruct.tactical.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.tools.LumberAxe;
import java.util.List;

public class TacticalBroadAxe extends LumberAxe {
    public TacticalBroadAxe(){ super(); addCategory(Category.WEAPON); }
    @Override public ToolNBT buildTagData(List<Material> mats){
        ToolNBT t=super.buildTagData(mats);
        t.attack += 1.0f;
        return t;
    }
    @Override public float damagePotential(){ return 1.15f; }
    @Override public double attackSpeed(){ return 0.9; }
}
