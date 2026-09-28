package slimeknights.tconstruct.tools.common.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import slimeknights.tconstruct.library.client.renderer.RenderProjectileBase;
import slimeknights.tconstruct.tools.common.entity.EntityThrowingAxe;

public class RenderThrowingAxe extends RenderProjectileBase<EntityThrowingAxe> {

  public RenderThrowingAxe(RenderManager renderManager) {
    super(renderManager);
  }

  @Override
  public void customRendering(EntityThrowingAxe entity, double x, double y, double z, float entityYaw, float partialTicks) {
    GlStateManager.scale(0.8F, 0.8F, 0.8F);
    GlStateManager.rotate(entity.rotationYaw, 0f, 1f, 0f);
    GlStateManager.rotate(-entity.rotationPitch, 1f, 0f, 0f);
    GlStateManager.rotate(90f, 1f, 0f, 0f);
    if(!entity.inGround) {
      entity.spin += 15 * partialTicks;
    }
    GlStateManager.rotate(entity.spin, 0f, 0f, 1f);
  }
}