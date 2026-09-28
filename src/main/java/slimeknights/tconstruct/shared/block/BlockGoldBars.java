package slimeknights.tconstruct.shared.block;

import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Barra de oro: decoración de panes de metal. Porte de TC3 (gold_bars). */
public class BlockGoldBars extends BlockPane {

  public BlockGoldBars() {
    super(Material.IRON, false);
    setHardness(3.0F);
    setResistance(100.0F);
    setSoundType(SoundType.METAL);
    setHarvestLevel("pickaxe", 1);
    setCreativeTab(TinkerRegistry.tabGeneral);
  }
}