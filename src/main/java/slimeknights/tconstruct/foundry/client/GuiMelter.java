package slimeknights.tconstruct.foundry.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.util.List;

import slimeknights.mantle.client.gui.GuiElementScalable;
import slimeknights.mantle.client.gui.GuiMultiModule;
import slimeknights.tconstruct.foundry.inventory.ContainerMelter;
import slimeknights.tconstruct.foundry.tileentity.TileMelter;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.client.GuiUtil;
import slimeknights.tconstruct.smeltery.client.IGuiLiquidTank;

public class GuiMelter extends GuiMultiModule implements IGuiLiquidTank {

  public static final ResourceLocation BACKGROUND = Util.getResource("textures/gui/melter.png");

  // scala frame, drawn over the liquid after the tank itself. found in the texture at the same location it is drawn
  protected GuiElementScalable scala = new GuiElementScalable(116, 16, 52, 52, 256, 256);

  protected final TileMelter melter;

  public GuiMelter(ContainerMelter container, TileMelter tile) {
    super(container);

    this.melter = tile;
    xSize = 176;
    ySize = 166;
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    // we don't need to add the corner since the mouse is already relative to the corner
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);

    // subtract the corner of the main module so the mouse location is relative to the center
    mouseX -= cornerX;
    mouseY -= cornerY;

    // Liquids tooltip
    List<String> tooltip = GuiUtil.getTankTooltip(melter.getTank(), mouseX, mouseY, 116, 16, 52, 52);
    if(tooltip != null) {
      this.drawHoveringText(tooltip, mouseX, mouseY);
    }

    // melting progress tooltip
    if(!melter.getStackInSlot(TileMelter.INPUT).isEmpty() && mouseX >= 78 && mouseX < 102 && mouseY >= 22 && mouseY < 39) {
      float progress = melter.getProgress(TileMelter.INPUT);
      String progressTooltip = null;

      if(Float.isNaN(progress)) {
        progressTooltip = "gui.smeltery.progress.no_recipe";
      }
      else if(!melter.hasFuel() && progress <= 0f) {
        progressTooltip = "gui.smeltery.progress.no_fuel";
      }
      else if(progress > 2f) {
        progressTooltip = "gui.smeltery.progress.no_space";
      }

      if(progressTooltip != null) {
        this.drawHoveringText(this.fontRenderer.listFormattedStringToWidth(Util.translate(progressTooltip), 100), mouseX, mouseY);
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    drawBackground(BACKGROUND);

    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    // draw the liquids in the tank
    GuiUtil.drawGuiTank(melter.getTank(), 116 + cornerX, 16 + cornerY, scala.w, scala.h, this.zLevel);

    // draw the scala frame over the liquids
    this.mc.getTextureManager().bindTexture(BACKGROUND);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    scala.draw(116 + cornerX, 16 + cornerY);

    // flame: full if we have fuel, empty socket otherwise
    int flameTexX = melter.hasFuel() ? 176 : 192;
    GuiScreen.drawModalRectWithCustomSizedTexture(33 + cornerX, 52 + cornerY, flameTexX, 76, 16, 16, 256, 256);

    // melting progress arrow
    if(melter.hasFuel() && melter.getProgress(TileMelter.INPUT) > 0f) {
      float progress = Math.min(1f, melter.getProgress(TileMelter.INPUT));
      int w = Math.round(24 * progress);
      GuiScreen.drawModalRectWithCustomSizedTexture(78 + cornerX, 22 + cornerY, 176, 96, w, 17, 256, 256);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if(mouseButton == 0) {
      GuiUtil.handleTankClick(melter.getTank(), mouseX - cornerX, mouseY - cornerY, 116, 16, 52, 52);
    }
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }

  @Override
  public FluidStack getFluidStackAtPosition(int mouseX, int mouseY) {
    return GuiUtil.getFluidStackAtPosition(melter.getTank(), mouseX - cornerX, mouseY - cornerY, 116, 16, 52, 52);
  }
}