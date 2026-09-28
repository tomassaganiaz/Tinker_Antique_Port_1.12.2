package slimeknights.tconstruct.world.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerCommons;

import java.util.Locale;
import java.util.Random;

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

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    CrystalType type = state.getValue(TYPE);
    ItemStack crystal = crystalDrop(type);
    if(!crystal.isEmpty()) {
      int count = 1 + (fortune > 0 ? RANDOM.nextInt(fortune + 1) : 0);
      crystal.setCount(count);
      drops.add(crystal);
    }
  }

  /** Devuelve el cristal/slimeball que suelta cada geoda, o vacío si se deja caer el propio bloque. */
  public static ItemStack crystalDrop(CrystalType type) {
    switch(type) {
      case QUARTZ:
        return new ItemStack(net.minecraft.init.Items.QUARTZ);
      case AMETHYST:
        return TinkerCommons.matSlimeCrystalGreen != null ? TinkerCommons.matSlimeCrystalGreen.copy() : ItemStack.EMPTY;
      case EARTH:
        return new ItemStack(net.minecraft.init.Items.SLIME_BALL);
      case SKY:
        return TinkerCommons.matSlimeBallBlue != null ? TinkerCommons.matSlimeBallBlue.copy() : ItemStack.EMPTY;
      case ICHOR:
        return TinkerCommons.matSlimeBallMagma != null ? TinkerCommons.matSlimeBallMagma.copy() : ItemStack.EMPTY;
      case ENDER:
        return TinkerCommons.matSlimeBallPurple != null ? TinkerCommons.matSlimeBallPurple.copy() : ItemStack.EMPTY;
      case BLOOD:
        return TinkerCommons.matSlimeBallBlood != null ? TinkerCommons.matSlimeBallBlood.copy() : ItemStack.EMPTY;
      case VENOM:
        return new ItemStack(net.minecraft.init.Items.SLIME_BALL);
      default:
        return ItemStack.EMPTY;
    }
  }

  public enum CrystalType implements IStringSerializable, EnumBlock.IEnumMeta {
    QUARTZ,
    AMETHYST,
    BLOOD,
    VENOM,
    ENDER,
    // Geodas de slime de TC3 1.20.1 (earth/sky/ichor/ender) + knightmetal
    EARTH,
    SKY,
    ICHOR,
    KNIGHTMETAL;

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
