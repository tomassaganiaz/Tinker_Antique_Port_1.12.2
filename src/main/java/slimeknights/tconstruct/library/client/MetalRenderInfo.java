package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.MetalColoredTexture;

/** Metallic texture with configurable shinyness, brightness and hue shift. */
public class MetalRenderInfo extends AbstractMaterialRenderInfo {

  protected float shinyness;
  protected float brightness;
  protected float hueshift;
  public int color;

  public MetalRenderInfo(int color, float shinyness, float brightness, float hueshift) {
    this.color = color;
    this.shinyness = shinyness;
    this.brightness = brightness;
    this.hueshift = hueshift;
  }

  public MetalRenderInfo(int color) {
    this(color, 0.4f, 0.4f, 0.1f);
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    return new MetalColoredTexture(baseTexture, location, color, shinyness, brightness, hueshift);
  }
}
