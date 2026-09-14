package slimeknights.tconstruct.tactical.worktable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TileTacticalWorktable extends TileEntity implements ITickable, net.minecraft.inventory.IInventory {
    public static final int SLOT_TOOL=0, SLOT_CRYSTAL=1, SLOT_OUTPUT=2, SIZE=3;
    private NonNullList<ItemStack> inv = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    @Override public int getSizeInventory(){ return SIZE;}
    @Override public boolean isEmpty(){ for(ItemStack s:inv) if(!s.isEmpty()) return false; return true;}
    @Override public ItemStack getStackInSlot(int i){ return inv.get(i);}
    @Override public ItemStack decrStackSize(int i,int c){ ItemStack s=inv.get(i); if(s.isEmpty()) return ItemStack.EMPTY; if(s.getCount()<=c){ inv.set(i, ItemStack.EMPTY); markDirty(); return s; } ItemStack r=s.splitStack(c); if(s.getCount()==0) inv.set(i, ItemStack.EMPTY); markDirty(); return r;}
    @Override public ItemStack removeStackFromSlot(int i){ ItemStack s=inv.get(i); inv.set(i, ItemStack.EMPTY); return s;}
    @Override public void setInventorySlotContents(int i, ItemStack s){ inv.set(i,s); if(s.getCount()>getInventoryStackLimit()) s.setCount(getInventoryStackLimit()); markDirty();}
    public void setStackInSlot(int i, ItemStack s){ setInventorySlotContents(i,s);}
    @Override public int getInventoryStackLimit(){ return 64;}
    @Override public boolean isUsableByPlayer(net.minecraft.entity.player.EntityPlayer p){ return !isInvalid() && p.getDistanceSq(pos) <= 64;}
    @Override public void openInventory(net.minecraft.entity.player.EntityPlayer p){}
    @Override public void closeInventory(net.minecraft.entity.player.EntityPlayer p){}
    @Override public boolean isItemValidForSlot(int i, ItemStack s){ return true;}
    @Override public int getField(int id){ return 0;}
    @Override public void setField(int id,int v){}
    @Override public int getFieldCount(){ return 0;}
    @Override public void clear(){ inv.clear();}
    @Override public String getName(){ return "container.tactical_worktable";}
    @Override public boolean hasCustomName(){ return false;}
    @Override public net.minecraft.util.text.ITextComponent getDisplayName(){ return new net.minecraft.util.text.TextComponentString(getName());}
    @Override public void update(){}
    @Override public NBTTagCompound writeToNBT(NBTTagCompound n){ super.writeToNBT(n); net.minecraft.inventory.ItemStackHelper.saveAllItems(n, inv); return n;}
    @Override public void readFromNBT(NBTTagCompound n){ super.readFromNBT(n); inv = NonNullList.withSize(SIZE, ItemStack.EMPTY); net.minecraft.inventory.ItemStackHelper.loadAllItems(n, inv);}
}
