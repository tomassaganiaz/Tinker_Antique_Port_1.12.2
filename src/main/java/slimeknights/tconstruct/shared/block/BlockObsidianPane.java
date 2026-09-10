package slimeknights.tconstruct.shared.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockObsidianPane extends Block {
  public BlockObsidianPane() {
    super(Material.ROCK);
    setHardness(3.0F);
    setResistance(100.0F);
    setSoundType(SoundType.STONE);
    setHarvestLevel("pickaxe", 2);
  }
}
