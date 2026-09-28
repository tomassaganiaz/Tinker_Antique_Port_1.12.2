package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

/** Base implementation for material render info that generates textures. */
public abstract class AbstractMaterialRenderInfo implements MaterialRenderInfo {

  private String suffix;

  @Override
  public boolean isStitched() {
    return true;
  }

  @Override
  public boolean useVertexColoring() {
    return false;
  }

  @Override
  public int getVertexColor() {
    return 0xffffffff; // white and opaque
  }

  @Override
  public String getTextureSuffix() {
    return suffix;
  }

  @Override
  public MaterialRenderInfo setTextureSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  @Override
  public abstract TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location);
}
