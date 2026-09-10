package slimeknights.tconstruct.foundry.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

import slimeknights.mantle.block.EnumBlock;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.smeltery.block.BlockEnumSmeltery;

/** Scorched structure brick used to build the foundry. */
public class BlockScorched extends BlockEnumSmeltery<BlockScorched.ScorchedType> {

  public final static PropertyEnum<ScorchedType> TYPE = PropertyEnum.create("type", ScorchedType.class);

  public BlockScorched() {
    super(Material.ROCK, TYPE, ScorchedType.class);
    this.setCreativeTab(TinkerRegistry.tabSmeltery);
    this.setHardness(3F);
    this.setResistance(20F);
    this.setSoundType(SoundType.METAL);
  }

  public enum ScorchedType implements IStringSerializable, EnumBlock.IEnumMeta {
    STONE,
    BRICK,
    BRICK_CRACKED,
    BRICK_FANCY,
    BRICK_SQUARE,
    ROAD;

    public final int meta;

    ScorchedType() {
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