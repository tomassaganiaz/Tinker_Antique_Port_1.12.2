package slimeknights.tconstruct.tactical.worktable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import slimeknights.tconstruct.TConstruct;

public class TacticalGuiHandler extends slimeknights.mantle.common.GuiHandler {
    public static final int ID_WORKTABLE = 77;
    private final net.minecraftforge.fml.common.network.IGuiHandler parent;
    public TacticalGuiHandler(net.minecraftforge.fml.common.network.IGuiHandler p){ this.parent=p; }
    public TacticalGuiHandler(){ this.parent=TConstruct.guiHandler; }
    @Override public Object getServerGuiElement(int id, EntityPlayer p, World w, int x,int y,int z){
        if(id==ID_WORKTABLE){
            TileTacticalWorktable t=(TileTacticalWorktable)w.getTileEntity(new BlockPos(x,y,z));
            if(t!=null) return new ContainerTacticalWorktable(p.inventory, t);
        }
        return parent!=null? parent.getServerGuiElement(id,p,w,x,y,z):null;
    }
    @Override public Object getClientGuiElement(int id, EntityPlayer p, World w, int x,int y,int z){
        if(id==ID_WORKTABLE){
            TileTacticalWorktable t=(TileTacticalWorktable)w.getTileEntity(new BlockPos(x,y,z));
            if(t!=null) return new GuiTacticalWorktable(new ContainerTacticalWorktable(p.inventory, t));
        }
        return parent!=null? parent.getClientGuiElement(id,p,w,x,y,z):null;
    }
    public static void register(){
        net.minecraftforge.fml.common.network.IGuiHandler orig=TConstruct.guiHandler;
        TacticalGuiHandler combined=new TacticalGuiHandler(orig);
        net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler(TConstruct.instance, combined);
        TConstruct.guiHandler = combined;
    }
}
