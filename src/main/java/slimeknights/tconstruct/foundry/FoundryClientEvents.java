package slimeknights.tconstruct.foundry;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.tconstruct.shared.client.BakedTableModel;

public class FoundryClientEvents {

  public static final ModelResourceLocation locFoundryTable = new ModelResourceLocation("tconstruct:foundry_casting", "type=table");
  public static final ModelResourceLocation locFoundryBasin = new ModelResourceLocation("tconstruct:foundry_casting", "type=basin");

  @SubscribeEvent
  public void onModelBake(ModelBakeEvent event) {
    wrap(event, locFoundryTable);
    wrap(event, locFoundryBasin);
  }

  private void wrap(ModelBakeEvent event, ModelResourceLocation loc) {
    IBakedModel model = event.getModelRegistry().getObject(loc);
    if(model != null) {
      event.getModelRegistry().putObject(loc, new BakedTableModel(model, null, DefaultVertexFormats.ITEM));
    }
  }
}
