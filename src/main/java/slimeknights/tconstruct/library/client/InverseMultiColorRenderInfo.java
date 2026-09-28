package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.InverseColoredTexture;

/** Colors the texture of the tool with inverted material colors. */
public class InverseMultiColorRenderInfo extends MultiColorRenderInfo {

  public InverseMultiColorRenderInfo(int low, int mid, int high) {
    super(low, mid, high);
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    return new InverseColoredTexture(low, mid, high, baseTexture, location);
  }
}
