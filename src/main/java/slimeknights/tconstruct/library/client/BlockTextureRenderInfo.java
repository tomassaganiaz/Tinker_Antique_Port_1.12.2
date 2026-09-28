package slimeknights.tconstruct.library.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.TextureColoredTexture;
import slimeknights.tconstruct.library.client.texture.TinkerTexture;

/** Uses a (block) texture instead of a color to create the texture. */
public class BlockTextureRenderInfo extends AbstractMaterialRenderInfo {

  protected ResourceLocation texturePath;

  public BlockTextureRenderInfo(ResourceLocation texturePath) {
    this.texturePath = texturePath;
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    TextureAtlasSprite blockTexture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(texturePath.toString());

    if(blockTexture == null) {
      blockTexture = TinkerTexture.loadManually(texturePath);
    }

    TextureColoredTexture sprite = new TextureColoredTexture(new ResourceLocation(blockTexture.getIconName()), baseTexture, location);
    sprite.stencil = false;
    return sprite;
  }
}
