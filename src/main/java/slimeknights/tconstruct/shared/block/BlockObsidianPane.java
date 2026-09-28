package slimeknights.tconstruct.shared.block;

import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Panel de obsidiana: cristal resistente que se rompe con pico. Porte de TC3 (obsidian_pane). */
public class BlockObsidianPane extends BlockPane {

  public BlockObsidianPane() {
    super(Material.ROCK, false);
    setHardness(3.0F);
    setResistance(100.0F);
    setSoundType(SoundType.STONE);
    setHarvestLevel("pickaxe", 2);
    setCreativeTab(TinkerRegistry.tabGeneral);
  }
}