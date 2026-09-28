package slimeknights.tconstruct.shared.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import slimeknights.tconstruct.library.TinkerRegistry;

/** Cristal del alma: vidrio resistente de la smeltery. Porte de TC3 (soul_glass). */
public class BlockSoulGlass extends BlockClearGlass {

  public BlockSoulGlass() {
    super();
    setHardness(0.5f);
    setResistance(15.0f);
    setCreativeTab(TinkerRegistry.tabGeneral);
  }
}