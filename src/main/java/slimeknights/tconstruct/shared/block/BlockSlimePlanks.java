package slimeknights.tconstruct.shared.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Tablón de madera de slime: por cada tipo de slime. */
public class BlockSlimePlanks extends EnumBlock<BlockSlimePlanks.SlimePlankType> {

  public static final PropertyEnum<SlimePlankType> TYPE = PropertyEnum.create("type", SlimePlankType.class);

  public BlockSlimePlanks() {
    super(Material.WOOD, TYPE, SlimePlankType.class);

    this.setHardness(2.0f);
    this.setResistance(7.0f);
    this.setCreativeTab(TinkerRegistry.tabGeneral);
    this.setSoundType(SoundType.WOOD);
    this.setHarvestLevel("axe", 0);
  }

  public enum SlimePlankType implements IStringSerializable, EnumBlock.IEnumMeta {
    GREEN,
    BLUE,
    PURPLE,
    MAGMA,
    BLOOD;

    public final int meta;

    SlimePlankType() {
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