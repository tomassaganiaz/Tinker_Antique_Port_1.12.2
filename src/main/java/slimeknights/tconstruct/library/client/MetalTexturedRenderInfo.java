package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.MetalTextureTexture;

/** Metallic texture generated from an additional base texture. */
public class MetalTexturedRenderInfo extends MetalRenderInfo {

  protected ResourceLocation extraTexture;

  public MetalTexturedRenderInfo(ResourceLocation extraTexture, int color, float shinyness, float brightness, float hueshift) {
    super(color, shinyness, brightness, hueshift);
    this.extraTexture = extraTexture;
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    return new MetalTextureTexture(extraTexture, baseTexture, location, color, shinyness, brightness, hueshift);
  }
}
