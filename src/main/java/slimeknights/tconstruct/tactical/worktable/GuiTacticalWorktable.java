package slimeknights.tconstruct.tactical.worktable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.Util;

public class GuiTacticalWorktable extends GuiContainer {
    private static final ResourceLocation BG = Util.getResource("textures/gui/tactical_worktable.png");
    public GuiTacticalWorktable(ContainerTacticalWorktable c){ super(c); xSize=176; ySize=166; }
    @Override protected void drawGuiContainerBackgroundLayer(float p, int mx, int my){
        GlStateManager.color(1,1,1,1);
        mc.getTextureManager().bindTexture(BG);
        drawTexturedModalRect(guiLeft, guiTop, 0,0, xSize, ySize);
    }
    @Override protected void drawGuiContainerForegroundLayer(int mx,int my){
        fontRenderer.drawString("Tactical Worktable", 8,6, 0x404040);
        fontRenderer.drawString("Inventory", 8, ySize-96+2, 0x404040);
    }
}
