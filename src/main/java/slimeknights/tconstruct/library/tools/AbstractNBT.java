package slimeknights.tconstruct.library.tools;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.utils.Tags;

public abstract class AbstractNBT {
  public int durability;
  public int modifiers;
  public int upgrades;
  public int abilities;

  protected final NBTTagCompound parent;

  public AbstractNBT() {
    durability = 0;
    modifiers = 3;
    upgrades = 2;
    abilities = 1;
    parent = new NBTTagCompound();
  }

  public AbstractNBT(NBTTagCompound tag) {
    read(tag);
    parent = tag;
  }

  public void read(NBTTagCompound tag) {
    durability = tag.getInteger(Tags.DURABILITY);
    modifiers = tag.getInteger(Tags.FREE_MODIFIERS);
    upgrades = tag.hasKey(Tags.FREE_UPGRADES) ? tag.getInteger(Tags.FREE_UPGRADES) : 2;
    abilities = tag.hasKey(Tags.FREE_ABILITIES) ? tag.getInteger(Tags.FREE_ABILITIES) : 1;
  }

  public void write(NBTTagCompound tag) {
    tag.setInteger(Tags.DURABILITY, durability);
    tag.setInteger(Tags.FREE_MODIFIERS, modifiers);
    tag.setInteger(Tags.FREE_UPGRADES, upgrades);
    tag.setInteger(Tags.FREE_ABILITIES, abilities);
  }

  public NBTTagCompound get() {
    NBTTagCompound tag = parent.copy();
    write(tag);
    return tag;
  }
}
