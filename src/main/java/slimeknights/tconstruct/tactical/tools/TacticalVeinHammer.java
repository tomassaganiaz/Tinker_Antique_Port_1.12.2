package slimeknights.tconstruct.tactical.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.tools.Hammer;
import java.util.List;

public class TacticalVeinHammer extends Hammer {
    public TacticalVeinHammer(){
        super();
        addCategory(Category.WEAPON);
    }
    @Override public ToolNBT buildTagData(List<Material> mats){
        ToolNBT t=super.buildTagData(mats);
        t.durability = (int)(t.durability * 1.2f);
        return t;
    }
    @Override public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, net.minecraft.entity.EntityLivingBase entity){
        boolean r=super.onBlockDestroyed(stack, world, state, pos, entity);
        if(!world.isRemote && entity instanceof EntityPlayer && state.getBlock().getHarvestLevel(state) >=0){
            java.util.Set<BlockPos> visited=new java.util.HashSet<>();
            java.util.Queue<BlockPos> q=new java.util.LinkedList<>();
            q.add(pos); visited.add(pos);
            int count=0;
            while(!q.isEmpty() && count<12){
                BlockPos p=q.poll();
                for(net.minecraft.util.EnumFacing f: net.minecraft.util.EnumFacing.VALUES){
                    BlockPos n=p.offset(f);
                    if(visited.contains(n)) continue;
                    IBlockState ns=world.getBlockState(n);
                    if(ns.getBlock()==state.getBlock()){
                        visited.add(n);
                        q.add(n);
                        if(!n.equals(pos)){
                            world.destroyBlock(n, true);
                            count++;
                            if(count>=12) break;
                        }
                    }
                }
            }
        }
        return r;
    }
}
