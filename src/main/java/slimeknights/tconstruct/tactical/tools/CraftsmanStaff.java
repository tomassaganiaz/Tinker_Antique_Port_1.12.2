package slimeknights.tconstruct.tactical.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class CraftsmanStaff extends TinkerToolCore {
    public CraftsmanStaff(){ super(PartMaterialType.head(TinkerTools.shard), PartMaterialType.handle(TinkerTools.toughToolRod), PartMaterialType.handle(TinkerTools.toolRod)); addCategory(Category.WEAPON);}
    @Override public float damagePotential(){return 0.35f;}
    @Override public double attackSpeed(){return 1.2d;}
    @Override protected ToolNBT buildTagData(List<Material> m){ HeadMaterialStats h=m.get(0).getStatsOrUnknown(MaterialTypes.HEAD); HandleMaterialStats th=m.get(1).getStatsOrUnknown(MaterialTypes.HANDLE); HandleMaterialStats ha=m.get(2).getStatsOrUnknown(MaterialTypes.HANDLE); ToolNBT d=new ToolNBT(); d.head(h); d.handle(th,ha); d.attack+=1.5f; return d;}
    @Override public int[] getRepairParts(){return new int[]{0};}
    @Override public Set<String> getToolClasses(ItemStack s){ Set<String> c=new HashSet<>(super.getToolClasses(s)); c.add("pickaxe"); c.add("axe"); c.add("shovel"); c.add("sword"); return c;}
    @Override public boolean canHarvestBlock(IBlockState st, ItemStack s){ return !ToolHelper.isBroken(s); }
}
