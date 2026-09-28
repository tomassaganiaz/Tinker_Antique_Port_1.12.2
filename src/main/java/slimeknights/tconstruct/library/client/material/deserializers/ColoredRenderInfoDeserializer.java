package slimeknights.tconstruct.library.client.material.deserializers;

import slimeknights.tconstruct.library.client.DefaultRenderInfo;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class ColoredRenderInfoDeserializer extends AbstractRenderInfoDeserializer {

  protected String color;

  @Override
  public MaterialRenderInfo getMaterialRenderInfo() {
    return new DefaultRenderInfo(fromHex(color));
  }
}
