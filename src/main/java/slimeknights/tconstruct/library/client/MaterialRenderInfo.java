package slimeknights.tconstruct.library.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Determines the type of texture used for rendering a specific material.
 */
@SideOnly(Side.CLIENT)
public interface MaterialRenderInfo {

  TextureAtlasSprite getTexture(ResourceLocation baseTexture, String location);

  boolean isStitched();

  boolean useVertexColoring();

  int getVertexColor();

  String getTextureSuffix();

  MaterialRenderInfo setTextureSuffix(String suffix);
}