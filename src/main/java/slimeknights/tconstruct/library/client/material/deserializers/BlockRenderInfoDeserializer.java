package slimeknights.tconstruct.library.client.material.deserializers;

import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.BlockTextureRenderInfo;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class BlockRenderInfoDeserializer extends AbstractRenderInfoDeserializer {

  protected String texture;

  @Override
  public MaterialRenderInfo getMaterialRenderInfo() {
    return new BlockTextureRenderInfo(new ResourceLocation(texture));
  }
}
