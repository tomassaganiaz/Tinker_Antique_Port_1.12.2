package slimeknights.tconstruct.tools.armor;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.tools.AbstractNBT;
import slimeknights.tconstruct.library.utils.Tags;

public class ArmorNBT extends AbstractNBT {

  public float defense;
  public float toughness;
  public float knockbackResistance;

  public ArmorNBT durability(int d) {
    this.durability = d;
    return this;
  }

  public ArmorNBT defense(float v) {
    this.defense = v;
    return this;
  }

  public ArmorNBT toughness(float v) {
    this.toughness = v;
    return this;
  }

  public ArmorNBT knockbackResistance(float v) {
    this.knockbackResistance = v;
    return this;
  }

  public ArmorNBT stats(ArmorMaterialStats stats) {
    this.durability = stats.durability;
    this.defense = stats.defense;
    this.toughness = stats.toughness;
    this.knockbackResistance = stats.knockbackResistance;
    return this;
  }

  public ArmorNBT maille(ArmorMaterialStats maille) {
    if(maille != null) {
      this.durability += maille.durability / 4;
      this.defense += maille.defense * 0.5f;
      this.toughness += maille.toughness * 0.5f;
      this.knockbackResistance += maille.knockbackResistance * 0.5f;
    }
    return this;
  }

  @Override
  public void read(NBTTagCompound tag) {
    super.read(tag);
    defense = tag.getFloat(Tags.DEFENSE);
    toughness = tag.getFloat(Tags.TOUGHNESS);
    knockbackResistance = tag.getFloat(Tags.KNOCKBACK_RESISTANCE);
  }

  @Override
  public void write(NBTTagCompound tag) {
    super.write(tag);
    tag.setFloat(Tags.DEFENSE, defense);
    tag.setFloat(Tags.TOUGHNESS, toughness);
    tag.setFloat(Tags.KNOCKBACK_RESISTANCE, knockbackResistance);
  }
}
