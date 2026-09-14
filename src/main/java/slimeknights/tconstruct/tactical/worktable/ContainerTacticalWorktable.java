package slimeknights.tconstruct.tactical.worktable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tactical.item.ItemTacticalCrystal;

public class ContainerTacticalWorktable extends Container {
    private final TileTacticalWorktable tile;
    public ContainerTacticalWorktable(InventoryPlayer inv, TileTacticalWorktable t){
        this.tile=t;
        addSlotToContainer(new SlotTacticalTool(t, 0, 20, 35));
        addSlotToContainer(new SlotTacticalCrystal(t, 1, 60, 35));
        addSlotToContainer(new Slot(t, 2, 120, 35){
            @Override public boolean isItemValid(ItemStack s){ return false; }
        });
        for(int r=0;r<3;r++) for(int c=0;c<9;c++) addSlotToContainer(new Slot(inv, c + r*9 + 9, 8 + c*18, 84 + r*18));
        for(int c=0;c<9;c++) addSlotToContainer(new Slot(inv, c, 8 + c*18, 142));
    }
    @Override public boolean canInteractWith(EntityPlayer p){ return true; }
    @Override public ItemStack transferStackInSlot(EntityPlayer p,int idx){
        ItemStack s=ItemStack.EMPTY; Slot slot=inventorySlots.get(idx);
        if(slot!=null && slot.getHasStack()){
            ItemStack a=slot.getStack(); s=a.copy();
            if(idx<3){ if(!mergeItemStack(a,3,inventorySlots.size(),true)) return ItemStack.EMPTY; }
            else { if(a.getItem() instanceof ToolCore){ if(!mergeItemStack(a,0,1,false)) return ItemStack.EMPTY; }
            else if(a.getItem() instanceof ItemTacticalCrystal){ if(!mergeItemStack(a,1,2,false)) return ItemStack.EMPTY; }
            else return ItemStack.EMPTY; }
            if(a.isEmpty()) slot.putStack(ItemStack.EMPTY); else slot.onSlotChanged();
        }
        return s;
    }
    @Override public void detectAndSendChanges(){
        super.detectAndSendChanges();
        ItemStack tool=tile.getStackInSlot(0);
        ItemStack crystal=tile.getStackInSlot(1);
        ItemStack out=ItemStack.EMPTY;
        if(!tool.isEmpty() && !crystal.isEmpty() && tool.getItem() instanceof ToolCore && crystal.getItem() instanceof ItemTacticalCrystal){
            String mod=ItemTacticalCrystal.getModifier(crystal);
            IModifier m=TinkerRegistry.getModifier(mod);
            if(m!=null){
                out=tool.copy();
                try{ m.apply(out); } catch(Exception e){ out=ItemStack.EMPTY; }
            }
        }
        tile.setStackInSlot(2, out);
    }
    static class SlotTacticalTool extends Slot {
        SlotTacticalTool(TileTacticalWorktable t,int i,int x,int y){ super(t,i,x,y); }
        @Override public boolean isItemValid(ItemStack s){ return s.getItem() instanceof ToolCore; }
        @Override public int getSlotStackLimit(){ return 1; }
    }
    static class SlotTacticalCrystal extends Slot {
        SlotTacticalCrystal(TileTacticalWorktable t,int i,int x,int y){ super(t,i,x,y); }
        @Override public boolean isItemValid(ItemStack s){ return s.getItem() instanceof ItemTacticalCrystal; }
    }
}
