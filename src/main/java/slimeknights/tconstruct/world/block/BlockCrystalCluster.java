package slimeknights.tconstruct.world.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;

import java.util.Locale;

public class BlockCrystalCluster extends EnumBlock<BlockCrystalCluster.CrystalType> {

  public static final PropertyEnum<CrystalType> TYPE = PropertyEnum.create("type", CrystalType.class);

  public BlockCrystalCluster() {
    super(Material.ROCK, TYPE, CrystalType.class);
    this.setCreativeTab(TinkerRegistry.tabWorld);
    this.setHardness(1.5f);
    this.setResistance(10.0f);
    this.setSoundType(SoundType.GLASS);
    this.setLightLevel(0.6f);
  }

  public enum CrystalType implements IStringSerializable, EnumBlock.IEnumMeta {
    QUARTZ,
    AMETHYST,
    BLOOD,
    VENOM,
    ENDER;

    CrystalType() {
      this.meta = this.ordinal();
    }

    public final int meta;

    @Override
    public int getMeta() {
      return meta;
    }

    @Override
    public String getName() {
      return this.toString().toLowerCase(Locale.US);
    }
  }
}
