package slimeknights.tconstruct.library.client.material.deserializers;

import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.MetalTexturedRenderInfo;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class TexturedMetalRenderInfoDeserializer extends MetalRenderInfoDeserializer {

  protected String texture;

  @Override
  public MaterialRenderInfo getMaterialRenderInfo() {
    return new MetalTexturedRenderInfo(new ResourceLocation(texture), fromHex(color), shinyness, brightness, hueshift);
  }
}
