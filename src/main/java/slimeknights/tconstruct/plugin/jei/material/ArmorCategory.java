package slimeknights.tconstruct.plugin.jei.material;

import mezz.jei.api.IGuiHelper;
import net.minecraft.util.text.TextComponentTranslation;
import slimeknights.tconstruct.common.ClientProxy;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;

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
                getHeading("stat.plating_boots.name"),
                getHeading("stat.maille.name")
        };
        String[] types = new String[]{
                ArmorMaterialStats.TYPE_HELMET,
                ArmorMaterialStats.TYPE_CHESTPLATE,
                ArmorMaterialStats.TYPE_LEGGINGS,
                ArmorMaterialStats.TYPE_BOOTS,
                ArmorMaterialStats.TYPE_MAILLE
        };
        int index = 0;
        for (String stat : statInfo) {
            // every armor stat group begins with its durability line; emit the header for the next type there
            if (index < header.length && stat != null && stat.startsWith("Durability")) {
                drawComponent(header[index], 0, lineNumber++, materialWrapper.material.materialTextColor, true);
                lineNumber += HEADING_SPACING;
                int dur = ArmorMaterialStats.getDurabilityMultiplier(types[index]);
                try {
                    int val = Integer.parseInt(stat.split(": ")[1].replace(",", ""));
                    int factor = val / dur;
                    drawComponent("  Factor: " + factor, 0, lineNumber++, 0xFFAAAAAA, true);
                } catch(Exception e) {}
                index++;
            }
            drawStatComponent(stat, lineNumber++);
        }
    }
}