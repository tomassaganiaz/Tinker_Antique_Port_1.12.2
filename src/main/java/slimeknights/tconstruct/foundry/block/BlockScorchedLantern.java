package slimeknights.tconstruct.foundry.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import slimeknights.tconstruct.library.TinkerRegistry;

public class BlockScorchedLantern extends Block {

  public BlockScorchedLantern() {
    super(Material.ROCK);
    setCreativeTab(TinkerRegistry.tabSmeltery);
    setHardness(3F);
    setResistance(20F);
    setSoundType(SoundType.METAL);
    setLightLevel(1.0F);
  }
}
