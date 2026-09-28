package slimeknights.tconstruct.library.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import slimeknights.tconstruct.library.client.texture.AnimatedColoredTexture;

/**
 * Creates an animated texture from an animated base texture. USE WITH CAUTION.
 * ACTUALLY ONLY USE THIS IF YOU KNOW EXACTLY WHAT YOU'RE DOING.
 */
public class AnimatedTextureRenderInfo extends AbstractMaterialRenderInfo {

  protected String texturePath;

  public AnimatedTextureRenderInfo(String texturePath) {
    this.texturePath = texturePath;
  }

  @Override
  public TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location) {
    TextureAtlasSprite blockTexture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(texturePath);

    if(blockTexture == null) {
      blockTexture = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    return new AnimatedColoredTexture(new ResourceLocation(blockTexture.getIconName()), baseTexture, location);
  }
}
