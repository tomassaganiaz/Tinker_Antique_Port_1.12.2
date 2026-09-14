package slimeknights.tconstruct.tactical;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArmorMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTraits;

public final class TacticalMaterials {
    public static final Material tacticalAlloy = new Material("tactical_alloy", 0x4a90e2);
    public static final Material tactisteel = new Material("tactisteel", 0xd4a76a);

    public static void register() {
        tacticalAlloy.setCraftable(true).setCastable(true);
        tacticalAlloy.addItemIngot("ingotTacticalAlloy");
        tacticalAlloy.setRepresentativeItem("ingotTacticalAlloy");
        tacticalAlloy.addTrait(TinkerTraits.lightweight, HeadMaterialStats.TYPE);
        tacticalAlloy.addTrait(TinkerTraits.magnetic);

        tactisteel.setCraftable(true).setCastable(true);
        tactisteel.addItemIngot("ingotTactisteel");
        tactisteel.setRepresentativeItem("ingotTactisteel");
        tactisteel.addTrait(TinkerTraits.heavy);
        tactisteel.addTrait(TinkerTraits.dense);
    }

    public static void registerStats() {
        TinkerRegistry.addMaterialStats(tacticalAlloy,
            new HeadMaterialStats(620, 7.2f, 5.8f, HarvestLevels.DIAMOND),
            new HandleMaterialStats(1.15f, 80),
            new ExtraMaterialStats(100));
        TinkerRegistry.addMaterialStats(tactisteel,
            new HeadMaterialStats(780, 6.5f, 6.2f, HarvestLevels.COBALT),
            new HandleMaterialStats(0.95f, 120),
            new ExtraMaterialStats(140));
        addArmor(tacticalAlloy, 18, 2,4,5,2, 1f, 0.02f);
        addArmor(tactisteel, 28, 2,5,7,2, 2f, 0.05f);
    }
    private static void addArmor(Material m,int f,int h,int c,int l,int b,float t,float k){
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_HELMET, f*11, h, t, k));
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_CHESTPLATE, f*16, c, t, k));
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_LEGGINGS, f*15, l, t, k));
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_BOOTS, f*13, b, t, k));
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_SHIELD, f*18, 0, t, k));
        TinkerRegistry.addMaterialStats(m, new ArmorMaterialStats(ArmorMaterialStats.TYPE_MAILLE, f*5, 1, 0, 0));
    }
}
