package slimeknights.tconstruct.plugin.jei.material;

import mezz.jei.api.IGuiHelper;
import net.minecraft.util.text.TextComponentTranslation;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.library.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArmorCategory extends AbstractCategory {
    public ArmorCategory(IGuiHelper guiHelper) {
        super(guiHelper, Reference.ARMOR_TYPES);
        icon = guiHelper.drawableBuilder(icon_location, 16, 0, 16, 16).setTextureSize(32, 32).build();
        title = new TextComponentTranslation("gui.jei.material.armor").getFormattedText();
        uuid = Util.MODID + ":armor_stats";
    }

    @Override
    protected List<String> additionalTooltips(List<String> statInfo, List<String> statDesc, int mouseX, int mouseY) {
        List<String> tooltip = new ArrayList<>();
        float height = 4 + HEADING_SPACING;
        for (int i = 0; i < Math.min(statInfo.size(), statDesc.size()); ++i) {
            if (isHovered(0, ClientProxy.fontRenderer.getStringWidth(statInfo.get(i)), height++, mouseX, mouseY)) {
                tooltip.addAll(formatTooltip(statDesc.get(i)));
            }
        }
        return tooltip;
    }

    @Override
    protected void drawStats(LinkedList<String> statInfo, float lineNumber) {
        String[] header = new String[]{
                getHeading("stat.plating_helmet.name"),
                getHeading("stat.plating_chestplate.name"),
                getHeading("stat.plating_leggings.name"),
                getHeading("stat.plating_boots.name")
        };
        int index = 0;
        for (int i = 0; i < statInfo.size(); i++) {
            if (i == 0 || i == 3 || i == 6 || i == 9) {
                drawComponent(header[index], 0, lineNumber++, materialWrapper.material.materialTextColor, true);
                lineNumber += HEADING_SPACING;
                index++;
                if(index >= header.length) index = header.length-1;
            }
            drawStatComponent(statInfo.get(i), lineNumber++);
            if(i == 0 || i == 3 || i == 6 || i == 9) {
                String type = slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_HELMET;
                if(i==3) type = slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_CHESTPLATE;
                else if(i==6) type = slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_LEGGINGS;
                else if(i==9) type = slimeknights.tconstruct.library.materials.ArmorMaterialStats.TYPE_BOOTS;
                int dur = slimeknights.tconstruct.library.materials.ArmorMaterialStats.getDurabilityMultiplier(type);
                try {
                    String dstr = statInfo.get(i);
                    int val = Integer.parseInt(dstr.split(": ")[1].replace(",", ""));
                    int factor = val / dur;
                    drawComponent("  Factor: " + factor, 0, lineNumber++, 0xFFAAAAAA, true);
                } catch(Exception e) {}
            }
        }
    }
}
