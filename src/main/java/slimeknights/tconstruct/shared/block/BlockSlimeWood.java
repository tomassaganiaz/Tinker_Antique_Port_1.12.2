package slimeknights.tconstruct.shared.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Tronco de madera de slime: por cada tipo de slime (green/blue/purple/magma/blood). */
public class BlockSlimeWood extends EnumBlock<BlockSlimeWood.SlimeWoodType> {

  public static final PropertyEnum<SlimeWoodType> TYPE = PropertyEnum.create("type", SlimeWoodType.class);

  public BlockSlimeWood() {
    super(Material.WOOD, TYPE, SlimeWoodType.class);

    this.setHardness(2.0f);
    this.setResistance(7.0f);
    this.setCreativeTab(TinkerRegistry.tabGeneral);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);
  }

  public enum SlimeWoodType implements IStringSerializable, EnumBlock.IEnumMeta {
    GREEN,
    BLUE,
    PURPLE,
    MAGMA,
    BLOOD;

    public final int meta;

    SlimeWoodType() {
      meta = ordinal();
    }

    @Override
    public String getName() {
      return this.toString().toLowerCase(Locale.US);
    }

    @Override
    public int getMeta() {
      return meta;
    }
  }
}