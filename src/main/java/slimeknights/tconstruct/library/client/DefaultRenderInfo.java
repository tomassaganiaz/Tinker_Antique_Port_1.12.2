package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.TinkerTexture;

/**
 * Does not actually generate a new texture. Used for vertex-coloring in the model generation.
 * Saves VRAM, so we use vertex colors instead of creating new data.
 */
public class DefaultRenderInfo extends AbstractMaterialRenderInfo {

  public final int color;

  public DefaultRenderInfo(int color) {
    this.color = color;
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    return TinkerTexture.loadManually(baseTexture);
  }

  @Override
  public boolean isStitched() {
    return false;
  }

  @Override
  public boolean useVertexColoring() {
    return true;
  }

  @Override
  public int getVertexColor() {
    return color;
  }
}
