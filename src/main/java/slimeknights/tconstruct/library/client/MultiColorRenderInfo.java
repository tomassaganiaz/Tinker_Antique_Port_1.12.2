package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.SimpleColoredTexture;

/** Colors the texture of the tool with three material colors. */
public class MultiColorRenderInfo extends AbstractMaterialRenderInfo {

  // colors to be used
  protected final int low, mid, high;

  public MultiColorRenderInfo(int low, int mid, int high) {
    this.low = low;
    this.mid = mid;
    this.high = high;
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    return new SimpleColoredTexture(low, mid, high, baseTexture, location);
  }
}
